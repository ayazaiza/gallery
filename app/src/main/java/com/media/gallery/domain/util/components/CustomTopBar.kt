package com.media.gallery.domain.util.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    onTap: () -> Unit,
    titleIsCentered: Boolean = false,
    imageVector: ImageVector = Icons.Rounded.ArrowBackIos,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val menuIcon: @Composable () -> Unit = {
        CustomIcon(
            title = title,
            imageVector = imageVector,
            onTap = onTap
        )
    }
    if (titleIsCentered) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = { DefaultAppBarTitle(text = title) },
            actions = actions,
            navigationIcon = menuIcon,
            colors = colors,
            scrollBehavior = scrollBehavior
        )
    } else {
        TopAppBar(
            modifier = modifier,
            title = { DefaultAppBarTitle(text = title) },
            actions = actions,
            navigationIcon = menuIcon,
            colors = colors,
            scrollBehavior = scrollBehavior
        )
    }
}