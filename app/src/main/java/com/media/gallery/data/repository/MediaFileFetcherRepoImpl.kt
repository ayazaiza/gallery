package com.media.gallery.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.BaseColumns
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.media.gallery.config.AppConstants.ANDROID_DATA_DIR
import com.media.gallery.config.AppConstants.DIRS_ACCESSIBLE_ONLY_WITH_SAF
import com.media.gallery.config.AppConstants.NOMEDIA
import com.media.gallery.config.AppConstants.SD_OTG_PATTERN
import com.media.gallery.config.AppConstants.TYPE_AUDIOS
import com.media.gallery.config.AppConstants.TYPE_GIFS
import com.media.gallery.config.AppConstants.TYPE_IMAGES
import com.media.gallery.config.AppConstants.TYPE_PORTRAITS
import com.media.gallery.config.AppConstants.TYPE_RAW
import com.media.gallery.config.AppConstants.TYPE_SVG
import com.media.gallery.config.AppConstants.TYPE_VIDEOS
import com.media.gallery.config.AppConstants.getInternalStorageLocation
import com.media.gallery.config.AppConstants.ignoredTryCatch
import com.media.gallery.config.AppConstants.isRPlus
import com.media.gallery.config.AppConstants.physicalPaths
import com.media.gallery.config.AppConstants.videoExtensions
import com.media.gallery.config.Resource
import com.media.gallery.data.data_source.PlayerRoomDatabase
import com.media.gallery.data.data_source.dao.entities.GalleryMediaItemEntity
import com.media.gallery.data.mapper.toGalleryMediaItem
import com.media.gallery.domain.extensions.getDistinctPath
import com.media.gallery.domain.extensions.getDuration
import com.media.gallery.domain.extensions.getFilenameFromPath
import com.media.gallery.domain.extensions.getIntValue
import com.media.gallery.domain.extensions.getLongValue
import com.media.gallery.domain.extensions.getMimeType
import com.media.gallery.domain.extensions.getParentPath
import com.media.gallery.domain.extensions.getStorageDirectories
import com.media.gallery.domain.extensions.getStringValue
import com.media.gallery.domain.extensions.getStringValueOrNull
import com.media.gallery.domain.extensions.isAudioFast
import com.media.gallery.domain.extensions.isGif
import com.media.gallery.domain.extensions.isImageFast
import com.media.gallery.domain.extensions.isMediaFile
import com.media.gallery.domain.extensions.isRawFast
import com.media.gallery.domain.extensions.isSvg
import com.media.gallery.domain.extensions.isVideoFast
import com.media.gallery.domain.extensions.shouldFolderBeVisible
import com.media.gallery.domain.extensions.shouldFolderBeVisibleForVideos
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.repository.MediaFileFetcherRepo
import com.media.gallery.domain.repository.SharedPreferenceRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.roundToLong


