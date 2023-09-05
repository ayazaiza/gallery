package com.media.gallery.presentation.home.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.media.gallery.config.loadDuration
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.util.components.CustomIconWithMenu
import com.media.gallery.ui.theme.promptFamily

@Composable
fun SmallItemView(
    modifier: Modifier = Modifier,
    galleryMediaItem: GalleryMediaItem,
    onEvent: (GalleryMediaItem) -> Unit = {}
) {
    val duration by loadDuration(galleryMediaItem = galleryMediaItem)

    ConstraintLayout(modifier = modifier) {
        val (titleView, moreBtn) = createRefs()
        Text(
            modifier = Modifier
                .constrainAs(titleView) {
                    start.linkTo(parent.start, 6.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(moreBtn.start)
                    width = Dimension.fillToConstraints
                    height = Dimension.preferredWrapContent
                },
            text = galleryMediaItem.title,
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = promptFamily
            ), overflow = TextOverflow.Ellipsis
        )

        /* Box(
             modifier = Modifier
                 .clip(RoundedCornerShape(4.dp))
                 .background(MaterialTheme.colorScheme.primary)
                 .padding(horizontal = 6.dp, vertical = 2.dp)
                 .constrainAs(durationView) {
                     start.linkTo(titleView.start)
                     top.linkTo(titleView.bottom)
                     bottom.linkTo(parent.bottom)
                 }
         ) {
             Text(
                 text = duration,
                 style = MaterialTheme.typography.labelSmall.copy(
                     fontFamily = promptFamily,
                     color = MaterialTheme.colorScheme.onPrimary
                 )
             )
         }*/
        CustomIconWithMenu(modifier = Modifier
            .constrainAs(moreBtn) {
                start.linkTo(titleView.end)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            items = emptyList(),
            imageVector = Icons.Rounded.MoreVert,
            onTap = {
//                onEvent(VideoScreenEvent.SelectItemMenu(videoCard = videoCard, id = it.id))
            }
        )
    }
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