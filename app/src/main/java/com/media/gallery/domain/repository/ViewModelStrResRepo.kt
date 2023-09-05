package com.media.gallery.domain.repository

import com.media.gallery.domain.models.BottomNavItem
import com.media.gallery.domain.models.OptionsMenu

interface ViewModelStrResRepo {
    val sortOptions: List<OptionsMenu>
    val viewTypeOptions: List<OptionsMenu>
    val itemMenuOptions: List<OptionsMenu>
    val settingThemesOptions: List<OptionsMenu>
    val allVideos: String
    val videosNotFound: String
    val filesNotFound: String
    val photosNotFound: String
    val detailsNotFound: String
    val deleted: String
    val somethingWentWrong: String
    val watchAgain: String
    val watch: String
    val failedToWatchVideo: String
    val failedToWatchMsg: String
    val pleaseWait: String
    val blockAds: String
    val successForOneDay: String
    val videoAdsIsNotAvailable: String
    val bottomItems: List<BottomNavItem>
}