package com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote

object FavoritesManager {
    // Create a mutable list to hold favorite quotes
    val favoriteQuotes = mutableListOf<Quote>()

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private const val FAVORITES_KEY = "favorites_list"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
        loadFavorites()  // Load favorites when the app starts
    }

    private fun loadFavorites() {
        val jsonString = sharedPreferences.getString(FAVORITES_KEY, null)
        if (jsonString != null) {
            val type = object : TypeToken<List<Quote>>() {}.type
            val loadedFavorites: List<Quote> = gson.fromJson(jsonString, type)
            favoriteQuotes.clear()
            favoriteQuotes.addAll(loadedFavorites)
        }
    }

    private fun saveFavorites() {
        val jsonString = gson.toJson(favoriteQuotes)
        sharedPreferences.edit().putString(FAVORITES_KEY, jsonString).apply()
    }


    // Function to add a quote to favorites
    fun addToFavorites(quote: Quote) {
        if (!favoriteQuotes.contains(quote)) {
            favoriteQuotes.add(quote)
            saveFavorites()  // Save after adding
        }
    }

    fun removeFromFavorites(quote: Quote) {
        if (favoriteQuotes.remove(quote)) {
            saveFavorites()  // Save after removing
        }
    }

    fun isFavorite(quote: Quote): Boolean {
        return favoriteQuotes.contains(quote)
    }

    // Function to get all favorite quotes
    fun getFavorites(): List<Quote> {
        var counter = 0

        favoriteQuotes.forEach { quote ->
            counter++ // Increment the counter for each quote
            Log.d("MainScreen", "Favorite #$counter: ${quote.quote}, Author: ${quote.author}")
        }

        if(favoriteQuotes.isEmpty())
            Log.d("MainScreen", "No favorite quotes found")


        return favoriteQuotes
    }

    fun clearFavorites() {
        favoriteQuotes.clear()
        saveFavorites()  // Save after clearing

        var counter = 0
        favoriteQuotes.forEach { quote ->
            counter++ // Increment the counter for each quote
            Log.d("MainScreen", "Favorite #$counter: ${quote.quote}, Author: ${quote.author}")
        }
        if(favoriteQuotes.isEmpty())
            Log.d("MainScreen", "No favorite quotes found")

        Log.d("MainScreen", "All favorite quotes cleared")
    }
}