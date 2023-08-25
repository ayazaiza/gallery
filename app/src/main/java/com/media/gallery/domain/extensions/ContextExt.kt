package com.media.gallery.domain.extensions

import android.app.RecoverableSecurityException
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.TextUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.media.gallery.R
import com.media.gallery.config.AppConstants.ignoredTryCatch
import com.media.gallery.domain.models.GalleryMediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.util.Collections
import java.util.regex.Pattern
import kotlin.math.roundToLong


/*fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}*/
fun Context.loadImageCrop(imageView: ImageView?, url: Uri?) {
    if (imageView != null && url != null) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(imageView)
    }
}

fun Context.openAd() {
    val uri = Uri.parse(getString(R.string.music_player_link))
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                uri
            )
        )
    } catch (e: ActivityNotFoundException) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    uri
                )
            )
        } catch (e2: Exception) {
            showToast(e2.message)
        }
    }
}

fun Context.openSubtitleSetting() {
    startActivity(Intent(Settings.ACTION_CAPTIONING_SETTINGS))
}

fun Context.privacy() {
    val url = resources.getString(R.string.privacy_policy_url)
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(Intent.createChooser(intent, getString(R.string.open_with).toFirstCap()))
}

fun Context.shareApp() {
    val intentInvite = Intent(Intent.ACTION_SEND)
    intentInvite.type = "text/plain"
    val body =
        "Let me recommend you this application https://play.google.com/store/apps/details?id=${packageName}"
    val subject = getString(R.string.app_name)
    intentInvite.putExtra(Intent.EXTRA_SUBJECT, subject)
    intentInvite.putExtra(Intent.EXTRA_TEXT, body)
    try {
        startActivity(Intent.createChooser(intentInvite, "Share using"))
    } catch (e: Exception) {
        showToast(getString(R.string.cant_share))
    }
}

fun Context.hasPermissions(permissions: Array<String>): Boolean {
    return permissions.all { perm ->
        ContextCompat.checkSelfPermission(
            this,
            perm
        ) == PackageManager.PERMISSION_GRANTED
    }
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Float.pxToDp(): Float {
    return this / Resources.getSystem().displayMetrics.density
}

/*fun Context.showSnackBar(view: View, text: String?, action: String?) {
    val snackBar = Snackbar.make(view, text.toString(), Snackbar.LENGTH_SHORT)
    if (action != null) {
        snackBar.setAction(R.string.error_details) {
            AlertDialog.Builder(this)
                .setMessage(action)
                .setPositiveButton(getString(com.google.android.gms.cast.framework.R.string.cast_tracks_chooser_dialog_ok)) { dialogInterface, _ ->
                    run {
                        dialogInterface.dismiss()
                    }
                }
                .setNegativeButton(getString(com.google.android.gms.cast.framework.R.string.cast_tracks_chooser_dialog_cancel)) { dialogInterface, _ ->
                    run {
                        dialogInterface.dismiss()
                        val act = this as Activity
                        act.finish()
                    }
                }.create().show()
        }
    }
    snackBar.anchorView = view
    snackBar.show()
}*/

fun Context.toast(msg: String?) {
    Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}

fun Context.getSharedPrefs(key: String): SharedPreferences {
    return getSharedPreferences(key, Context.MODE_PRIVATE)
}

fun Context.getVideoName(uri: Uri?): String {
    if (uri == null) return "Title"
    var result: String? = null
    try {
        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
            contentResolver.query(
                uri, projection, null, null, null
            ).use {
                if (it == null) return@use
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index > -1) {
                        result = it.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result ?: "Title"
}

private fun getFileUri(path: String): Uri = when {
    path.isImageSlow() -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    path.isVideoSlow() -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    path.isAudioSlow() -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    else -> MediaStore.Files.getContentUri("external")
}

fun Context.getDuration(path: String): Long? {
    val projection = arrayOf(
        MediaStore.Video.Media.DURATION
    )

    val uri = getFileUri(path)
    val selection =
        if (path.startsWith("content://")) "${BaseColumns._ID} = ?" else "${MediaStore.MediaColumns.DATA} = ?"
    val selectionArgs =
        if (path.startsWith("content://")) arrayOf(path.substringAfterLast("/")) else arrayOf(path)

    ignoredTryCatch {
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                return (cursor.getIntValue(MediaStore.Video.Media.DURATION) / 1000.toDouble()).roundToLong()
            }
        }
    }

    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
            .toInt() / 1000f).roundToLong()
    } catch (ignored: Exception) {
        null
    }
}

