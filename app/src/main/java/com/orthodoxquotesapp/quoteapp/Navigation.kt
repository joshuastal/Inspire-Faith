package com.orthodoxquotesapp.quoteapp

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.orthodoxquotesapp.quoteapp.screens.CalendarScreen
import com.orthodoxquotesapp.quoteapp.screens.FavoritesScreen
import com.orthodoxquotesapp.quoteapp.screens.HomeScreen
import com.orthodoxquotesapp.quoteapp.screens.QuotesScreen
import com.orthodoxquotesapp.quoteapp.screens.ReadingsScreen.ReadingsScreen
import com.orthodoxquotesapp.quoteapp.screens.ReadingsScreen.SaintsScreen
import com.orthodoxquotesapp.quoteapp.screens.SettingsScreen
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.FavoritesManager

@Composable
fun Navigation(navController: NavHostController, onComplete: () -> Unit) {
    val favoritesPagerState = rememberPagerState(pageCount = { FavoritesManager.favoriteQuotes.size })

    NavHost(navController = navController, startDestination = "home") {
        composable(
            "quotes",
//            enterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
//            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
            ) {
            QuotesScreen(
                favoritesPagerState = favoritesPagerState,
            )
        }
        composable("favorites") {
            FavoritesScreen(favoritesPagerState)
        }
        composable("readings") {
            ReadingsScreen()
        }
        composable("saints") {
            SaintsScreen()
        }
        composable("calendar"){
            CalendarScreen()
        }
        composable("home"){
            HomeScreen(onComplete, navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
    }
}