package com.media.gallery.presentation.album

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.util.components.CustomIconWithMenu
import com.media.gallery.domain.util.components.CustomTopBar
import com.media.gallery.presentation.home.MainHomeEvent
import com.media.gallery.presentation.photos.GalleryItemsScreen
import com.media.gallery.presentation.photos.models.GalleryItemsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    title: String, popBack: () -> Unit,
    onEvent: (MainHomeEvent) -> Unit,
    state: GalleryItemsState,
    onClick: (GalleryMediaItem) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(title = title, onTap = popBack, actions = {
                CustomIconWithMenu(
                    imageVector = Icons.Rounded.FilterList, onTap = { optionMenu ->
                        onEvent(MainHomeEvent.ChangePhotoSortByOption(optionMenu))
                    }, items = state.sortOptions
                )
                Spacer(modifier = Modifier.width(8.dp))
                CustomIconWithMenu(
                    imageVector = state.viewTypeOptions.find { it.isSelected }?.icon
                        ?: Icons.Rounded.RemoveRedEye, onTap = { optionMenu ->
                        onEvent(MainHomeEvent.ChangePhotosViewTypeOption(optionMenu))
                    }, items = state.viewTypeOptions
                )
            })
        },
    ) { paddingValues ->
        GalleryItemsScreen(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = state,
            onClick = onClick
        )
    }
}