fun Context.getStorageDirectories(): Array<String> {
    val paths = HashSet<String>()
    val rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET")
    val rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE")
    if (rawEmulatedStorageTarget.isNullOrEmpty()) {
        getExternalFilesDirs(null).filterNotNull().map { it.absolutePath }
            .mapTo(paths) { it.substring(0, it.indexOf("Android/data")) }
    } else {
        val path = Environment.getExternalStorageDirectory().absolutePath
        val folders = Pattern.compile("/").split(path)
        val lastFolder = folders[folders.size - 1]
        var isDigit = false
        try {
            Integer.valueOf(lastFolder)
            isDigit = true
        } catch (ignored: NumberFormatException) {
        }
        val rawUserId = if (isDigit) lastFolder else ""
        if (TextUtils.isEmpty(rawUserId)) {
            paths.add(rawEmulatedStorageTarget)
        } else {
            paths.add(rawEmulatedStorageTarget + File.separator + rawUserId)
        }
    }
    if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
        val rawSecondaryStorages = rawSecondaryStoragesStr!!.split(File.pathSeparator.toRegex())
            .dropLastWhile(String::isEmpty).toTypedArray()
        Collections.addAll(paths, *rawSecondaryStorages)
    }
    return paths.map { it.trimEnd('/') }.toTypedArray()
}

fun Context.showToast(msg: String?) {
    Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}


fun Context.deleteMedia(uri: String): Any? {
    val uriPath = Uri.parse(uri)
    if (uriPath.scheme != null) {
        try {
            val deleted = contentResolver.delete(uriPath, null, null)
            if (deleted > 0) {
                return 1
            }
            return null
        } catch (e: SecurityException) {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    MediaStore.createDeleteRequest(
                        contentResolver, listOf(uriPath)
                    ).intentSender
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    try {
                        (e as RecoverableSecurityException).userAction.actionIntent.intentSender
                    } catch (e: ClassCastException) {
                        null
                    }
                }

                else -> {
                    null
                }
            }
        }
    } else {
        val file = File(uri)
        if (!file.exists()) {
            return 1
        }
        if (!file.delete()) {
            return null
        }
        return 1
    }
}

fun Context.getDuration(galleryMediaItem: GalleryMediaItem): Flow<String> {
    return flow {
        val duration = if (galleryMediaItem.duration != 0L) {
            (galleryMediaItem.duration * 1000).toString().stringForTime()
        } else {
            val item = extractVideoLocationInfo(galleryMediaItem)
            item.duration.toString().stringForTime()
        }
        emit(duration)
    }.flowOn(Dispatchers.IO)
}

fun Context.extractVideoLocationInfo(galleryMediaItem: GalleryMediaItem): GalleryMediaItem {
    return try {
        val theUri = if (galleryMediaItem.uri.isBlank()) {
            Uri.fromFile(File(galleryMediaItem.path))
        } else {
            Uri.parse(galleryMediaItem.uri)
        }
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(this, theUri)
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
        val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val mimetype = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
        galleryMediaItem.copy(
            title = if (!TextUtils.isEmpty(title)) title.toString() else galleryMediaItem.title,
            mimeType = if (!TextUtils.isEmpty(mimetype)) mimetype!! else galleryMediaItem.mimeType,
            width = if (!TextUtils.isEmpty(width)) width?.toInt() ?: 0 else galleryMediaItem.width,
            height = if (!TextUtils.isEmpty(height)) height?.toInt() ?: 0 else galleryMediaItem.height,
            duration = if (!TextUtils.isEmpty(duration)) duration?.toLong()
                ?: 0L else galleryMediaItem.duration
        )
    } catch (e: RuntimeException) {
        e.printStackTrace()
        galleryMediaItem
    }
}
//fun Context.createSettingsDialog() {
//    val alert = MaterialAlertDialogBuilder(this)
//    alert.setTitle(getText(R.string.attention_required))
//    alert.setMessage(getText(R.string.base_text))
//    alert.setNegativeButton(getText(R.string.cancel)) { _, _ -> }
//    alert.setPositiveButton(getText(R.string.settings)) { _, _ ->
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        val uri = Uri.fromParts("package", activity.packageName, null)
//        intent.data = uri
//        activity.startActivity(intent)
//    }
//    alert.show()
//}