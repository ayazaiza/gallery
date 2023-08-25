package com.media.gallery.domain.repository

import com.media.gallery.domain.models.PlayerOption

interface ViewModelStrRes {
    val sortBy: List<PlayerOption>
    val viewBy: List<PlayerOption>
    val itemMenu: List<PlayerOption>
    val settingThemes: List<PlayerOption>
    val allVideos: String
    val videosNotFound: String
    val detailsNotFound: String
    val deleted: String
    val somethingWentWrong: String
    val watchAgain: String
    val watch: String
    val failedToWatchVideo: String
    val failedToWatchMsg: String
    val pleaseWait: String
    val blockAds: String
    val successForOneDay:String
    val videoAdsIsNotAvailable:String
}