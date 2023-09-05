package com.media.gallery.presentation.all_albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.sealedCls.PageItemViewType
import com.media.gallery.domain.util.components.LoadImageBitmap
import com.media.gallery.domain.util.components.LoadImageThumb
import com.media.gallery.presentation.all_albums.models.AllAlbumsState
import com.media.gallery.ui.theme.promptFamily

@Composable
fun AllAlbumsScreen(state: AllAlbumsState) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Fixed(state.albumsPageViewType.count), content = {
            items(state.albums) { album ->
                when (state.albumsPageViewType) {
                    is PageItemViewType.GridListView,
                    PageItemViewType.LargeListView -> {
                        Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)) {
                            Box(
                                modifier = Modifier
                                    .padding(0.5.dp)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                if (album.type == AppConstants.TYPE_VIDEOS) {
                                    LoadImageThumb(
                                        url = album.path,
                                        modifier = Modifier.matchParentSize()
                                    )
                                } else {
                                    LoadImageBitmap(
                                        url = album.path,
                                        modifier = Modifier.matchParentSize()
                                    )
                                }

                            }
                            Text(
                                text = album.folderName,
                                maxLines = 2, style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = promptFamily,
                                ),
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                    }

                    PageItemViewType.MediumListView -> {

                    }

                    PageItemViewType.SmallListView -> {

                    }
                }
            }
        })

    }
}