package com.media.gallery.domain.util.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable RowScope.() -> Unit,
    popBack: () -> Unit,
    titleIsCentered: Boolean = false
) {
    if (titleIsCentered) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = { DefaultAppBarTitle(text = title) },
            actions = actions,
            navigationIcon = {
                CustomIcon(
                    title = title,
                    imageVector = Icons.Outlined.ArrowBackIos,
                    onTap = popBack
                )
            }
        )
    } else {
        TopAppBar(
            modifier = modifier,
            title = { DefaultAppBarTitle(text = title) },
            actions = actions,
            navigationIcon = {
                CustomIcon(
                    title = title,
                    imageVector = Icons.Outlined.ArrowBackIos,
                    onTap = popBack
                )
            }
        )
    }
}