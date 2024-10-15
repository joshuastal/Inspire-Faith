package com.example.quoteapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object LocalQuoteManager {
    private const val PREFS_NAME = "quotes_prefs"
    private const val QUOTES_KEY = "local_quotes"

    fun saveQuotes(context: Context, quotes: List<Quote>) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(quotes) // Convert the quotes list to JSON
        editor.putString(QUOTES_KEY, json)
        editor.apply()
    }

    fun getSavedQuotes(context: Context): MutableList<Quote> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(QUOTES_KEY, null)
        val type = object : TypeToken<MutableList<Quote>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }
}