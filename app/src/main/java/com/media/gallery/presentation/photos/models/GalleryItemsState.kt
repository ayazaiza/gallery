package com.media.gallery.presentation.photos.models

import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.models.OptionsMenu
import com.media.gallery.domain.sealedCls.PageItemViewType

data class GalleryItemsState(
    val mediaItems: List<GalleryMediaItem> = emptyList(),
    val emptyMsg: String? = null,
    val sortOptions: List<OptionsMenu> = emptyList(),
    val viewTypeOptions: List<OptionsMenu> = emptyList(),
    val pageViewType: PageItemViewType = PageItemViewType.GridListView()
)
