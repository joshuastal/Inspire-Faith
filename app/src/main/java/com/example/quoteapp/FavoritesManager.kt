package com.example.quoteapp

object FavoritesManager {
    // Create a mutable list to hold favorite quotes
    val favoriteQuotes = mutableListOf<Quote>()

    // Function to add a quote to favorites
    fun addToFavorites(quote: Quote) {
        if (!favoriteQuotes.contains(quote)) {
            favoriteQuotes.add(quote)
        }
    }

    // Function to get all favorite quotes
    fun getFavorites(): List<Quote> {
        return favoriteQuotes
    }
}