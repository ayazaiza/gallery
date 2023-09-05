package com.media.gallery.presentation.all_albums.models

import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.models.OptionsMenu
import com.media.gallery.domain.sealedCls.PageItemViewType

data class AllAlbumsState(
    val albums: List<GalleryMediaItem> = emptyList(),
    val emptyMsg: String? = null,
    val albumsSortList: List<OptionsMenu> = emptyList(),
    val albumsViewType: List<OptionsMenu> = emptyList(),
    val albumsPageViewType: PageItemViewType = PageItemViewType.GridListView(3)
)
