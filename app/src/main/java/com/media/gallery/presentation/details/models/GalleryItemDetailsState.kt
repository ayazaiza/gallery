package com.media.gallery.presentation.details.models

import com.media.gallery.domain.models.GalleryMediaItem

data class GalleryItemDetailsState(
    val galleryMediaItem: GalleryMediaItem? = null,
    val msg: String? = null,
    val isLoading: Boolean = false
)
