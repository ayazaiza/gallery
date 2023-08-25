package com.media.gallery.config

import android.os.Build
import android.os.Environment
import android.os.Looper
import androidx.annotation.ChecksSdkIntAtLeast
import java.io.File
import java.util.concurrent.CancellationException

object AppConstants {
    const val ONE_DAY = "blockAdsForaDay"
    const val ANDROID_DATA_DIR = "/Android/data/"
    const val ANDROID_OBB_DIR = "/Android/obb/"
    val DIRS_ACCESSIBLE_ONLY_WITH_SAF = listOf(ANDROID_DATA_DIR, ANDROID_OBB_DIR)
    const val LOCATION_INTERNAL = 1
    const val LOCATION_SD = 2
    const val LOCATION_OTG = 3
    const val PATH_PARAM = "path"
    const val LINK_PARAM = "link"
    const val ID_PARAM = "id"
    const val FOLDER_NAME_PARAM = "folder_name"
    const val COUNT_PARAM = "video_count"
    const val EVER_SHOWN_FOLDERS = "ever_shown_folders"
    const val INCLUDED_FOLDERS = "included_folders"
    const val EXCLUDED_FOLDERS = "excluded_folders"
    const val APP_INITIAL_ROUTE = "app_initial"
    const val APP_PERMISSIONS_ROUTE = "app_permissions"
    const val ORIENTATION = "newScreenOrientation"
    const val BRIGHTNESS = "brightness"
    const val PREVIOUS = "previous-video"
    const val RESUME = "resume"
    const val AUTOPLAY = "ap"

    const val APP_FOLDERS_NAME = "app_folders"
    const val APP_FOLDERS_ROUTE = APP_FOLDERS_NAME

    const val APP_ALL_FILES_NAME = "app_all_files"
//    const val APP_ALL_FILES_ROUTE = APP_ALL_FILES_NAME

    const val APP_ALL_VIDEOS_NAME = "app_all_videos"
    const val APP_ALL_VIDEOS_ROUTE =
        "$APP_ALL_VIDEOS_NAME?$PATH_PARAM={$PATH_PARAM}&$COUNT_PARAM={$COUNT_PARAM}&$FOLDER_NAME_PARAM={$FOLDER_NAME_PARAM}"

    const val APP_ALL_PHOTOS_NAME = "app_all_videos"
    const val APP_ALL_PHOTOS_ROUTE =
        "$APP_ALL_PHOTOS_NAME?$PATH_PARAM={$PATH_PARAM}&$COUNT_PARAM={$COUNT_PARAM}&$FOLDER_NAME_PARAM={$FOLDER_NAME_PARAM}"


    const val APP_VIDEO_DETAILS_NAME = "app_video_details"
    const val APP_VIDEO_DETAILS_ROUTE = "$APP_VIDEO_DETAILS_NAME?$ID_PARAM={$ID_PARAM}"

    const val APP_SETTINGS_ROUTE = "app_settings"

    const val APP_FILE_MANAGER_NAME = "app_file_manager"
    const val APP_FILE_MANAGER_ROUTE = "$APP_FILE_MANAGER_NAME?$PATH_PARAM={${PATH_PARAM}}"

    const val APP_SEARCH_ROUTE = "app_search"
    const val APP_SEARCH_NAME = "app_search"

    const val APP_PRIVACY_POLICY_ROUTE = "app_privacy_policy"
    const val APP_DISCLAIMER_ROUTE = "app_disclaimer"
    const val APP_BLOCK_ADS_ROUTE = "app_block_ads"


    /* SharedPreferences */
    const val SHARED_PREFS_KEY = "video_player_app_prefs"
    const val THEME_KEY = "app_theme"
    const val DYNAMIC_THEME_KEY = "app_dynamic_theme"
    const val THEME_FOLLOW_SYSTEM = 0
    const val THEME_LIGHT = 1
    const val THEME_DARK = 2

    const val FOLDER_VIEW_TYPE_KEY = "folder_view_type"
    const val VIDEOS_VIEW_TYPE_KEY = "videos_view_type"
    const val MEDIUM_LIST_VIEW = 1
    const val SMALL_LIST_VIEW = 2
    const val LARGE_LIST_VIEW = 4
    const val GRID_VIEW = 3


    const val FOLDER_SORT_TYPE_KEY = "folder_sort_type"
    const val VIDEOS_SORT_TYPE_KEY = "videos_sort_type"
    const val SORT_TYPE_BY_NAME_A_TO_Z = 0
    const val SORT_TYPE_BY_NAME_Z_TO_A = 1
    const val SORT_TYPE_BY_LAST_MODIFIED = 2
    const val SORT_TYPE_BY_SIZE = 3
    const val PLAY = 1
    const val DELETE = 2
    const val VIDEO_DETAILS = 3


