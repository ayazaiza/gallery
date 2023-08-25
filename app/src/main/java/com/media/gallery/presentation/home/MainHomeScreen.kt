package com.media.gallery.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.media.gallery.R
import com.media.gallery.domain.extensions.toFirstCap
import com.media.gallery.domain.models.BottomNavItem
import com.media.gallery.domain.util.components.CustomIcon
import com.media.gallery.domain.util.components.DefaultAppBarTitle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(
    title: String = stringResource(id = R.string.app_name)
) {

    val bottomItems = listOf(
        BottomNavItem(
            title = stringResource(id = R.string.folders),
            activeIcon = Icons.Rounded.Folder,
            inActiveIcon = Icons.Outlined.Folder
        ),
        BottomNavItem(
            title = stringResource(id = R.string.photos).toFirstCap(),
            activeIcon = Icons.Rounded.Image,
            inActiveIcon = Icons.Outlined.Image
        ),
        BottomNavItem(
            title = stringResource(id = R.string.videos),
            activeIcon = Icons.Rounded.Movie,
            inActiveIcon = Icons.Outlined.Movie
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            DefaultAppBarTitle(text = title)
        }, navigationIcon = {
            CustomIcon(
                title = title,
                imageVector = Icons.Rounded.Menu,
                onTap = {

                }
            )
        })
    }, bottomBar = {
        NavigationBar {
            bottomItems.forEachIndexed { index, bottomNavItem ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        selectedItemIndex = index
                    },
                    icon = {
                        Icon(
                            imageVector = if (selectedItemIndex == index) bottomNavItem.activeIcon else bottomNavItem.inActiveIcon,
                            contentDescription = bottomNavItem.title
                        )
                    },
                    label = {
                        Text(text = bottomNavItem.title)
                    }
                )
            }
        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

        }
    }
}