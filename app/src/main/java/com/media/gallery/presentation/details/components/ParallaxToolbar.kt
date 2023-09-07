package com.media.gallery.presentation.details.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.media.gallery.R
import com.media.gallery.config.AppConstants
import com.media.gallery.config.loadDuration
import com.media.gallery.domain.extensions.getFileSize
import com.media.gallery.domain.extensions.toFirstCap
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.util.components.CustomIcon
import com.media.gallery.domain.util.components.LoadImageBitmap
import com.media.gallery.domain.util.components.LoadImageThumb
import com.media.gallery.ui.theme.promptFamily

private val headerHeight = 300.dp

@Composable
fun FilesDetailUi(
    galleryMediaItem: GalleryMediaItem,
    close: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll: ScrollState = rememberScrollState(0)
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    Box(
        modifier = modifier
    ) {
        Content(galleryMediaItem, scroll)
        RegisterScreenHeader(
            scroll, headerHeightPx, close, onPlay, galleryMediaItem
        )
    }

}

@Composable
private fun RegisterScreenHeader(
    scroll: ScrollState,
    headerHeightPx: Float,
    navigateUp: () -> Unit,
    onPlay: () -> Unit,
    galleryMediaItem: GalleryMediaItem

) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(headerHeight)
        .graphicsLayer {
            translationY = -scroll.value.toFloat() / 2f // Parallax effect
            alpha = (-1f / headerHeightPx) * scroll.value + 1
        }) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.primary.copy(0.8f)
                )
        )
        Icon(
            imageVector = Icons.Outlined.Movie,
            contentDescription = "",
            tint = Color.Gray,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)

        )
        if (galleryMediaItem.type == AppConstants.TYPE_VIDEOS) {
            LoadImageThumb(
                url = galleryMediaItem.path,
                modifier = Modifier.matchParentSize(),
            )
        } else {
            LoadImageBitmap(
                url = galleryMediaItem.path,
                modifier = Modifier.matchParentSize(),
            )
        }

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Transparent),
                        startY = 1 * headerHeightPx / 4 // Gradient applied to wrap the title only
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            Pair(0.4f, Color.Transparent),
                            Pair(1f, MaterialTheme.colorScheme.background)
                        )
                    )
                )
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)

        ) {
            CustomIcon(
                title = galleryMediaItem.title,
                imageVector = Icons.Outlined.ArrowBackIos,
                onTap = navigateUp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    onPlay()
                },
                contentPadding = PaddingValues(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(),
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
            ) {
                Icon(Icons.Rounded.PlayArrow, stringResource(id = R.string.video))
            }
        }
    }
}


@Composable
fun Content(galleryMediaItem: GalleryMediaItem, scroll: ScrollState) {
    val duration by loadDuration(galleryMediaItem = galleryMediaItem)
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .verticalScroll(scroll)
            .padding(12.dp)
    ) {
        Spacer(modifier = Modifier.height(headerHeight))
        Description(
            galleryMediaItem.path, "${stringResource(id = R.string.location).toFirstCap()} :"
        )
        Description(
            text = galleryMediaItem.size.toString().getFileSize(),
            title = "${stringResource(id = R.string.size)} :"
        )
        if (galleryMediaItem.type == AppConstants.TYPE_VIDEOS) {
            Description(
                text = duration, title = "${
                    stringResource(
                        id = R.string.duration
                    ).toFirstCap()
                }: "
            )
        }
        if (galleryMediaItem.width > 0 && galleryMediaItem.height > 0) {
            Description(
                text = "${galleryMediaItem.width} X ${galleryMediaItem.height}",
                title = "${stringResource(id = R.string.dimensions).toFirstCap()} :"
            )
        }
    }
}

@Composable
fun Description(text: String, title: String) {
    val boldStyleForTitle = SpanStyle(
        fontWeight = FontWeight.Bold, fontFamily = promptFamily
    )
    val boldStyleForDesc = SpanStyle(
        fontWeight = FontWeight.Normal, fontFamily = promptFamily
    )
    Text(
        text = buildAnnotatedString {
            pushStyle(boldStyleForTitle)
            append(title)
            pop()
            pushStyle(boldStyleForDesc)
            append(text)
        },
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}


/* val context = LocalContext.current
     var duration by remember {
         mutableStateOf("00:00")
     }

     LaunchedEffect(key1 = videoCard, block = {
         context.getDuration(videoCard).collectLatest {
             duration = it
         }
     })*/