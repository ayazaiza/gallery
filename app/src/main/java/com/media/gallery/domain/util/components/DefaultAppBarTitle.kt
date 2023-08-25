package com.media.gallery.domain.util.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.media.gallery.ui.theme.promptFamily

@Composable
fun DefaultAppBarTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontFamily = promptFamily
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}