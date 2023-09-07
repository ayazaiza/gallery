package com.media.gallery.presentation.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.media.gallery.R
import com.media.gallery.domain.models.GalleryMediaItem
import com.media.gallery.domain.sealedCls.HomeBottomNavigation
import com.media.gallery.domain.util.components.CustomIconWithMenu
import com.media.gallery.domain.util.components.CustomTopBar
import com.media.gallery.presentation.all_albums.AllAlbumsScreen
import com.media.gallery.presentation.home.models.MainHomeState
import com.media.gallery.presentation.photos.GalleryItemsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(
    title: String = stringResource(id = R.string.app_name),
    bottomNavHostController: NavHostController = rememberNavController(),
    mainHomeState: MainHomeState,
    onEvent: (MainHomeEvent) -> Unit,
    files: (GalleryMediaItem) -> Unit,
    onTap: (GalleryMediaItem) -> Unit
) {
    val context = LocalContext.current

    DisposableEffect(key1 = context, effect = {
        val listeners = NavController.OnDestinationChangedListener { _, dest, _ ->
            onEvent(MainHomeEvent.ChangeCurrentRoute(currentRoute = dest.route.toString()))
        }
        bottomNavHostController.addOnDestinationChangedListener(listeners)
        onDispose {
            bottomNavHostController.removeOnDestinationChangedListener(listeners)
        }
    })

    Scaffold(topBar = {
        CustomTopBar(title = title,
            imageVector = Icons.Rounded.Menu,
            titleIsCentered = true,
            onTap = {

            },
            actions = {
                when (mainHomeState.currentRoute) {
                    HomeBottomNavigation.Photos.routeName -> {
                        CustomIconWithMenu(
                            imageVector = Icons.Rounded.FilterList, onTap = { optionMenu ->
                                onEvent(MainHomeEvent.ChangePhotoSortByOption(optionMenu))
                            }, items = mainHomeState.photosState.sortOptions
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomIconWithMenu(
                            imageVector = mainHomeState.photosState.viewTypeOptions.find { it.isSelected }?.icon
                                ?: Icons.Rounded.RemoveRedEye, onTap = { optionMenu ->
                                onEvent(MainHomeEvent.ChangePhotosViewTypeOption(optionMenu))
                            }, items = mainHomeState.photosState.viewTypeOptions
                        )
                    }

                    HomeBottomNavigation.AllFiles.routeName -> {
                        CustomIconWithMenu(
                            imageVector = Icons.Rounded.FilterList, onTap = { optionMenu ->
                                onEvent(MainHomeEvent.ChangeAlbumsSortByOption(optionMenu))
                            }, items = mainHomeState.albumsState.albumsSortList
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomIconWithMenu(
                            imageVector = mainHomeState.albumsState.albumsViewType.find { it.isSelected }?.icon
                                ?: Icons.Rounded.RemoveRedEye, onTap = { optionMenu ->
                                onEvent(MainHomeEvent.ChangeAlbumsViewTypeOption(optionMenu))
                            }, items = mainHomeState.albumsState.albumsViewType
                        )
                    }

                    HomeBottomNavigation.Videos.routeName -> {
                        CustomIconWithMenu(
                            imageVector = Icons.Rounded.FilterList, onTap = { optionMenu ->
                                onEvent(MainHomeEvent.ChangeVideoSortByOption(optionMenu))
                            }, items = mainHomeState.videosState.sortOptions
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomIconWithMenu(
                            imageVector = mainHomeState.videosState.viewTypeOptions.find { it.isSelected }?.icon
                                ?: Icons.Rounded.RemoveRedEye, onTap = { optionMenu ->
                                onEvent(MainHomeEvent.ChangeVideosViewTypeOption(optionMenu))
                            }, items = mainHomeState.videosState.viewTypeOptions
                        )
                    }
                }

            })
    }, bottomBar = {
        NavigationBar {
            val navBackStackEntry by bottomNavHostController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            mainHomeState.bottomList.forEach { bottomNavItem ->
                val isSelected = currentRoute == bottomNavItem.routeName
                NavigationBarItem(selected = isSelected, onClick = {
                    bottomNavHostController.navigate(bottomNavItem.routeName) {
                        popUpTo(bottomNavHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, icon = {
                    Icon(
                        imageVector = if (isSelected) bottomNavItem.activeIcon else bottomNavItem.inActiveIcon,
                        contentDescription = bottomNavItem.title
                    )
                }, label = {
                    Text(text = bottomNavItem.title)
                })
            }
        }
    }) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            navController = bottomNavHostController,
            startDestination = HomeBottomNavigation.Photos.routeName
        ) {
            composable(HomeBottomNavigation.Photos.routeName) {
                GalleryItemsScreen(
                    state = mainHomeState.photosState,
                    modifier = Modifier.fillMaxSize(),
                    onClick = onTap
                )
            }
            composable(HomeBottomNavigation.AllFiles.routeName) {
                AllAlbumsScreen(state = mainHomeState.albumsState, onClick = files)
            }
            composable(HomeBottomNavigation.Videos.routeName) {
                GalleryItemsScreen(
                    state = mainHomeState.videosState,
                    modifier = Modifier.fillMaxSize(),
                    onClick = onTap
                )
            }
        }
    }
}


/*
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    // set up all transformation states
                    var scale by remember { mutableFloatStateOf(1f) }
                    var rotation by remember { mutableFloatStateOf(0f) }
                    var offset by remember { mutableStateOf(Offset.Zero) }
                    val state =
                        rememberTransformableState { zoomChange, offsetChange, rotationChange ->
                            scale *= zoomChange
                            rotation += rotationChange
                            offset += offsetChange
                        }
                    Box(
                        Modifier
                            // apply other transformations like rotation and zoom
                            // on the pizza slice emoji
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
//                                rotationZ = rotation,
                                translationX = offset.x,
                                translationY = offset.y
                            )
                            // add transformable to listen to multitouch transformation events
                            // after offset
                            .transformable(state = state)
                            .background(Color.Blue)
                            .fillMaxSize()
                            .combinedClickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null,
                                onClick = {},
                                onDoubleClick = {
                                    scale = 1f
                                    offset = Offset.Zero
                                }
                            )
                    )
                }



*/