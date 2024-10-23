package com.orthodoxquotesapp.quoteapp

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(navController: NavHostController, onComplete: () -> Unit) {
    NavHost(navController = navController, startDestination = "home") {
        composable(
            "home",
            enterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
            ) {
            MainScreen(
                onComplete = onComplete,
                navController = navController
            )
        }
        composable("favorites") {
            FavoritesScreen(navController)
        }
    }
}