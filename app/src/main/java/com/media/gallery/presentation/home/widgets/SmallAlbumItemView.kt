package com.media.gallery.presentation.home.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.media.gallery.R
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.ui.theme.promptFamily

@Composable
fun SmallAlbumItemView(
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
        val (titleView, sizeView) = createRefs()

        Text(
            modifier = Modifier.constrainAs(titleView) {
                start.linkTo(parent.start, 6.dp)
                top.linkTo(parent.top, 6.dp)
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
                bottom.linkTo(parent.bottom)
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
