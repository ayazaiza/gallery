package com.media.gallery.presentation.home.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun LargeItemView(
    modifier: Modifier = Modifier,
    galleryMediaItem: GalleryMediaItem,
//    state: VideosState,
    onEvent: (GalleryMediaItem) -> Unit = {}
) {

    val duration by loadDuration(galleryMediaItem = galleryMediaItem)

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 230.dp)
                .background(Color.Black)
        ) {
            if (galleryMediaItem.type == AppConstants.TYPE_VIDEOS) {
                LoadImageThumb(url = galleryMediaItem.path, modifier = Modifier.matchParentSize())
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = duration,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = promptFamily,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            } else {
                LoadImageBitmap(url = galleryMediaItem.path, modifier = Modifier.matchParentSize())
            }

        }
        ConstraintLayout(modifier = modifier) {
            val (titleView, moreBtn) = createRefs()
            Text(
                modifier = Modifier
                    .constrainAs(titleView) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(moreBtn.start)
                        width = Dimension.fillToConstraints
                        height = Dimension.preferredWrapContent
                    },
                text = galleryMediaItem.title,
                maxLines = 3,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = promptFamily
                ), overflow = TextOverflow.Ellipsis
            )
            CustomIconWithMenu(modifier = Modifier
                .constrainAs(moreBtn) {
                    start.linkTo(titleView.end)
                    top.linkTo(titleView.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(titleView.bottom)
                },
                items = emptyList(),
                imageVector = Icons.Rounded.MoreVert,
                onTap = {
//                    onEvent(VideoScreenEvent.SelectItemMenu(videoCard = videoCard, id = it.id))
                }
            )

        }
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

/*IconButton(
                modifier = Modifier
                    .constrainAs(moreBtn) {
                        start.linkTo(titleView.end)
                        top.linkTo(titleView.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(titleView.bottom)
//                            width = Dimension.preferredWrapContent
//                            height = Dimension.fillToConstraints
                    },
                onClick = { }) {
                Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
            }*/