class MediaFileFetcherRepoImpl(
    private val sharedPreferenceRepo: SharedPreferenceRepo,
    private val playerRoomDatabase: PlayerRoomDatabase,
    private val context: Context,
) : MediaFileFetcherRepo {

    private val _isMediaFetching = MutableStateFlow(false)

    private val sdCardPath get() = getDefaultSDCardPath()
    private val internalStoragePath get() = getDefaultInternalPath()

    private var fetchJob: Job? = null

    private val oTGPath get() = sharedPreferenceRepo.oTGPath

    override fun getAllVideos(path: String?): Flow<Resource<List<GalleryMediaItem>>> {
        return getAllVideosFromDb(path).map {
            Resource.Success(it.map { dd -> dd.toGalleryMediaItem() })
        }
    }


    override fun getVideoCard(path: String): Flow<Resource<GalleryMediaItem?>> {
        return playerRoomDatabase.videoCardsDao.getMediaFileFromPathFlow(path).map {
            val videoCard = it.firstOrNull()?.toGalleryMediaItem()
            Resource.Success(videoCard)
        }
    }

    override fun getVideoCardById(id: String): Flow<Resource<GalleryMediaItem?>> {
        return playerRoomDatabase.videoCardsDao.getMediaFileFromIdByFlow(id).map {
            val videoCard = it.firstOrNull()?.toGalleryMediaItem()
            Resource.Success(videoCard)
        }
    }

    override fun cancelJob() {
        fetchJob?.cancel()
    }

    override suspend fun deleteVideoCard(path: String) {
        playerRoomDatabase.videoCardsDao.deleteMediumPath(path = path)
    }

    private var _queues: List<GalleryMediaItem> = emptyList()
    override val queueMediaItem: List<GalleryMediaItem>
        get() = _queues
    override val isMediaFetching: StateFlow<Boolean>
        get() = _isMediaFetching.asStateFlow()

    override suspend fun searchVideos(query: String): List<GalleryMediaItem> {
        return playerRoomDatabase.videoCardsDao.search(query)
            .map { galleryItem -> galleryItem.toGalleryMediaItem() }
    }

    override fun setQueuedVideos(list: List<GalleryMediaItem>) {
        _queues = list
    }


    override fun fetchAllFiles(): Flow<Resource<List<GalleryMediaItem>>> {
        return playerRoomDatabase.videoCardsDao.getAllMediaFilesFlow()
            .map {
                Resource.Success(it.map { dir ->
                    dir.toGalleryMediaItem().copy(
                        mediaCount = playerRoomDatabase.videoCardsDao.getFoldersCount(dir.parentPath)
                            .toInt()
                    )
                })
            }
    }


    override fun checkAllFolders() {
        fetchJob?.cancel()
        _isMediaFetching.update { true }
        fetchJob = CoroutineScope(Dispatchers.IO).launch {
            val includedPaths = sharedPreferenceRepo.includedFolders
            val excludedPaths = sharedPreferenceRepo.excludedFolders
            val directories = playerRoomDatabase.videoCardsDao.getAllMediaFiles()
            _isMediaFetching.update { directories.isEmpty() }
            val filterMedia = sharedPreferenceRepo.filterMedia
            val noMediaFolderFiles = HashMap<String, Boolean>()
            directories.filter {
                !getDoesFilePathExist(it.path, oTGPath)
            }.forEach {
                ignoredTryCatch {
                    playerRoomDatabase.videoCardsDao.deleteMediumPath(it.path)
                }
            }
            val filteredDirectories = ArrayList(directories.filter { it.type == filterMedia }
                .distinctBy { it.parentPath }).filter {
                it.path.shouldFolderBeVisibleForVideos(
                    excludedPaths, includedPaths, true, noMediaFolderFiles
                ) { _, _ ->

                }
            } as ArrayList<GalleryMediaItemEntity>
            filteredDirectories.forEach {
                println(it.parentPath)
            }
            gotDirectories(filteredDirectories)
            _isMediaFetching.update { false }
        }
    }

    // 24 folders
    private suspend fun gotDirectories(newDirs: ArrayList<GalleryMediaItemEntity>) {
        val android11files = getAndroid11FolderMedia()
        val dirPathsToRemove = ArrayList<String>()
        ignoredTryCatch {
            for (directory in newDirs) {
                val newMedia = getFilesFrom(
                    curPath = directory.parentPath,
                    android11Files = android11files,
                    parentName = directory.folderName
                )
                if (newMedia.isEmpty()) {
                    dirPathsToRemove.add(directory.parentPath)
                }
                ignoredTryCatch { playerRoomDatabase.videoCardsDao.insertAll(newMedia) }
                val mediaToDelete = arrayListOf<GalleryMediaItemEntity>()
                getCachedMedia(directory.parentPath) {
                    it.forEach { mediaFile ->
                        if (!newMedia.contains(mediaFile)) {
                            mediaToDelete.add(mediaFile)
                        }
                    }
                }
                if (mediaToDelete.isNotEmpty()) {
                    playerRoomDatabase.videoCardsDao.deleteMedia(*mediaToDelete.toTypedArray())
                }
            }
            if (dirPathsToRemove.isNotEmpty()) {
                val dirsToRemove = newDirs.filter { dirPathsToRemove.contains(it.parentPath) }
                dirsToRemove.forEach {
                    ignoredTryCatch {
                        playerRoomDatabase.videoCardsDao.deleteMediaFileByParentPath(it.parentPath)
                    }
                }
            }
        }
        val foldersToScan = fetchFoldersToScan().toMutableList()

        for (folder in foldersToScan) {
            val newMedia = ArrayList<GalleryMediaItemEntity>()
            val parentName = folder.getFilenameFromPath()
            val tmpMedia = getFilesFrom(
                curPath = folder,
                parentName = parentName,
                android11Files = android11files,
            )
            for (media in tmpMedia) {
                if (media.folderName == "") {
                    media.folderName = parentName
                }
                newMedia.add(media)
            }

            if (newMedia.isEmpty()) {
                continue
            }

            ignoredTryCatch {
                playerRoomDatabase.videoCardsDao.insertAll(newMedia)
            }
        }
        if (newDirs.size > 50) {
            excludeSpamFolders(newDirs)
        }

        val excludedFolders = sharedPreferenceRepo.excludedFolders
        val everShownPaths = sharedPreferenceRepo.everShownFolders.toMutableSet()

        newDirs.filter { dir ->
            if (excludedFolders.any { dir.parentPath.startsWith(it) }) {
                return@filter false
            }
            return@filter true
        }.mapTo(everShownPaths) { it.parentPath }

        try {
            everShownPaths.addAll(getFoldersWithMedia(internalStoragePath, context))
            sharedPreferenceRepo.everShownFolders = everShownPaths
        } catch (_: Exception) {
            sharedPreferenceRepo.everShownFolders = HashSet()
        }

    }


    private fun getNoMediaFolder(): List<String> {
        val list = java.util.ArrayList<String>()
        val uri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
        val selection =
            "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? AND ${MediaStore.Files.FileColumns.TITLE} LIKE ?"
        val selectionArgs =
            arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_NONE.toString(), "%$NOMEDIA%")
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"

        var query: Cursor? = null
        try {
            query =
                context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
            query?.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val path =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                                ?: continue
                        val file = File(path)
                        if (file.name == NOMEDIA && file.parent != null) {
                            list.add(file.parent!!)
                        }
                    } while (cursor.moveToNext())
                }
            }
        } catch (_: Exception) {
        } finally {
            query?.close()
        }
        return list
    }

    private fun getFoldersWithMedia(path: String, context: Context): HashSet<String> {
        val folders = HashSet<String>()
        ignoredTryCatch {
            val files = File(path).listFiles()
            if (files != null) {
                files.sortBy { it.isDirectory }
                for (file in files) {
                    if (file.isDirectory || !file.startsWith("${internalStoragePath}/Android")) {
                        folders.addAll(getFoldersWithMedia(file.absolutePath, context))
                    } else if (file.isFile || file.isMediaFile()) {
                        folders.add(file.parent ?: "")
                        break
                    }
                }
            }
        }
        return folders
    }


    private fun getAllVideosFromDb(path: String?): Flow<List<GalleryMediaItemEntity>> {
        return if (path.isNullOrEmpty()) {
            playerRoomDatabase.videoCardsDao.getAllMediaFilesFlow()
        } else {
            playerRoomDatabase.videoCardsDao.getMediaFromPathFlow(path)
        }
    }

    private fun excludeSpamFolders(newDirs: ArrayList<GalleryMediaItemEntity>) {
        ignoredTryCatch {
            val internalPath = internalStoragePath
            val checkedPaths = arrayListOf<String>()
            val oftenRepeatedPaths = arrayListOf<String>()
            val paths = newDirs.map { it.parentPath.removePrefix(internalPath) }.toMutableList()
            paths.forEach { path ->
                val parts = path.split('/')
                var currentString = ""
                for (part in parts) {
                    currentString += "$part/"
                    if (!checkedPaths.contains(currentString)) {
                        val cnt = paths.count { it.startsWith(currentString) }
                        if (cnt > 50 && currentString.startsWith("/Android/data", true)) {
                            oftenRepeatedPaths.add(currentString)
                        }
                    }
                    checkedPaths.add(currentString)
                }
            }
            val subStringToRemove = oftenRepeatedPaths.filter { path ->
                path == "/" || oftenRepeatedPaths.any { it != path && it.startsWith(path) }
            }
            oftenRepeatedPaths.removeAll(subStringToRemove.toSet())

            oftenRepeatedPaths.forEach {
                val file = File("$internalPath/$it")
                if (getDoesFilePathExist(file.absolutePath, oTGPath)) {
                    sharedPreferenceRepo.addExcludedFolder(file.absolutePath)
                }
            }
        }
    }


    private suspend fun getCachedMedia(
        path: String?, callback: (ArrayList<GalleryMediaItemEntity>) -> Unit
    ) {
        val foldersToScan = if (path.isNullOrEmpty()) fetchFoldersToScan() else arrayListOf(path)
        var media = arrayListOf<GalleryMediaItemEntity>()
        foldersToScan.filter { it.isNotEmpty() }.forEach {
            ignoredTryCatch {
                val currMedia = playerRoomDatabase.videoCardsDao.getMediaFromPath(it)
                media.addAll(currMedia)
            }
        }

        val filterMedia = TYPE_VIDEOS

        media = media.filter { it.type == filterMedia } as ArrayList<GalleryMediaItemEntity>

        callback(media)
        ignoredTryCatch {
            val mediaToDelete = arrayListOf<GalleryMediaItemEntity>()
            media.filter { !getDoesFilePathExist(it.path, oTGPath) }.forEach {
                mediaToDelete.add(it)
            }
            if (mediaToDelete.isNotEmpty()) {
                ignoredTryCatch {
                    playerRoomDatabase.videoCardsDao.deleteMedia(*mediaToDelete.toTypedArray())
                }
            }
        }
    }

    private fun getLatestUpToTenFolder(context: Context): LinkedHashSet<String> {
        val uri = MediaStore.Files.getContentUri("external")
        val projections = arrayOf(MediaStore.Images.ImageColumns.DATA)
        val parents = LinkedHashSet<String>()
        var cursor: Cursor? = null
        try {
            cursor = if (isRPlus()) {
                val bundle = Bundle().apply {
                    putInt(ContentResolver.QUERY_ARG_LIMIT, 10)
                    putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(BaseColumns._ID))
                    putInt(
                        ContentResolver.QUERY_ARG_SORT_DIRECTION,
                        ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                    )
                }
                context.contentResolver.query(uri, projections, bundle, null)
            } else {
                val sorting = "${BaseColumns._ID} DESC LIMIT 10"
                context.contentResolver.query(uri, projections, null, null, sorting)
            }

            if (cursor?.moveToFirst() == true) {
                do {
                    val data =
                        cursor.getStringValueOrNull(MediaStore.Images.ImageColumns.DATA) ?: continue
                    parents.add(data.getParentPath())
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }
        return parents
    }


    private fun fetchFoldersToScan(): List<String> {
        return try {
            val folders = getLatestUpToTenFolder(context)
            folders.addAll(arrayListOf(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString(),
                "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/Camera",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString(),
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                    .toString(),
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                    .toString(),
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString(),
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS)
                    .toString(),
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES)
                    .toString()
            ).filter { getDoesFilePathExist(it, oTGPath) })
            val foldersToIgnore = arrayListOf("/storage/emulated/legacy")
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val selection = getSelectionQuery()
            val selectionArgs = getSelectionArgsQuery().toTypedArray()
            val cursor =
                context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            val foldersToScan = sharedPreferenceRepo.everShownFolders.toHashSet()
            val includedFolders = sharedPreferenceRepo.includedFolders
            cursor?.use {
                if (cursor.moveToFirst()) {
                    do {
                        val path =
                            cursor.getStringValueOrNull(MediaStore.Video.Media.DATA) ?: continue
                        val parentPath = File(path).parent ?: continue
                        if (!includedFolders.contains(parentPath) && !foldersToIgnore.contains(
                                parentPath
                            )
                        ) {
                            foldersToScan.add(parentPath)
                        }
                    } while (cursor.moveToNext())
                }
            }
            cursor?.close()
            includedFolders.forEach {
                addFolder(foldersToScan, it)
            }

            folders.addAll(foldersToScan)
            val folderNoMediaStatuses = HashMap<String, Boolean>()

            val distinctPathsMap = HashMap<String, String>()
            val distinctPaths = folders.distinctBy {
                when {
                    distinctPathsMap.containsKey(it) -> distinctPathsMap[it]
                    else -> {
                        val distinct = it.getDistinctPath()
                        distinctPathsMap[it.getParentPath()] = distinct.getParentPath()
                        distinct
                    }
                }
            }
            val noMediaFolders = getNoMediaFolder()
            noMediaFolders.forEach { folder ->
                folderNoMediaStatuses["$folder/$NOMEDIA"] = true
            }
            distinctPaths.filter {
                it.shouldFolderBeVisible(
                    sharedPreferenceRepo.excludedFolders,
                    includedFolders,
                    true,
                    folderNoMediaStatuses
                ) { path, hasNoMedia ->
                    folderNoMediaStatuses[path] = hasNoMedia
                }
            }.toMutableList()

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun addFolder(curFolders: HashSet<String>, folder: String) {
        curFolders.add(folder)
        val files = File(folder).listFiles() ?: return
        for (file in files) {
            if (file.isDirectory) {
                addFolder(curFolders, file.absolutePath)
            }
        }
    }

    private fun getSelectionQuery(): String {
        return buildString {
            repeat(videoExtensions.count()) {
                append("${MediaStore.Video.Media.DATA} LIKE ? OR ")
            }
        }.trim().removeSuffix("OR")
    }

    private fun getSelectionArgsQuery(): ArrayList<String> {
        val args = ArrayList<String>()
        videoExtensions.forEach {
            args.add("%$it")
        }
        return args
    }


    private fun getFilesFrom(
        curPath: String,
        parentName: String,
        android11Files: HashMap<String, ArrayList<GalleryMediaItemEntity>>?,
    ): List<GalleryMediaItemEntity> {
        val filterMedia = sharedPreferenceRepo.filterMedia
        if (filterMedia == 0) {
            return emptyList()
        }
        val curMedia = ArrayList<GalleryMediaItemEntity>()

        if (isPathOnOTG(curPath)) {
            if (hasOTGConnected()) {
                val newMedia = getMediaOnOTG(curPath, filterMedia)
                curMedia.addAll(newMedia)
            }
        } else {
            if (isRPlus() && !Environment.isExternalStorageManager()) {
                if (android11Files != null) {
                    if (android11Files.containsKey(curPath.lowercase())) {
                        curMedia.addAll(android11Files[curPath.lowercase()]!!)
                    }
                } else {
                    val files = getAndroid11FolderMedia()
                    if (files.containsKey(curPath.lowercase())) {
                        curMedia.addAll(files[curPath.lowercase()]!!)
                    }
                }
            }
            if (curMedia.isEmpty()) {
                val deepFolder = getMediaInFolder(
                    folder = curPath,
                    filterMedia = filterMedia,
                    parentName = parentName
                )
                if (isRPlus() && !Environment.isExternalStorageManager()) {
                    val files = getAndroid11FolderMedia()
                    deepFolder.forEach { newVideoCard ->
                        for ((_, media) in files) {
                            media.forEach { card ->
                                if (card.path == newVideoCard.path) {
                                    newVideoCard.size = card.size
                                }
                            }
                        }
                    }
                }
                deepFolder.forEach { deepFile ->
                    if (!curMedia.any { it.path.equals(deepFile.path, true) }) {
                        curMedia.add(deepFile)
                    }
                }
            }
        }
        return curMedia
    }

    private fun getMediaInFolder(
        folder: String,
        parentName: String,
        filterMedia: Int,
    ): List<GalleryMediaItemEntity> {
        val mediaFile = ArrayList<GalleryMediaItemEntity>()
        val files = File(folder).listFiles()?.toMutableList() ?: return mediaFile

        files.forEach { file ->
            val path = file.absolutePath
            val filename = file.name
            val isImage = path.isImageFast()
            val isVideo = if (isImage) false else path.isVideoFast()
            val isGif = if (isImage || isVideo) false else path.isGif()
            val isRaw = if (isImage || isVideo || isGif) false else path.isRawFast()
            val isSvg = if (isImage || isVideo || isGif || isRaw) false else path.isSvg()
            if (!isImage && !isVideo && !isGif && !isRaw && !isSvg) {
                return@forEach
            }

            if (isVideo && (filterMedia and TYPE_VIDEOS == 0)) {
                return@forEach
            }

            if (isImage && (filterMedia and TYPE_IMAGES == 0))
                return@forEach

            if (isGif && filterMedia and TYPE_GIFS == 0)
                return@forEach

            if (isRaw && filterMedia and TYPE_RAW == 0)
                return@forEach

            if (isSvg && filterMedia and TYPE_SVG == 0)
                return@forEach

            if (file.length() <= 0L || !file.exists() || !file.isFile) {
                return@forEach
            }
            val type = when {
                isVideo -> TYPE_VIDEOS
                isGif -> TYPE_GIFS
                isRaw -> TYPE_RAW
                isSvg -> TYPE_SVG
                else -> TYPE_IMAGES
            }
            val duration = context.getDuration(file.absolutePath) ?: 0L
            val media = GalleryMediaItemEntity(
                title = filename,
                path = path,
                parentPath = file.parent ?: path.getParentPath(),
                lastModified = file.lastModified(),
                taken = file.lastModified(),
                type = type,
                size = file.length(),
                duration = duration,
                deletedTS = 0L,
                mimeType = filename.getMimeType(),
                timestamp = System.currentTimeMillis(),
                isFavorite = false,
                folderName = parentName,
            )
            mediaFile.add(media)
        }

        return mediaFile
    }

    private fun getMediaOnOTG(
        folder: String,
        filterMedia: Int
    ): ArrayList<GalleryMediaItemEntity> {
        val media = ArrayList<GalleryMediaItemEntity>()
        val files = getDocumentFile(folder)?.listFiles() ?: return media
        val showHidden = true
        val otgPath = sharedPreferenceRepo.oTGPath

        files.forEach { file ->
            val filename = file.name ?: return@forEach
            val isVideo = filename.isVideoFast()
//            val isAudio = if (isVideo) false else filename.isAudioFast()
            val isImage = if (isVideo) false else filename.isImageFast()
            val isGif = if (isImage || isVideo) false else filename.isGif()
            val isRaw = if (isImage || isVideo || isGif) false else filename.isRawFast()
            val isSvg = if (isImage || isVideo || isGif || isRaw) false else filename.isSvg()
            if (!isImage && !isVideo && !isGif && !isRaw && !isSvg)
                return@forEach
            if (isVideo && filterMedia and TYPE_VIDEOS == 0)
                return@forEach

            if (isImage && filterMedia and TYPE_IMAGES == 0)
                return@forEach

            if (isGif && filterMedia and TYPE_GIFS == 0)
                return@forEach

            if (isRaw && filterMedia and TYPE_RAW == 0)
                return@forEach

            if (isSvg && filterMedia and TYPE_SVG == 0)
                return@forEach
            /*         if (isVideo && (getOnlyAudios || filterMedia and TYPE_VIDEOS == 0)) return@forEach
                     if (isAudio && (getOnlyVideos || filterMedia and TYPE_AUDIOS == 0)) return@forEach*/
            if (!showHidden && filename.startsWith(".")) return@forEach
            val size = file.length()
            if (size <= 0L || !getDoesFilePathExist(
                    file.uri.toString(), otgPath
                )
            ) return@forEach

            val dateTaken = file.lastModified()
            val dateModified = file.lastModified()

            val type = when {
                isVideo -> TYPE_VIDEOS
                isGif -> TYPE_GIFS
                isRaw -> TYPE_RAW
                isSvg -> TYPE_SVG
                else -> TYPE_IMAGES
            }
            val path = Uri.decode(
                file.uri.toString().replaceFirst(
                    "${sharedPreferenceRepo.oTGTreeUri}/document/${sharedPreferenceRepo.oTGPartition}%3A",
                    "${sharedPreferenceRepo.oTGPath}/"
                )
            )

            val mediaFile = GalleryMediaItemEntity(
                path = path,
                title = filename,
                parentPath = folder,
                size = size,
                type = type,
                isFavorite = false,
                deletedTS = 0L,
                mimeType = file.type ?: filename.getMimeType(),
                duration = 0L,
                taken = dateTaken,
                lastModified = dateModified,
                mediaStoreId = 0L
            )
            media.add(mediaFile)
        }
        return media
    }

    private fun hasOTGConnected(): Boolean {
        return try {
            (context.getSystemService(Context.USB_SERVICE) as UsbManager).deviceList.any {
                it.value.getInterface(0).interfaceClass == UsbConstants.USB_CLASS_MASS_STORAGE
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun getDoesFilePathExist(path: String, otgPath: String = ""): Boolean {
        return when {
            File(path).exists() -> true
            isRestrictedSAFOnlyRoot(path = path) -> getFastAndroidSAFDocument(path)?.exists()
                ?: false

            otgPath.isNotEmpty() && path.startsWith(otgPath) -> getOTGFastDocumentFile(path)?.exists()
                ?: false

            else -> false
        }
    }

    private fun getOTGFastDocumentFile(path: String, otgPathToUse: String? = null): DocumentFile? {
        if (sharedPreferenceRepo.oTGTreeUri.isEmpty()) {
            return null
        }

        val otgPath = otgPathToUse ?: sharedPreferenceRepo.oTGPath

        if (sharedPreferenceRepo.oTGPartition.isEmpty()) {
            sharedPreferenceRepo.oTGPartition =
                sharedPreferenceRepo.oTGTreeUri.removeSuffix("%3A").substringAfterLast('/')
                    .trimEnd('/')
            updateOTGPathFromPartition()
        }

        val relativePath = Uri.encode(path.substring(otgPath.length).trim('/'))
        val fullUri =
            "${sharedPreferenceRepo.oTGTreeUri}/document/${sharedPreferenceRepo.oTGPartition}%3A$relativePath"
        return DocumentFile.fromSingleUri(context, Uri.parse(fullUri))

    }

    private fun updateOTGPathFromPartition() {
        val otgPath = "/storage/${sharedPreferenceRepo.oTGPartition}"
        sharedPreferenceRepo.oTGPath =
            if (getOTGFastDocumentFile(otgPath, otgPath)?.exists() == true) {
                otgPath
            } else {
                "/mnt/media_rw/${sharedPreferenceRepo.oTGPartition}"
            }
    }

    private fun getFastAndroidSAFDocument(path: String): DocumentFile? {
        val treeUri = getAndroidTreeUri(path)
        if (treeUri.isEmpty()) {
            return null
        }
        val uri = getAndroidSAFUriByTreeUri(path, treeUri.toUri())
        return DocumentFile.fromSingleUri(context, uri)
    }

    private fun getAndroidSAFUriByTreeUri(path: String, treeUri: Uri): Uri {
        val documentId = createAndroidSAFDocumentId(path)
        return DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
    }

    private fun createAndroidSAFDocumentId(path: String): String {
        val basePath = getBasePath(path)
        val relativePath = path.substring(basePath.length).trim('/')
        val storageId = getStorageRootIdForAndroidDir(path)
        return "$storageId:$relativePath"
    }

    private fun getBasePath(path: String): String {
        return when {
            path.startsWith(internalStoragePath) -> internalStoragePath
            isPathOnOTG(path) -> sharedPreferenceRepo.oTGPath
            isPathOnSD(path) -> sharedPreferenceRepo.sdCardPath
            else -> "/"
        }
    }

    private fun getStorageRootIdForAndroidDir(path: String): String {
        return getAndroidTreeUri(path).removeSuffix(if (isAndroidDataDir(path)) "%3AAndroid%2Fdata" else "%3AAndroid%2Fobb")
            .substringAfterLast('/').trim('/')
    }

    private fun isSAFOnlyRoot(path: String): Boolean {
        return getSAFOnlyDirs().any { "${path.trimEnd('/')}/".startsWith(it) }
    }

    private fun getSAFOnlyDirs(): List<String> {
        return DIRS_ACCESSIBLE_ONLY_WITH_SAF.map { "$internalStoragePath$it" } + DIRS_ACCESSIBLE_ONLY_WITH_SAF.map {
            "$sdCardPath$it"
        }
    }


    private fun isRestrictedSAFOnlyRoot(path: String): Boolean {
        return isSAFOnlyRoot(path = path) && isRPlus()
    }

    private fun getAndroidTreeUri(path: String): String {
        return when {
            isPathOnOTG(path) -> if (isAndroidDataDir(path)) sharedPreferenceRepo.otgAndroidDataTreeUri else sharedPreferenceRepo.otgAndroidObbTreeUri
            isPathOnSD(path) -> if (isAndroidDataDir(path)) sharedPreferenceRepo.sdAndroidDataTreeUri else sharedPreferenceRepo.sdAndroidObbTreeUri
            else -> if (isAndroidDataDir(path)) sharedPreferenceRepo.primaryAndroidDataTreeUri else sharedPreferenceRepo.primaryAndroidObbTreeUri
        }
    }

    private fun isAndroidDataDir(path: String): Boolean {
        return "${path.trimEnd('/')}/".contains(ANDROID_DATA_DIR)
    }

    private fun isPathOnSD(path: String): Boolean =
        getDefaultSDCardPath().isNotEmpty() && path.startsWith(getDefaultSDCardPath())


    private fun getAndroid11FolderMedia(): HashMap<String, ArrayList<GalleryMediaItemEntity>> {
        val media = HashMap<String, ArrayList<GalleryMediaItemEntity>>()
        if (!isRPlus() || Environment.isExternalStorageManager()) {
            return media
        }
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.SIZE,
            MediaStore.MediaColumns.DURATION,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.BUCKET_ID
        )
        val uri = MediaStore.Files.getContentUri("external")
        val cursor = context.contentResolver.query(
            uri, projection, null, null, null
        )
        try {
            cursor?.use {
                if (cursor.moveToFirst()) {
                    do {
                        try {
                            val mediaStoreId = cursor.getLongValue(MediaStore.Video.Media._ID)
                            val filename =
                                cursor.getStringValue(MediaStore.Images.Media.DISPLAY_NAME)
                            val path = cursor.getStringValue(MediaStore.Images.Media.DATA)
                            val height = cursor.getIntValue(MediaStore.Images.Media.HEIGHT)
                            val width = cursor.getIntValue(MediaStore.Images.Media.WIDTH)
                            val isPortrait = false
                            val isImage = path.isImageFast()
                            val isVideo = if (isImage) false else path.isVideoFast()
                            val isGif = if (isImage || isVideo) false else path.isGif()
                            val isRaw = if (isImage || isVideo || isGif) false else path.isRawFast()
                            val isSvg =
                                if (isImage || isVideo || isGif || isRaw) false else path.isSvg()

                            val contentUri = ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaStoreId
                            )

                            if (!isImage && !isVideo && !isGif && !isRaw && !isSvg) {
                                continue
                            }
                            val size = cursor.getLongValue(MediaStore.Images.Media.SIZE)
                            if (size <= 0L) {
                                continue
                            }

                            val type = when {
                                isVideo -> TYPE_VIDEOS
                                isGif -> TYPE_GIFS
                                isRaw -> TYPE_RAW
                                isSvg -> TYPE_SVG
                                isPortrait -> TYPE_PORTRAITS
                                else -> TYPE_IMAGES
                            }

                            val lastModified =
                                cursor.getLongValue(MediaStore.Images.Media.DATE_MODIFIED) * 1000
                            var dateTaken = cursor.getLongValue(MediaStore.Images.Media.DATE_TAKEN)

                            if (dateTaken == 0L) {
                                dateTaken = lastModified
                            }
                            val videoDuration =
                                (cursor.getIntValue(MediaStore.MediaColumns.DURATION) / 1000.toDouble()).roundToLong()
                            val medium = GalleryMediaItemEntity(
                                title = filename,
                                path = path,
                                parentPath = path.getParentPath(),
                                lastModified = lastModified,
                                taken = dateTaken,
                                size = size,
                                type = type,
                                duration = videoDuration,
                                isFavorite = false,
                                deletedTS = 0L,
                                mediaStoreId = mediaStoreId,
                                width = width,
                                height = height,
                                mimeType = filename.getMimeType(),
                                uri = contentUri.toString()
                            )
                            val parent = medium.parentPath.lowercase(Locale.getDefault())
                            val currentFolderMedia = media[parent]
                            if (currentFolderMedia == null) {
                                media[parent] = ArrayList()
                            }
                            media[parent]?.add(medium)
                        } catch (_: Exception) {
                        }
                    } while (cursor.moveToNext())
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return media
    }

    private fun getDocumentFile(path: String): DocumentFile? {
        val isOTG = isPathOnOTG(path)
        var relativePath =
            path.substring(if (isOTG) sharedPreferenceRepo.oTGPath.length else getDefaultSDCardPath().length)
        if (relativePath.startsWith(File.separator)) {
            relativePath = relativePath.substring(1)
        }

        return try {
            val treeUri =
                Uri.parse(if (isOTG) sharedPreferenceRepo.oTGTreeUri else sharedPreferenceRepo.sdTreeUri)
            var document = DocumentFile.fromTreeUri(context.applicationContext, treeUri)
            val parts = relativePath.split('/').filter { it.isNotEmpty() }
            for (part in parts) {
                document = document?.findFile(path)
            }
            document
        } catch (_: Exception) {
            null
        }
    }

    private fun isPathOnOTG(path: String): Boolean {
        val otg = sharedPreferenceRepo.oTGPath
        return otg.isNotEmpty() && otg.startsWith(path)
    }

    private fun getDefaultSDCardPath(): String {
        val sdPath =
            if (sharedPreferenceRepo.sdCardPath == "") getSDCardPath() else sharedPreferenceRepo.sdCardPath
        sharedPreferenceRepo.sdCardPath = sdPath
        return sdPath
    }

    private fun getDefaultInternalPath(): String {
        val internalPath =
            if (sharedPreferenceRepo.internalStoragePath == "") getInternalStorageLocation() else sharedPreferenceRepo.internalStoragePath
        sharedPreferenceRepo.internalStoragePath = internalPath
        return internalPath
    }


    private fun getSDCardPath(): String {
        val directories = context.getStorageDirectories().filter {
            it != getInternalStorageLocation() && !it.equals(
                "/storage/emulated/0", true
            ) && (sharedPreferenceRepo.oTGPartition.isEmpty() || !it.endsWith(sharedPreferenceRepo.oTGPartition))
        }

        val fullSDPattern = Pattern.compile(SD_OTG_PATTERN)
        var sdCardPath = directories.firstOrNull { fullSDPattern.matcher(it).matches() }
            ?: directories.firstOrNull { !physicalPaths.contains(it.lowercase()) } ?: ""

        if (sdCardPath.trimEnd('/').isEmpty()) {
            val file = File("/storage/sdcard1")
            if (file.exists()) {
                return file.absolutePath
            }
            sdCardPath = directories.firstOrNull() ?: ""
        }

        if (sdCardPath.isEmpty()) {
            val sdPattern = Pattern.compile(SD_OTG_PATTERN)
            try {
                File("/storage").listFiles()?.forEach {
                    if (sdPattern.matcher(it.name).matches()) {
                        sdCardPath = "/storage/${it.name}"
                    }
                }
            } catch (_: Exception) {
            }
        }

        val finalPath = sdCardPath.trimEnd('/')
        sharedPreferenceRepo.sdCardPath = finalPath
        return finalPath
    }

}

/* override fun fetchAllFolders(
       videosOnly: Boolean,
       audiosOnly: Boolean
   ): Flow<Resource<List<MediaDirectory>>> {
       return flow {
           emit(Resource.IsLoading())
           val directories = playerRoomDatabase.directoriesDao.getAllDirectories()
           val includedPaths = sharedPreferenceRepo.includedFolders
           val excludedPaths = sharedPreferenceRepo.excludedFolders
           val noMediaFolderFiles = HashMap<String, Boolean>()
           val filterMedia = TYPE_VIDEOS
           val noMediaFolders = getNoMediaFolder()
           noMediaFolders.forEach { folder ->
               noMediaFolderFiles["$folder/$NOMEDIA"] = true
           }
           val filteredDirectories = directories.filter {
               it.path.shouldFolderBeVisibleForVideos(
                   excludedPaths, includedPaths, true, noMediaFolderFiles
               ) { path, hasNoMedia ->
                   noMediaFolderFiles[path] = hasNoMedia
               }
           }.filter { it.mediaTypes == filterMedia } as ArrayList<MediaDirectoryEntity>
           if (filteredDirectories.isNotEmpty()) {
               emit(Resource.DataList(filteredDirectories.map { it.toMediaDirectory() }
                   .distinctBy { it.path }.sortedBy { it.name }))
           }
           removeInvalidFolders(filteredDirectories)
           val finalDataList = gotDirectories(filteredDirectories).map { it.toMediaDirectory() }
               .distinctBy { it.path }
           if (finalDataList.isEmpty()) {
               emit(Resource.DataEmpty(context.getString(R.string.video_are_not_found)))
               return@flow
           }
           emit(Resource.DataList(finalDataList.sortedBy {
               it.name
           }))
       }
   }*/