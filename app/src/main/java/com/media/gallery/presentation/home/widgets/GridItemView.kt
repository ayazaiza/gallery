package com.media.gallery.presentation.home.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.util.components.LoadImageBitmap
import com.media.gallery.domain.util.components.LoadImageThumb

@Composable
fun GridItemView(
    modifier: Modifier = Modifier,
    galleryMediaItem: GalleryMediaItem
) {

    Box(
        modifier = modifier
            .padding(0.5.dp)
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (galleryMediaItem.type == AppConstants.TYPE_VIDEOS) {
            LoadImageThumb(
                url = galleryMediaItem.path,
                modifier = Modifier.matchParentSize(),
                imageVector = Icons.Outlined.PlayCircle
            )
            Icon(
                imageVector = Icons.Outlined.PlayCircle,
                contentDescription = galleryMediaItem.path,
                modifier = Modifier.align(Alignment.Center),
                tint = Color.White
            )
        } else {
            LoadImageBitmap(
                url = galleryMediaItem.path,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}