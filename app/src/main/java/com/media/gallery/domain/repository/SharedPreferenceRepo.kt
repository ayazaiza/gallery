package com.media.gallery.domain.repository

interface SharedPreferenceRepo {

    var theme: Int
    var dynamicTheme:Boolean
    var adsTimes: Long
    var filterMedia: Int
    var folderViewType: Int
    var folderSortType: Int
    var videosViewType: Int
    var videosSortType: Int
    var oTGPath: String
    var oTGPartition: String
    var oTGTreeUri: String
    var sdTreeUri: String
    var otgAndroidObbTreeUri: String
    var sdAndroidObbTreeUri: String
    var primaryAndroidObbTreeUri: String
    var otgAndroidDataTreeUri: String
    var sdAndroidDataTreeUri: String
    var primaryAndroidDataTreeUri: String
    var sdCardPath: String
    var internalStoragePath: String
    var everShownFolders: Set<String>
    var includedFolders: MutableSet<String>
    var excludedFolders: MutableSet<String>


    var resume:Boolean
    var autoPlay:Boolean
    var orientation:Int
    var brightness:Int
    fun removeBunch(path: String)
    fun setBunch(key: String, value: String)
    fun getBunch(key: String): String?
    fun addExcludedFolder(path: String)
    fun contains(key: String): Boolean
    fun setPreviousVideo(videoPath: String)
    fun removePreviousVideo()
    fun isOneDayPremium(): Boolean
    fun setOneDayPremium()
}