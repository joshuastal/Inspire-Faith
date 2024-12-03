package com.orthodoxquotesapp.quoteapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.FavoritesManager

@Composable
fun FavoritesScreen(pagerState: PagerState) {
    val favoritesList = FavoritesManager.favoriteQuotes

    Box(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        if (favoritesList.isEmpty()) {
            // Show special QuoteCard when there are no favorites
            EmptyQuoteCard(modifier = Modifier.fillMaxSize())
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
@Composable
fun EmptyQuoteCard(modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text( // QUOTE BODY
            text = "No favorites yet...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}
