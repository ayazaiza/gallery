package com.media.gallery.presentation.navigation

import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.presentation.MainActivity

sealed interface HomeViewEvent {
    data class PlayVideos(val list: List<GalleryMediaItem>, val path: String) : HomeViewEvent
    data class PlayLink(val path: String) : HomeViewEvent
    data object DestroyAds : HomeViewEvent

    data class ShowAdsFromCompose(val showNext: () -> Unit) : HomeViewEvent
    data class ShowAds(
        val activity: MainActivity,
        val loadNextAds: Boolean,
        val showNext: () -> Unit
    ) : HomeViewEvent

    data class ShowRewardedAds(
        val activity: MainActivity,
        val showNext: () -> Unit
    ) : HomeViewEvent


    data class ShowAdsSlow(
        val activity: MainActivity,
        val loadNextAds: Boolean,
        val showNext: () -> Unit
    ) : HomeViewEvent

    data class DynamicTheme(val enable: Boolean) : HomeViewEvent
    data class ChangeTheme(val theme: Int) : HomeViewEvent
    data class Resume(val resume: Boolean) : HomeViewEvent
    data class AutoPlay(val autoPlay: Boolean) : HomeViewEvent
    data class ShowNdHideTheme(val themes: Boolean) : HomeViewEvent
    data object ShareApp : HomeViewEvent
    data object ShowRewardAd : HomeViewEvent
    data object RewardedAdCancel : HomeViewEvent
}