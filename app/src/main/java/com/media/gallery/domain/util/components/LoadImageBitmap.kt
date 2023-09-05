package com.media.gallery.domain.util.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun LoadImageBitmap(
    url: String,
    modifier: Modifier = Modifier
) {

    val painter = rememberAsyncImagePainter(
        model = File(url),
        contentScale = ContentScale.Crop
    )
    Box(modifier = modifier) {
        Icon(
            imageVector = Icons.Outlined.Image,
            contentDescription = url,
            modifier = Modifier.align(Alignment.Center),
            tint =  MaterialTheme.colorScheme.primary
        )
        Image(
            painter = painter,
            contentDescription = url,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        /* AsyncImage(
             model =  File(url),
             contentDescription = url,
             contentScale = ContentScale.Crop,
             imageLoader = ImageLoader.Builder(context)
                 .components {
                     add(VideoFrameDecoder.Factory())
                 }.build()
         )*/

        /*   AsyncImage(
               model = ImageRequest.Builder(LocalContext.current)
                   .data(File(url))
                   .crossfade(1000)
                   .crossfade(true)
                   .build(),
               contentDescription = url,
               contentScale =
               ContentScale.Crop
           )*/
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.1f))
        )
    }

}

