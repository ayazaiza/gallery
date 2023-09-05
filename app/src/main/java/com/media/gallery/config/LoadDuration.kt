package com.media.gallery.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.media.gallery.R
import com.media.gallery.domain.extensions.getDuration
import com.media.gallery.domain.extensions.toFirstCap
import com.media.gallery.domain.models.GalleryMediaItem

@Composable
fun loadDuration(galleryMediaItem: GalleryMediaItem): State<String> {
    val context = LocalContext.current
    val widthText = stringResource(id = R.string.width).toFirstCap()
    val heightText = stringResource(id = R.string.height).toFirstCap()
    val duration = remember {
        mutableStateOf("00:00")
    }
    val heightNdWidth = remember {
        mutableStateOf("$widthText:${galleryMediaItem.width}, $heightText:${galleryMediaItem.height}")
    }
    return if (galleryMediaItem.type == AppConstants.TYPE_VIDEOS) context.getDuration(
        galleryMediaItem
    ).collectAsState(initial = duration.value) else heightNdWidth

}