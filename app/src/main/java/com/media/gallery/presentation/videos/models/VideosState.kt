package com.media.gallery.presentation.videos.models

import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.models.OptionsMenu
import com.media.gallery.domain.sealedCls.PageItemViewType

data class VideosState(
    val videos: List<GalleryMediaItem> = emptyList(),
    val emptyMsg: String? = null,
    val videosSortList: List<OptionsMenu> = emptyList(),
    val videosViewType: List<OptionsMenu> = emptyList(),
    val videosPageViewType: PageItemViewType = PageItemViewType.GridListView(4)
)
