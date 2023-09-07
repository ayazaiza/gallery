package com.media.gallery.domain.util

import com.media.gallery.config.AppConstants


sealed class Routes(val route: String, val name: String) {
    data object AppInitialScreen :
        Routes(AppConstants.APP_INITIAL_ROUTE, AppConstants.APP_INITIAL_ROUTE)

    data object AppPermissionScreen :
        Routes(AppConstants.APP_PERMISSIONS_ROUTE, AppConstants.APP_PERMISSIONS_ROUTE)

    data object AllFilesScreen :
        Routes(AppConstants.APP_ALL_FILES_NAME, AppConstants.APP_ALL_FILES_NAME)

    data object FilesScreen :
        Routes(AppConstants.APP_FILES_ROUTE, AppConstants.APP_FILES_NAME)


    data object AllVideosScreen :
        Routes(AppConstants.APP_ALL_VIDEOS_ROUTE, AppConstants.APP_ALL_VIDEOS_NAME)

    data object AllPhotosScreen :
        Routes(AppConstants.APP_ALL_PHOTOS_ROUTE, AppConstants.APP_ALL_PHOTOS_NAME)

    data object FileDetailsScreen :
        Routes(AppConstants.APP_FILE_DETAILS_ROUTE, AppConstants.APP_FILE_DETAILS_NAME)

    data object SettingsScreen :
        Routes(AppConstants.APP_SETTINGS_ROUTE, AppConstants.APP_SETTINGS_ROUTE)

    data object FileManagerScreen :
        Routes(AppConstants.APP_FILE_MANAGER_ROUTE, AppConstants.APP_FILE_MANAGER_NAME)

    data object SearchScreen : Routes(AppConstants.APP_SEARCH_ROUTE, AppConstants.APP_SEARCH_NAME)
    data object PrivacyPolicyScreen :
        Routes(AppConstants.APP_PRIVACY_POLICY_ROUTE, AppConstants.APP_PRIVACY_POLICY_ROUTE)

    data object BlockAdsScreen :
        Routes(AppConstants.APP_BLOCK_ADS_ROUTE, AppConstants.APP_BLOCK_ADS_ROUTE)
}
