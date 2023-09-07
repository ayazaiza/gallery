package com.media.gallery.presentation.home.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.media.gallery.R
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.util.components.LoadImageBitmap
import com.media.gallery.domain.util.components.LoadImageThumb
import com.media.gallery.ui.theme.promptFamily

@Composable
fun MediumAlbumItemView(
    modifier: Modifier = Modifier,
    galleryMediaItem: GalleryMediaItem,
    onEvent: (GalleryMediaItem) -> Unit = {}
) {
    val sizeText = if (galleryMediaItem.mediaCount < 2) {
        stringResource(id = R.string.file)
    } else {
        stringResource(id = R.string.files)
    }

    ConstraintLayout(modifier = modifier) {
        val (cardView, titleView, sizeView) = createRefs()
        Box(
            modifier = Modifier
                .size(80.dp, 70.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .constrainAs(cardView) {
                    start.linkTo(parent.start, 3.dp)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                    end.linkTo(titleView.start)
                },
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
                LoadImageBitmap(url = galleryMediaItem.path, modifier = Modifier.matchParentSize())
            }
        }
        Text(
            modifier = Modifier.constrainAs(titleView) {
                start.linkTo(cardView.end, 6.dp)
                top.linkTo(cardView.top)
                bottom.linkTo(sizeView.top)
                end.linkTo(parent.end, 6.dp)
                width = Dimension.fillToConstraints
                height = Dimension.preferredWrapContent
            },
            text = galleryMediaItem.title,
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = promptFamily, fontWeight = FontWeight.SemiBold
            ),
            overflow = TextOverflow.Ellipsis
        )

        Box(modifier = Modifier
            .constrainAs(sizeView) {
                start.linkTo(titleView.start)
                top.linkTo(titleView.bottom)
                bottom.linkTo(cardView.bottom)
            }) {
            Text(
                text = "${galleryMediaItem.mediaCount} $sizeText",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = promptFamily,
                )
            )
        }
    }
}

/*   val context = LocalContext.current
   var duration by remember {
       mutableStateOf("00:00")
   }

   LaunchedEffect(key1 = videoCard, block = {
       context.getDuration(videoCard).collectLatest {
           duration = it
       }
   })*/