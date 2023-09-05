package com.media.gallery.domain.sealedCls

import com.media.gallery.config.AppConstants

sealed class HomeBottomNavigation(val routeName: String) {
    data object AllFiles : HomeBottomNavigation(AppConstants.ALL_FILES_BOTTOM_ROUTE_NAME)
    data object Photos : HomeBottomNavigation(AppConstants.PHOTOS_BOTTOM_ROUTE_NAME)
    data object Videos : HomeBottomNavigation(AppConstants.VIDEOS_BOTTOM_ROUTE_NAME)
}
