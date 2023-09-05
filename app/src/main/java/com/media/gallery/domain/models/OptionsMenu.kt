package com.media.gallery.domain.models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class OptionsMenu(
    val title: String,
    val id: Int,
    val isSelected: Boolean = false,
    val icon: ImageVector? = null,
    @DrawableRes val painter: Int? = null
)
