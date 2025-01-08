package com.orthodoxquotesapp.quoteapp.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.orthodoxquotesapp.quoteapp.dataclasses.BottomNavigationItem

@Composable
fun BottomNavigationBar(
    navController: NavController,
    bottomNavBarItems: List<BottomNavigationItem>,
    modifier: Modifier = Modifier
) {
    // Get the current route from the navController
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // Track selected item based on the current route
    var selectedIndex by rememberSaveable { mutableIntStateOf(1) } // Default to index 0

    // Find the index of the currently active screen
    bottomNavBarItems.forEachIndexed { index, item ->
        if (item.title == currentRoute) {
            selectedIndex = index // Update the selected index if current route matches the title
        }
    }

    // Build the NavigationBar
    NavigationBar(modifier = modifier) {
        bottomNavBarItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    if (selectedIndex != index) {
                        selectedIndex = index
                        navController.navigate(item.title) {
                            // Prevent multiple copies of the same destination in the back stack
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    val isSelected = selectedIndex == index
                    val size = if (isSelected) item.selectedIconSize else item.unselectedIconSize
                    val adjustedSize = if (isSelected) {
                        size * 1.2f // Scale up the filled icon slightly
                    } else {
                        size
                    }
                    val containerSize = 30.dp // Fixed size for the container
                    Box(
                        modifier = Modifier
                            .size(containerSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            modifier = if (size != Dp.Unspecified) Modifier.size(adjustedSize) else Modifier
                        )
                    }
                },
                label = { Text(item.title) }
            )
        }
    }
}