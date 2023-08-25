package com.media.gallery.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.media.gallery.domain.extensions.getDuration
import com.media.gallery.domain.models.GalleryMediaItem

@Composable
fun loadDuration(galleryMediaItem: GalleryMediaItem): State<String> {
    val context = LocalContext.current
    val duration by remember {
        mutableStateOf("00:00")
    }
    return context.getDuration(galleryMediaItem).collectAsState(initial = duration)
}