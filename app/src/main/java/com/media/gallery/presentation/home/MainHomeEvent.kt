package com.media.gallery.presentation.home

import com.media.gallery.domain.models.OptionsMenu

sealed interface MainHomeEvent {
    data class ChangePhotoSortByOption(val optionsMenu: OptionsMenu) : MainHomeEvent
    data class ChangeAlbumsSortByOption(val optionsMenu: OptionsMenu) : MainHomeEvent
    data class ChangeVideoSortByOption(val optionsMenu: OptionsMenu) : MainHomeEvent
    data class ChangePhotosViewTypeOption(val optionsMenu: OptionsMenu) : MainHomeEvent
    data class ChangeAlbumsViewTypeOption(val optionsMenu: OptionsMenu) : MainHomeEvent
    data class ChangeVideosViewTypeOption(val optionsMenu: OptionsMenu) : MainHomeEvent
    data class ChangeCurrentRoute(val currentRoute: String) : MainHomeEvent
}