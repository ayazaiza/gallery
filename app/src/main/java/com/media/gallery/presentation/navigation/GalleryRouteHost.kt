package com.media.gallery.presentation.navigation

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.models.HomeState
import com.media.gallery.domain.util.Routes
import com.media.gallery.presentation.home.MainHomeScreen
import com.media.gallery.presentation.initial.InitialScreen
import com.media.gallery.presentation.initial.InitialScreenViewModel
import com.media.gallery.presentation.permissions.PermissionScreen
import com.media.gallery.presentation.permissions.PermissionsViewModel


@Composable
fun GalleryRouteHost(
    navController: NavHostController,
    onEvent: (HomeViewEvent) -> Unit,
    homeState: HomeState
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.AppInitialScreen.route
        ) {
            composable(route = Routes.AppInitialScreen.route) {
                val viewModel = hiltViewModel<InitialScreenViewModel>()
                InitialScreen(
                    navToHome = {
                        navController.navigate(
                            Routes.AllFilesScreen.name
                        ) {
                            popUpTo(Routes.AppInitialScreen.name) {
                                inclusive = true
                            }
                        }
                    },
                    navToPermissions = {
                        navController.navigate(
                            Routes.AppPermissionScreen.name
                        ) {
                            popUpTo(Routes.AppInitialScreen.name) {
                                inclusive = true
                            }
                        }
                    },
                    permissions = viewModel.requestedPermissions,
                    fetchVideo = viewModel::fetchVideos
                )
            }


            composable(route = Routes.AppPermissionScreen.route) {
                val viewModel = hiltViewModel<PermissionsViewModel>()
                val state by viewModel.showDialog.collectAsStateWithLifecycle()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                PermissionScreen(
                    navToHome = {
                        navController.navigate(Routes.AllFilesScreen.name) {
                            popUpTo(Routes.AppPermissionScreen.name) {
                                inclusive = true
                            }
                        }
                    },
                    permissions = viewModel.requestedPermissions,
                    hideDialog = viewModel::hideAlertDialog,
                    showDialog = viewModel::showAlertDialog,
                    isDialogShow = state,
                    intent = intent,
                    fetchData = viewModel::fetchData
                )
            }

            composable(route = Routes.AllFilesScreen.route) {
                MainHomeScreen()
                /* val viewModel = hiltViewModel<FoldersViewModel>()
                 val state by viewModel.state.collectAsStateWithLifecycle()
                 if (state.mediaDirectories.isNotEmpty() && !state.isOneDayPremium) {
                     viewModel.onEvent(FolderScreenEvent.LoadInterAds)
                 }
                 FoldersScreen(
                     state = state,
                     navToVideos = { path, mediaCount, name ->
                         onEvent(
                             HomeViewEvent.ShowAdsFromCompose {
                                 navController.navigate(
                                     buildLink(
                                         Routes.AllVideosScreen.name,
                                         AppConstants.PATH_PARAM,
                                         path,
                                         AppConstants.COUNT_PARAM,
                                         mediaCount.toString(),
                                         AppConstants.FOLDER_NAME_PARAM,
                                         name
                                     )
                                 )
                             }
                         )
                         *//*navController.navigate(
                            buildLink(
                                Routes.AllVideosScreen.name,
                                AppConstants.PATH_PARAM,
                                path,
                                AppConstants.COUNT_PARAM,
                                mediaCount.toString(),
                                AppConstants.FOLDER_NAME_PARAM,
                                name
                            )
                        )*//*
                    },
                    onEvent = viewModel::onEvent,
                    settingsScreen = {
                        navController.navigate(Routes.SettingsScreen.name)
                    },
                    fileManagerScreen = {
                        navController.navigate(Routes.FileManagerScreen.name)
                    }, shareScreen = {
                        onEvent(HomeViewEvent.ShareApp)
                    },
                    searchScreen = {

                        navController.navigate(Routes.SearchScreen.name)
                    },
                    blockAdsScreen = {
                        navController.navigate(Routes.BlockAdsScreen.name)

                    }
                )*/

            }

            composable(
                route = Routes.AllVideosScreen.route,
                arguments = listOf(
                    navArgument(AppConstants.PATH_PARAM) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument(AppConstants.COUNT_PARAM) {
                        type = NavType.IntType
                        defaultValue = 0
                    },
                    navArgument(AppConstants.FOLDER_NAME_PARAM) {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) { navBackStackEntry ->
                /* val title = navBackStackEntry.arguments?.getString(AppConstants.FOLDER_NAME_PARAM)
                     ?: stringResource(
                         id = R.string.all_videos
                     )
                 val viewModel = hiltViewModel<VideosViewModel>()
                 val state by viewModel.state.collectAsStateWithLifecycle()

                 VideosScreen(title = title, onEvent = { videoScreenEvent ->
                     if (videoScreenEvent is VideoScreenEvent.SelectItemMenu) {
                         if (videoScreenEvent.id == AppConstants.PLAY) {
                             onEvent(
                                 HomeViewEvent.PlayVideos(
                                     state.allVideos,
                                     videoScreenEvent.videoCard.path
                                 )
                             )
                             return@VideosScreen
                         }
                         if (videoScreenEvent.id == AppConstants.VIDEO_DETAILS) {
                             val id = videoScreenEvent.videoCard.id
                             if (id != null) {
                                 onEvent(
                                     HomeViewEvent.ShowAdsFromCompose {
                                         navController.navigate(
                                             buildLink(
                                                 Routes.VideoDetailsScreen.name,
                                                 AppConstants.ID_PARAM,
                                                 id.toString()
                                             )
                                         )
                                     }
                                 )
                             }
                             return@VideosScreen
                         }
                     }
                     viewModel.onEvent(videoScreenEvent)
                 }, navBack = {
                     navController.popBackStack()
                 }, state = state, videoUpdates = viewModel.videoUIAction)*/
            }


            composable(
                route = Routes.FileDetailsScreen.route,
                arguments = listOf(
                    navArgument(AppConstants.ID_PARAM) {
                        type = NavType.StringType
                    }
                )
            ) {
                /*  val viewModel = hiltViewModel<VideoDetailsViewModel>()
                  val state by viewModel.state.collectAsStateWithLifecycle()
                  VideoDetailsScreen(
                      state = state, navigateUp = { navController.popBackStack() },
                      playVideo = { path ->
                          onEvent(HomeViewEvent.PlayLink(path))
                      }
                  )*/
            }

            composable(route = Routes.SearchScreen.route) {
                /* val viewModel = hiltViewModel<SearchViewModel>()
                 val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                 val state by viewModel.state.collectAsStateWithLifecycle()
                 SearchScreen(
                     goBack = {
                         navController.popBackStack()
                     },
                     searchQuery = searchQuery,
                     state = state,
                     onSearch = viewModel::onSearch,
                     play = { path ->
                         onEvent(HomeViewEvent.PlayLink(path))
                     }
                 )*/
            }

            composable(route = Routes.SettingsScreen.route) {
                /*  SettingsScreen(goBack = {
                      navController.popBackStack()
                  }, settingState = homeState, onEvent = onEvent)*/
            }

            composable(route = Routes.FileManagerScreen.route,
                arguments = listOf(
                    navArgument(AppConstants.PATH_PARAM) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) {
                /*val viewModel = hiltViewModel<FileManagerViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val title = state.title.ifBlank { stringResource(id = R.string.internal_storage) }
                FileManagerScreen(title = title, goBack = {
                    navController.popBackStack()
                }, state = state, onNext = { path ->
                    onEvent(
                        HomeViewEvent.ShowAdsFromCompose {
                            navController.navigate(
                                buildLink(
                                    Routes.FileManagerScreen.name,
                                    AppConstants.PATH_PARAM,
                                    path
                                )
                            )
                        }
                    )
                }, onPlay = { path ->
                    onEvent(HomeViewEvent.PlayLink(path))
                })*/
            }
            composable(route = Routes.PrivacyPolicyScreen.route) {
                /* PrivacyPolicyScreen(goBack = {
                     navController.popBackStack()
                 })*/
            }
            composable(route = Routes.BlockAdsScreen.route) {
//                val viewModel = hiltViewModel<BlockAdsViewModel>()
//                val state by viewModel.state.collectAsStateWithLifecycle()
                /*     BlockAdsScreen(goBack = {
                         navController.popBackStack()
                     }, state = homeState, onEvent = onEvent)*/
            }

        }
    }
}


/* composable(route = Routes.DisclaimerScreen.route) {
     DisclaimerScreen(goBack = {
         navController.popBackStack()
     })
 }*/