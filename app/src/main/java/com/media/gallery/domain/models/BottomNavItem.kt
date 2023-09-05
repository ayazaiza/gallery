package com.media.gallery.domain.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.media.gallery.domain.sealedCls.HomeBottomNavigation

data class BottomNavItem(
    val title: String,
    val activeIcon: ImageVector,
    val inActiveIcon: ImageVector,
    val routeName: String,
    val homeBottomNavigation: HomeBottomNavigation
)