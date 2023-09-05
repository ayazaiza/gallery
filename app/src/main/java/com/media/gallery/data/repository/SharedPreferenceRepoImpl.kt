package com.media.gallery.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Environment
import com.media.gallery.R
import com.media.gallery.config.AppConstants
import com.media.gallery.config.AppConstants.EVER_SHOWN_FOLDERS
import com.media.gallery.config.AppConstants.EXCLUDED_FOLDERS
import com.media.gallery.config.AppConstants.FILTER_MEDIA
import com.media.gallery.config.AppConstants.INCLUDED_FOLDERS
import com.media.gallery.config.AppConstants.INTERNAL_STORAGE_PATH
import com.media.gallery.config.AppConstants.OTG_ANDROID_DATA_TREE_URI
import com.media.gallery.config.AppConstants.OTG_ANDROID_OBB_TREE_URI
import com.media.gallery.config.AppConstants.OTG_PARTITION
import com.media.gallery.config.AppConstants.OTG_REAL_PATH
import com.media.gallery.config.AppConstants.OTG_TREE_URI
import com.media.gallery.config.AppConstants.PRIMARY_ANDROID_DATA_TREE_URI
import com.media.gallery.config.AppConstants.PRIMARY_ANDROID_OBB_TREE_URI
import com.media.gallery.config.AppConstants.SD_ANDROID_DATA_TREE_URI
import com.media.gallery.config.AppConstants.SD_ANDROID_OBB_TREE_URI
import com.media.gallery.config.AppConstants.SD_CARD_PATH
import com.media.gallery.config.AppConstants.SD_TREE_URI
import com.media.gallery.config.AppConstants.getDefaultFileFilter
import com.media.gallery.config.AppConstants.ignoredTryCatch
import com.media.gallery.domain.repository.SharedPreferenceRepo
import java.util.Calendar
import java.util.TimeZone

class SharedPreferenceRepoImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) :
    SharedPreferenceRepo {

    override var adsTimes: Long
        get() = sharedPreferences.getLong(AppConstants.ONE_DAY, 0)
        set(oneDay) = putLongValue(AppConstants.ONE_DAY, oneDay)


    private fun showAdsNextDay(): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.add(Calendar.SECOND, context.resources.getInteger(R.integer.ads_free_time))
        return calendar.time.time
    }

    private fun nowTime(): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        return calendar.time.time
    }

    override fun isOneDayPremium(): Boolean {
        if (adsTimes <= 0) {
            return false
        }
        if (nowTime() >= adsTimes) {
            adsTimes = 0L
            return false
        }
        return true
    }

    override fun setOneDayPremium() {
        adsTimes = showAdsNextDay()
    }

    override var dynamicTheme: Boolean
        get() = sharedPreferences.getBoolean(AppConstants.DYNAMIC_THEME_KEY, false)
        set(value) {
            putBooleanValue(AppConstants.DYNAMIC_THEME_KEY, value)
        }

    override var filterMedia: Int
        get() = sharedPreferences.getInt(FILTER_MEDIA, getDefaultFileFilter())
        set(filterMedia) = sharedPreferences.edit().putInt(FILTER_MEDIA, filterMedia).apply()


    override var theme: Int
        get() = sharedPreferences.getInt(AppConstants.THEME_KEY, AppConstants.THEME_LIGHT)
        set(value) {
            putIntValue(AppConstants.THEME_KEY, value)
        }


    override var folderViewType: Int
        get() = sharedPreferences.getInt(
            AppConstants.FOLDER_VIEW_TYPE_KEY,
            AppConstants.GRID_VIEW
        )
        set(value) {
            putIntValue(AppConstants.FOLDER_VIEW_TYPE_KEY, value)
        }

    override var folderSortType: Int
        get() = sharedPreferences.getInt(
            AppConstants.FOLDER_SORT_TYPE_KEY, AppConstants.SORT_TYPE_BY_LAST_MODIFIED
        )
        set(value) {
            putIntValue(AppConstants.FOLDER_SORT_TYPE_KEY, value)
        }
    override var videosViewType: Int
        get() = sharedPreferences.getInt(
            AppConstants.VIDEOS_VIEW_TYPE_KEY,
            AppConstants.MEDIUM_LIST_VIEW
        )
        set(value) {
            putIntValue(AppConstants.VIDEOS_VIEW_TYPE_KEY, value)
        }

    override var videosSortType: Int
        get() = sharedPreferences.getInt(
            AppConstants.VIDEOS_SORT_TYPE_KEY,
            AppConstants.SORT_TYPE_BY_LAST_MODIFIED
        )
        set(value) {
            putIntValue(AppConstants.VIDEOS_SORT_TYPE_KEY, value)
        }
    override var photosSortType: Int
        get() = sharedPreferences.getInt(
            AppConstants.PHOTOS_SORT_TYPE_KEY,
            AppConstants.SORT_TYPE_BY_LAST_MODIFIED
        )
        set(value) {
            putIntValue(AppConstants.PHOTOS_SORT_TYPE_KEY, value)
        }

    override var photosViewType: Int
        get() = sharedPreferences.getInt(
            AppConstants.PHOTOS_VIEW_TYPE_KEY,
            AppConstants.GRID_VIEW
        )
        set(value) {
            putIntValue(AppConstants.PHOTOS_VIEW_TYPE_KEY, value)
        }
    override var internalStoragePath: String
        get() = sharedPreferences.getString(INTERNAL_STORAGE_PATH, "")!!
        set(internalStoragePath) = putStringValue(
            INTERNAL_STORAGE_PATH,
            internalStoragePath
        )
    override var everShownFolders: Set<String>
        get() = sharedPreferences.getStringSet(EVER_SHOWN_FOLDERS, getEverShownFolders())!!
        set(everShownFolders) = putStringSets(EVER_SHOWN_FOLDERS, everShownFolders)

    override var oTGPath: String
        get() = sharedPreferences.getString(OTG_REAL_PATH, "")!!
        set(value) = putStringValue(OTG_REAL_PATH, value)

    override var sdCardPath: String
        get() = sharedPreferences.getString(SD_CARD_PATH, "")!!
        set(sdCardPath) = putStringValue(SD_CARD_PATH, sdCardPath)

    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun setPreviousVideo(videoPath: String) {
        putStringValue(AppConstants.PREVIOUS, videoPath)
    }

    override fun removePreviousVideo() {
        val editor = sharedPreferences.edit()
        editor.remove(AppConstants.PREVIOUS)
        editor.apply()
    }

    override var primaryAndroidDataTreeUri: String
        get() = sharedPreferences.getString(PRIMARY_ANDROID_DATA_TREE_URI, "")!!
        set(value) = putStringValue(PRIMARY_ANDROID_DATA_TREE_URI, value)

    override var sdAndroidDataTreeUri: String
        get() = sharedPreferences.getString(SD_ANDROID_DATA_TREE_URI, "")!!
        set(value) = putStringValue(SD_ANDROID_DATA_TREE_URI, value)

    override var otgAndroidDataTreeUri: String
        get() = sharedPreferences.getString(OTG_ANDROID_DATA_TREE_URI, "")!!
        set(value) = putStringValue(OTG_ANDROID_DATA_TREE_URI, value)

    override var primaryAndroidObbTreeUri: String
        get() = sharedPreferences.getString(PRIMARY_ANDROID_OBB_TREE_URI, "")!!
        set(value) = putStringValue(PRIMARY_ANDROID_OBB_TREE_URI, value)

    override var sdAndroidObbTreeUri: String
        get() = sharedPreferences.getString(SD_ANDROID_OBB_TREE_URI, "")!!
        set(value) = putStringValue(SD_ANDROID_OBB_TREE_URI, value)

    override var otgAndroidObbTreeUri: String
        get() = sharedPreferences.getString(OTG_ANDROID_OBB_TREE_URI, "")!!
        set(value) = putStringValue(OTG_ANDROID_OBB_TREE_URI, value)

    override var sdTreeUri: String
        get() = sharedPreferences.getString(SD_TREE_URI, "")!!
        set(value) = putStringValue(SD_TREE_URI, value)

    override var oTGTreeUri: String
        get() = sharedPreferences.getString(OTG_TREE_URI, "")!!
        set(value) = putStringValue(OTG_TREE_URI, value)

    override var oTGPartition: String
        get() = sharedPreferences.getString(OTG_PARTITION, "")!!
        set(value) = putStringValue(OTG_PARTITION, value)

    fun addIncludedFolders(paths: Set<String>) {
        val currIncludedFolders = HashSet(includedFolders)
        currIncludedFolders.addAll(paths)
        includedFolders = currIncludedFolders.filter { it.isNotEmpty() }.toHashSet()
    }

    fun removeIncludedFolder(path: String) {
        val currIncludedFolders = HashSet(includedFolders)
        currIncludedFolders.remove(path)
        includedFolders = currIncludedFolders
    }

    override var includedFolders: MutableSet<String>
        get() = sharedPreferences.getStringSet(INCLUDED_FOLDERS, HashSet())!!
        set(includedFolders) = putStringSets(INCLUDED_FOLDERS, includedFolders)
    override var excludedFolders: MutableSet<String>
        get() = sharedPreferences.getStringSet(
            EXCLUDED_FOLDERS,
            HashSet()
        )!!
        set(excludedFolders) = putStringSets(EXCLUDED_FOLDERS, excludedFolders)

    // hashSetOf("/storage/emulated/0/DCIM/Camera","/storage/emulated/0/Download")
    override var resume: Boolean
        get() = sharedPreferences.getBoolean(AppConstants.RESUME, true)
        set(value) {
            putBooleanValue(AppConstants.RESUME, value)
        }
    override var autoPlay: Boolean
        get() = sharedPreferences.getBoolean(AppConstants.AUTOPLAY, false)
        set(value) {
            putBooleanValue(AppConstants.AUTOPLAY, value)
        }
    override var orientation: Int
        get() = sharedPreferences.getInt(
            AppConstants.ORIENTATION,
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        )
        set(value) {
            putIntValue(AppConstants.ORIENTATION, value)
        }
    override var brightness: Int
        get() = sharedPreferences.getInt(AppConstants.BRIGHTNESS, 50)
        set(value) {
            putIntValue(AppConstants.BRIGHTNESS, value)
        }

    override fun removeBunch(path: String) {
        ignoredTryCatch {
            val editor = sharedPreferences.edit()
            editor.remove(path)
            editor.apply()
        }
    }

    override fun setBunch(key: String, value: String) {
        putStringValue(key, value)
    }

    override fun getBunch(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun addExcludedFolder(path: String) {
        addExcludedFolders(hashSetOf(path))
    }

    private fun addExcludedFolders(paths: Set<String>) {
        val currIncludedFolders = HashSet(excludedFolders)
        currIncludedFolders.addAll(paths)
        excludedFolders = currIncludedFolders.filter { it.isNotEmpty() }.toHashSet()
    }

    private fun putIntValue(key: String, value: Int) {
        sharedPreferences.edit()
            .remove(key)
            .putInt(key, value)
            .apply()
    }

    private fun putBooleanValue(key: String, value: Boolean) {
        sharedPreferences.edit()
            .remove(key)
            .putBoolean(key, value)
            .apply()
    }

    private fun putStringValue(key: String, value: String) {
        sharedPreferences.edit()
            .remove(key)
            .putString(key, value)
            .apply()
    }

    private fun putStringValue(key: String, value: Boolean) {
        sharedPreferences.edit()
            .remove(key)
            .putBoolean(key, value)
            .apply()
    }

    private fun putLongValue(key: String, value: Long) {
        sharedPreferences.edit()
            .remove(key)
            .putLong(key, value)
            .apply()
    }

    private fun putStringSets(key: String, value: Set<String>) {
        sharedPreferences.edit()
            .remove(key)
            .putStringSet(key, value)
            .apply()
    }

    private fun getEverShownFolders() = hashSetOf(
        internalStoragePath,
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath,
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath,
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath,
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath,
//        "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath}/Screenshots",
//        "$internalStoragePath/WhatsApp/Media/WhatsApp Images",
//        "$internalStoragePath/WhatsApp/Media/WhatsApp Images/Sent",
        "$internalStoragePath/WhatsApp/Media/WhatsApp Video",
        "$internalStoragePath/WhatsApp/Media/WhatsApp Video/Sent",
        "$internalStoragePath/WhatsApp/Media/.Statuses",

//        "$internalStoragePath/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images",
//        "$internalStoragePath/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/Sent",
        "$internalStoragePath/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video",
        "$internalStoragePath/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video/Sent",
//        "$internalStoragePath/Android/media/com.whatsapp/WhatsApp/Media/.Statuses",

//        "$internalStoragePath/WhatsApp Business/Media/WhatsApp Business Images",
//        "$internalStoragePath/WhatsApp Business/Media/WhatsApp Business Images/Sent",
        "$internalStoragePath/WhatsApp Business/Media/WhatsApp Business Video",
        "$internalStoragePath/WhatsApp Business/Media/WhatsApp Business Video/Sent",
        "$internalStoragePath/WhatsApp Business/Media/.Statuses",

//        "$internalStoragePath/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Images",
//        "$internalStoragePath/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Images/Sent",
        "$internalStoragePath/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Video",
        "$internalStoragePath/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Video/Sent",
//        "$internalStoragePath/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses",
    )
}