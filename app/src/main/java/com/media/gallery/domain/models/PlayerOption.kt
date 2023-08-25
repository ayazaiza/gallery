package com.media.gallery.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class PlayerOption(
    val title: String,
    val id: Int,
    val isSelected: Boolean = false,
    val icon: ImageVector
)
