package com.media.gallery.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.media.gallery.presentation.details.components.FilesDetailUi
import com.media.gallery.presentation.details.models.GalleryItemDetailsState
import com.media.gallery.ui.theme.promptFamily

@Composable
fun FileDetailsScreen(
    state: GalleryItemDetailsState,
    navigateUp: () -> Unit,
    playVideo: (String) -> Unit
) {

    Scaffold(
//        contentWindowInsets = WindowInsets(left = 0.dp, right = 0.dp, bottom = 0.dp, top = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (state.galleryMediaItem != null) {
                FilesDetailUi(
                    galleryMediaItem = state.galleryMediaItem,
                    close = navigateUp,
                    onPlay = { playVideo(state.galleryMediaItem.path) })
            } else {
                if (state.msg != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Movie,
                            contentDescription = state.msg,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = state.msg,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = promptFamily,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

            }
        }
    }

}