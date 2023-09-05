package com.media.gallery.presentation.photos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.media.gallery.domain.sealedCls.PageItemViewType
import com.media.gallery.presentation.home.widgets.GridItemView
import com.media.gallery.presentation.home.widgets.LargeItemView
import com.media.gallery.presentation.home.widgets.MediumItemView
import com.media.gallery.presentation.home.widgets.SmallItemView
import com.media.gallery.presentation.photos.models.GalleryItemsState

@Composable
fun GalleryItemsScreen(
    state: GalleryItemsState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(state.pageViewType.count),
            content = {
                items(state.mediaItems) { item ->
                    when (state.pageViewType) {
                        is PageItemViewType.GridListView -> {
                            GridItemView(galleryMediaItem = item)
                        }

                        PageItemViewType.LargeListView -> {
                            LargeItemView(
                                galleryMediaItem = item,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp)
                            )
                        }

                        PageItemViewType.MediumListView -> {
                            MediumItemView(
                                galleryMediaItem = item,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp)
                            )
                        }

                        PageItemViewType.SmallListView -> {
                            SmallItemView(
                                galleryMediaItem = item,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            },
        )
    }

}