package com.media.gallery.presentation.videos

import androidx.compose.runtime.Composable
import com.media.gallery.presentation.photos.GalleryItemsScreen
import com.media.gallery.presentation.photos.models.GalleryItemsState
import com.media.gallery.presentation.videos.models.VideosState

@Composable
fun VideosScreen(
    state: VideosState
) {
    GalleryItemsScreen(
        state = GalleryItemsState(
            mediaItems = state.videos,
            emptyMsg = state.emptyMsg,
            sortOptions = state.videosSortList,
            viewTypeOptions = state.videosViewType,
            pageViewType = state.videosPageViewType
        )
    )
}