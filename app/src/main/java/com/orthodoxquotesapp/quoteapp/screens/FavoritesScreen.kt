package com.orthodoxquotesapp.quoteapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.orthodoxquotesapp.quoteapp.composables.quotes_screen.QuoteCard
import com.orthodoxquotesapp.quoteapp.composables.quotes_screen.NoFavoritesDisplay
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.FavoritesManager

@Composable
fun FavoritesScreen(pagerState: PagerState) {
    val favoritesList = FavoritesManager.favoriteQuotes

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        if (favoritesList.isEmpty()) {
            // Show special QuoteCard when there are no favorites
            NoFavoritesDisplay(modifier = Modifier.fillMaxSize())
        } else {
            Box(modifier = Modifier){
                // VerticalPager for displaying favorite quotes
                VerticalPager(
                    state = pagerState,
                    userScrollEnabled = true,
                ) { verticalPage ->
                    QuoteCard(quote = favoritesList[verticalPage], modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
