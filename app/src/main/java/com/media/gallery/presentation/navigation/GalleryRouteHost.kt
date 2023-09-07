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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.media.gallery.R
import com.media.gallery.config.AppConstants
import com.media.gallery.domain.models.HomeState
import com.media.gallery.domain.util.Routes
import com.media.gallery.presentation.album.AlbumScreen
import com.media.gallery.presentation.album.AlbumViewModel
import com.media.gallery.presentation.details.FileDetailsScreen
import com.media.gallery.presentation.details.FileDetailsViewModel
import com.media.gallery.presentation.home.MainHomeScreen
import com.media.gallery.presentation.home.MainHomeViewModel
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
                val viewModel = hiltViewModel<MainHomeViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                MainHomeScreen(
                    mainHomeState = state,
                    onEvent = viewModel::onEvent,
                    files = { galleryMediaItem ->
                        navController.navigate(
                            AppConstants.buildLink(
                                Routes.FilesScreen.name,
                                AppConstants.PATH_PARAM,
                                galleryMediaItem.parentPath,
                                AppConstants.FOLDER_NAME_PARAM,
                                galleryMediaItem.folderName
                            )
                        )
                    },
                    onTap = { galleryMediaItem ->
                        navController.navigate(
                            AppConstants.buildLink(
                                Routes.FileDetailsScreen.name,
                                AppConstants.ID_PARAM,
                                galleryMediaItem.id.toString()
                            )
                        )
                    }
                )
            }

            composable(
                route = Routes.FilesScreen.route,
                arguments = listOf(
                    navArgument(AppConstants.PATH_PARAM) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument(AppConstants.FOLDER_NAME_PARAM) {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) { navBackStackEntry ->
                val title = navBackStackEntry.arguments?.getString(AppConstants.FOLDER_NAME_PARAM)
                    ?: stringResource(
                        id = R.string.all_files
                    )
                val viewModel = hiltViewModel<AlbumViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                AlbumScreen(title = title, popBack = {
                    navController.navigateUp()
                }, state = state, onEvent = viewModel::onEvent,
                    onClick = { galleryMediaItem ->
                        navController.navigate(
                            AppConstants.buildLink(
                                Routes.FileDetailsScreen.name,
                                AppConstants.ID_PARAM,
                                galleryMediaItem.id.toString()
                            )
                        )
                    })

            }


            composable(
                route = Routes.FileDetailsScreen.route,
                arguments = listOf(
                    navArgument(AppConstants.ID_PARAM) {
                        type = NavType.StringType
                    }
                )
            ) {
                val viewModel = hiltViewModel<FileDetailsViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                FileDetailsScreen(
                    state = state, navigateUp = { navController.navigateUp() },
                    playVideo = { path ->
                        onEvent(HomeViewEvent.PlayLink(path))
                    }
                )
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