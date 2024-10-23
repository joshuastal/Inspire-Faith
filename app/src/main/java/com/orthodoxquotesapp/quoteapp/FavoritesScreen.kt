package com.orthodoxquotesapp.quoteapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun FavoritesScreen(navController: NavController) {

    val favoritesList = FavoritesManager.favoriteQuotes

    Box(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        val verticalPagerState = rememberPagerState(pageCount = {favoritesList.size})
        VerticalPager(
            state = verticalPagerState,
            userScrollEnabled = true,
        ) { verticalPage ->
            QuoteCard(quote = favoritesList[verticalPage], modifier = Modifier.fillMaxSize())
            Text(
                text = "Quote: ${verticalPage + 1}",
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}