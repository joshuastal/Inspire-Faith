package com.orthodoxquotesapp.quoteapp.dataclasses

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val selectedIconSize: Dp = Dp.Unspecified, // Default size or unspecified
    val unselectedIconSize: Dp = Dp.Unspecified,
)


