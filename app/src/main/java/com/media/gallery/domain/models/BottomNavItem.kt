package com.media.gallery.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val activeIcon: ImageVector,
    val inActiveIcon: ImageVector
)