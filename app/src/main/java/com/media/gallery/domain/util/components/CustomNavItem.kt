package com.media.gallery.domain.util.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.media.gallery.ui.theme.promptFamily

@Composable
fun CustomNavItem(
    title: String,
    onClick: () -> Unit,
    icon: ImageVector,
    isSelected: Boolean = false,
    iconTint: Color = MaterialTheme.colorScheme.primary
) {
    CustomNavItem(
        title = title,
        icon = {
            Icon(
                icon,
                contentDescription = title,
                tint = iconTint,
            )
        },
        onClick = onClick,
        isSelected = isSelected
    )

}

@Composable
fun CustomNavItem(
    title: String,
    onClick: () -> Unit,
    icon: Painter,
    isSelected: Boolean = false,
    iconTint: Color = MaterialTheme.colorScheme.primary
) {
    CustomNavItem(
        title = title,
        icon = {
            Icon(
                icon,
                contentDescription = title,
                tint = iconTint
            )
        },
        onClick = onClick,
        isSelected = isSelected
    )
}

@Composable
private fun CustomNavItem(
    title: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    isSelected: Boolean = false
) {
    NavigationDrawerItem(
        icon = icon,
        label = {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = promptFamily,
                    fontWeight = FontWeight.W400
                )
            )
        },
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

/* NavigationDrawerItem(
       icon = {
           Icon(
               icon,
               contentDescription = title,
               tint = MaterialTheme.colorScheme.primary
           )
       },
       label = {
           Text(
               title,
               style = MaterialTheme.typography.bodyLarge.copy(
                   fontFamily = promptFamily,
                   fontWeight = FontWeight.W500
               )
           )
       },
       selected = isSelected,
       onClick = onClick,
       modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
   )*/