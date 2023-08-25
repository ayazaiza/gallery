package com.media.gallery.domain.util.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.media.gallery.config.loadDuration
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.ui.theme.promptFamily

@Composable
fun CustomMediaVideoItemUI(
    modifier: Modifier = Modifier,
    galleryMediaItem: GalleryMediaItem
) {
    val duration by loadDuration(galleryMediaItem = galleryMediaItem)

    ConstraintLayout(modifier = modifier) {
        val (cardView, titleView, durationView) = createRefs()
        Box(
            modifier = Modifier
                .size(100.dp, 70.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
                .constrainAs(cardView) {
                    start.linkTo(parent.start, 3.dp)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                    end.linkTo(titleView.start)
                },
        ) {
            LoadImageThumb(url = galleryMediaItem.path, modifier = Modifier.matchParentSize())
        }
        Text(
            modifier = Modifier
                .constrainAs(titleView) {
                    start.linkTo(cardView.end, 6.dp)
                    top.linkTo(cardView.top)
                    bottom.linkTo(durationView.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.preferredWrapContent
                },
            text = galleryMediaItem.title,
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = promptFamily
            ), overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .constrainAs(durationView) {
                    start.linkTo(titleView.start)
                    top.linkTo(titleView.bottom)
                    bottom.linkTo(cardView.bottom)
                }
        ) {
            Text(
                text = duration,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = promptFamily,
                    color = MaterialTheme.colorScheme.onPrimary
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