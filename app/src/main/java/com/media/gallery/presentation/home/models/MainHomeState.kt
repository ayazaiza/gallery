package com.media.gallery.presentation.home.models

import com.media.gallery.domain.models.BottomNavItem
import com.media.gallery.presentation.all_albums.models.AllAlbumsState
import com.media.gallery.presentation.photos.models.GalleryItemsState

data class MainHomeState(
    val bottomList: List<BottomNavItem> = emptyList(),
    val photosState: GalleryItemsState = GalleryItemsState(),
    val currentRoute: String? = null,
    val videosState: GalleryItemsState = GalleryItemsState(),
    val albumsState: AllAlbumsState = AllAlbumsState()
)
