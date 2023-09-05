package com.media.gallery.presentation.home.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.media.gallery.config.AppConstants
import com.media.gallery.config.loadDuration
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.util.components.CustomIconWithMenu
import com.media.gallery.domain.util.components.LoadImageBitmap
import com.media.gallery.domain.util.components.LoadImageThumb
import com.media.gallery.ui.theme.promptFamily

@Composable
fun MediumItemView(
    modifier: Modifier = Modifier,
    galleryMediaItem: GalleryMediaItem,
    onEvent: (GalleryMediaItem) -> Unit = {}
) {
    val duration by loadDuration(galleryMediaItem = galleryMediaItem)

    ConstraintLayout(modifier = modifier) {
        val (cardView, titleView, moreBtn, durationView) = createRefs()
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
                bottom.linkTo(durationView.top)
                end.linkTo(moreBtn.start, 6.dp)
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
//                .clip(RoundedCornerShape(4.dp))
//                .background(MaterialTheme.colorScheme.primary)
//                .padding(horizontal = 0.dp, vertical = 0.dp)
            .constrainAs(durationView) {
                start.linkTo(titleView.start)
                top.linkTo(titleView.bottom)
                bottom.linkTo(cardView.bottom)
            }) {

            Text(
                text = duration, style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = promptFamily,
//                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
        CustomIconWithMenu(modifier = Modifier.constrainAs(moreBtn) {
            start.linkTo(titleView.end)
            top.linkTo(parent.top)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }, items = emptyList(), imageVector = Icons.Rounded.MoreVert, onTap = {
//                onEvent(VideoScreenEvent.SelectItemMenu(videoCard = videoCard, id = it.id))
        })

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