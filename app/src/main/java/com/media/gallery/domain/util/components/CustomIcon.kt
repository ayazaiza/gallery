package com.media.gallery.domain.util.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.media.gallery.R
import com.media.gallery.domain.models.OptionsMenu
import com.media.gallery.ui.theme.promptFamily

@Composable
fun CustomIcon(
    iconCornerSize: Dp = dimensionResource(id = R.dimen.icon_corner_size),
    iconSize: Dp = dimensionResource(id = R.dimen.icon_size),
    iconPadding: Dp = dimensionResource(id = R.dimen.icon_padding),
    title: String,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    tint: Color = MaterialTheme.colorScheme.primary,
    imageVector: ImageVector,
    onTap: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(iconCornerSize))
            .background(color = color)
            .clickable { onTap() }
            .padding(iconPadding),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = title,
            modifier = Modifier.size(iconSize),
            tint = tint
        )
    }
}


@Composable
fun CustomIconWithMenu(
    modifier: Modifier = Modifier,
    iconCornerSize: Dp = dimensionResource(id = R.dimen.icon_corner_size),
    iconSize: Dp = dimensionResource(id = R.dimen.icon_size),
    iconPadding: Dp = dimensionResource(id = R.dimen.icon_padding),
    title: String? = null,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    tint: Color = MaterialTheme.colorScheme.primary,
    imageVector: ImageVector,
    onTap: (OptionsMenu) -> Unit,
    items: List<OptionsMenu>,
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(iconCornerSize))
            .background(color = color)
            .clickable { isContextMenuVisible = true }
            .padding(iconPadding),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = title,
            modifier = Modifier.size(iconSize),
            tint = tint

        )
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
        ) {
            if (title != null) {
                DropdownMenuItem(text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = promptFamily
                        )
                    )
                }, onClick = { }, enabled = false)
            }

            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.title, style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = promptFamily
                            )
                        )
                    },
                    leadingIcon = {
                        if (item.icon != null) {
                            CustomIcon(
                                title = item.title,
                                imageVector = item.icon,
                                iconSize = 20.dp
                            )
                        } else {
                            CustomIcon(
                                title = item.title,
                                painter = painterResource(id = item.painter!!),
                                iconSize = 16.dp
                            )
                        }
                    },
                    trailingIcon = {
                        if (item.isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.Check, contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    onClick = {
                        isContextMenuVisible = false
                        onTap(item)
                    })
            }

        }
    }
}

@Composable
fun CustomIconWithMenu(
    modifier: Modifier = Modifier,
    iconCornerSize: Dp = dimensionResource(id = R.dimen.icon_corner_size),
    iconSize: Dp = dimensionResource(id = R.dimen.icon_size),
    iconPadding: Dp = dimensionResource(id = R.dimen.icon_padding),
    title: String? = null,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    tint: Color = MaterialTheme.colorScheme.primary,
    painter: Painter,
    onTap: (OptionsMenu) -> Unit,
    items: List<OptionsMenu>,
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(iconCornerSize))
            .background(color = color)
            .clickable { isContextMenuVisible = true }
            .padding(iconPadding),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter,
            contentDescription = title,
            modifier = Modifier.size(iconSize),
            tint = tint

        )
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
        ) {
            if (title != null) {
                DropdownMenuItem(text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = promptFamily
                        )
                    )
                }, onClick = { }, enabled = false)
            }

            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.title, style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = promptFamily
                            )
                        )
                    },
                    leadingIcon = {
                        if (item.icon != null) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                painterResource(id = item.painter!!),
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    trailingIcon = {
                        if (item.isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.Check, contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                    },
                    onClick = {
                        isContextMenuVisible = false
                        onTap(item)
                    })
            }

        }
    }
}

@Composable
fun CustomIcon(
    iconCornerSize: Dp = dimensionResource(id = R.dimen.icon_corner_size),
    iconSize: Dp = dimensionResource(id = R.dimen.icon_size),
    iconPadding: Dp = dimensionResource(id = R.dimen.icon_padding),
    title: String,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    painter: Painter,
    onTap: () -> Unit = {},
    tint: Color = MaterialTheme.colorScheme.primary,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(iconCornerSize))
            .background(color = color)
            .clickable { onTap() }
            .padding(iconPadding),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = title,
            modifier = Modifier.size(iconSize),
            tint = tint
        )
    }
}


/*
@Composable
fun CustomIconWithMenuPointer(
    iconCornerSize: Dp = dimensionResource(id = R.dimen.icon_corner_size),
    iconSize: Dp = dimensionResource(id = R.dimen.icon_size),
    iconPadding: Dp = dimensionResource(id = R.dimen.icon_padding),
    title: String,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    imageVector: ImageVector,
    onTap: () -> Unit
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .onSizeChanged {
                itemHeight = with(density) {
                    it.height.toDp()
                }
            }
            .clip(RoundedCornerShape(iconCornerSize))
            .background(color = color)
            .pointerInput(true) {
                detectTapGestures(
                    onTap = {
                        isContextMenuVisible = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    }
                )
            }
            .clickable { onTap() }
            .padding(iconPadding),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = title,
            modifier = Modifier.size(iconSize)
        )
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            )
        ) {
            repeat(3) {
                DropdownMenuItem(
                    text = { Text(text = "$it item") },
                    onClick = { isContextMenuVisible = false })
            }
        }
    }
}*/
