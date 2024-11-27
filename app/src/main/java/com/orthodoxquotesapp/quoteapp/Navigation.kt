package com.orthodoxquotesapp.quoteapp

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.FavoritesManager

@Composable
fun Navigation(navController: NavHostController, onComplete: () -> Unit) {
    val favoritesPagerState = rememberPagerState(pageCount = { FavoritesManager.favoriteQuotes.size })

    NavHost(navController = navController, startDestination = "home") {
        composable(
            "home",
            enterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
            ) {
            MainScreen(
                onComplete = onComplete,
                favoritesPagerState = favoritesPagerState,
                navController = navController
            )
        }
        composable("favorites") {
            FavoritesScreen(navController, favoritesPagerState)
        }
        composable("readings") {
            ReadingsScreen(navController)
        }
    }
}