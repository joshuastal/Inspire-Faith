package com.orthodoxquotesapp.quoteapp.composables.buttons

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.FavoritesManager

@Composable
fun FavoriteIconButton(quote: Quote) {
    // Store the initial state of whether the quote is favorited or not
    var isFavorited by remember { mutableStateOf(false) }

    // Use a side effect to get the favorite status initially and update the state accordingly
    LaunchedEffect(quote) {
        isFavorited = FavoritesManager.isFavorite(quote)
    }

    IconButton(
        onClick = {
            if (isFavorited) {
                FavoritesManager.removeFromFavorites(quote)
                FavoritesManager.getFavorites()
                Log.d("Favorites", "Favorite removed: ${quote.quote}, Author: ${quote.author}")

            } else {
                FavoritesManager.addToFavorites(quote)
                FavoritesManager.getFavorites()
                Log.d("Favorites", "Favorite added: ${quote.quote}, Author: ${quote.author}")
            }
            isFavorited = !isFavorited // Toggle the local state
        }
    ) {
        Icon(
            imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorited) "Unfavorite" else "Favorite",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
    }
}