    const val TYPE_IMAGES = 1
    const val TYPE_VIDEOS = 2
    const val TYPE_GIFS = 4
    const val TYPE_RAW = 8
    const val TYPE_SVG = 16
    const val TYPE_PORTRAITS = 32
    const val TYPE_AUDIOS = 64
    const val RECYCLE_BIN = "recycle_bin"
    const val NOMEDIA = ".nomedia"
    const val SD_TREE_URI = "tree_uri_2"
    const val PRIMARY_ANDROID_DATA_TREE_URI = "primary_android_data_tree_uri_2"
    const val OTG_ANDROID_DATA_TREE_URI = "otg_android_data_tree__uri_2"
    const val SD_ANDROID_DATA_TREE_URI = "sd_android_data_tree_uri_2"
    const val PRIMARY_ANDROID_OBB_TREE_URI = "primary_android_obb_tree_uri_2"
    const val OTG_ANDROID_OBB_TREE_URI = "otg_android_obb_tree_uri_2"
    const val SD_ANDROID_OBB_TREE_URI = "sd_android_obb_tree_uri_2"
    const val OTG_TREE_URI = "otg_tree_uri_2"
    const val SD_CARD_PATH = "sd_card_path_2"
    const val OTG_REAL_PATH = "otg_real_path_2"
    const val INTERNAL_STORAGE_PATH = "internal_storage_path"
    const val OTG_PARTITION = "otg_partition_2"

    const val FILTER_MEDIA = "filter_media"

    fun getDefaultFileFilter() = TYPE_VIDEOS or TYPE_IMAGES or TYPE_GIFS or TYPE_SVG

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    fun isRPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R


    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    fun isSPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    const val SD_OTG_PATTERN = "^/storage/[A-Za-z0-9]{4}-[A-Za-z0-9]{4}$"
    const val SD_OTG_SHORT = "^[A-Za-z0-9]{4}-[A-Za-z0-9]{4}$"

    fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()


    val photoExtensions: Array<String>
        get() = arrayOf(
            ".jpg", ".png", ".jpeg", ".bmp", ".webp", ".heic", ".heif", ".apng", ".avif"
        )
    val videoExtensions: Array<String>
        get() = arrayOf(
            ".mp4", ".mkv", ".webm", ".avi", ".3gp", ".mov", ".m4v", ".3gpp"
        )
    val audioExtensions: Array<String>
        get() = arrayOf(
            ".mp3", ".wav", ".wma", ".ogg", ".m4a", ".opus", ".flac", ".aac"
        )
    val rawExtensions: Array<String>
        get() = arrayOf(
            ".dng", ".orf", ".nef", ".arw", ".rw2", ".cr2", ".cr3"
        )
    val extensionsSupportingEXIF: Array<String>
        get() = arrayOf(
            ".jpg", ".jpeg", ".png", ".webp", ".dng"
        )

    val physicalPaths = arrayListOf(
        "/storage/sdcard1", // Motorola Xoom
        "/storage/extsdcard", // Samsung SGS3
        "/storage/sdcard0/external_sdcard", // User request
        "/mnt/extsdcard", "/mnt/sdcard/external_sd", // Samsung galaxy family
        "/mnt/external_sd", "/mnt/media_rw/sdcard1", // 4.4.2 on CyanogenMod S3
        "/removable/microsd", // Asus transformer prime
        "/mnt/emmc", "/storage/external_SD", // LG
        "/storage/ext_sd", // HTC One Max
        "/storage/removable/sdcard1", // Sony Xperia Z1
        "/data/sdext", "/data/sdext2", "/data/sdext3", "/data/sdext4", "/sdcard1", // Sony Xperia Z
        "/sdcard2", // HTC One M8s
        "/storage/usbdisk0", "/storage/usbdisk1", "/storage/usbdisk2"
    )


    fun buildLink(route: String, vararg routes: String): String {
        val builder = buildString {
            append(route)
            if (routes.isNotEmpty()) {
                append("?")
                routes.forEach { param ->
                    val tmp = this.toString()
                    append(param)
                    if (tmp.endsWith("?") || tmp.endsWith("&")) {
                        append("=")
                        return@forEach
                    }
                    append("&")
                }
            }
        }
        return builder.removeSuffix("&").trim()
    }

    inline fun ignoredTryCatch(callback: () -> Unit) {
        try {
            callback.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) {
                throw e
            }
        }
    }

    fun getInternalStorageLocation() =
        if (File("/storage/emulated/0").exists()) "/storage/emulated/0" else Environment.getExternalStorageDirectory().absolutePath.trimEnd(
            '/'
        )


}