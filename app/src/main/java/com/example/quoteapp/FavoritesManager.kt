package com.example.quoteapp

import android.util.Log

object FavoritesManager {
    // Create a mutable list to hold favorite quotes
    val favoriteQuotes = mutableListOf<Quote>()

    // Function to add a quote to favorites
    fun addToFavorites(quote: Quote) {
        if (!favoriteQuotes.contains(quote)) {
            favoriteQuotes.add(quote)
        }
    }

    fun removeFromFavorites(quote: Quote){
        favoriteQuotes.remove(quote)
    }

    fun isFavorite(quote: Quote): Boolean {
        return favoriteQuotes.contains(quote)
    }

    // Function to get all favorite quotes
    fun getFavorites(): List<Quote> {
        var counter = 0

        favoriteQuotes.forEach { quote ->
            counter++ // Increment the counter for each quote
            Log.d("Favorites", "Favorite #$counter: ${quote.quote}, Author: ${quote.author}")

            if(favoriteQuotes.isEmpty())
                Log.d("Favorites", "No favorite quotes found")
        }


        return favoriteQuotes
    }
}