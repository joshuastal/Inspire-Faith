package com.orthodoxquotesapp.quoteapp.screens.homeUtilities

import android.content.Context
import com.orthodoxquotesapp.quoteapp.objects.QuoteData
import com.orthodoxquotesapp.quoteapp.retrofit_things.CalendarViewModel
import com.orthodoxquotesapp.quoteapp.services.FirebaseService
import com.orthodoxquotesapp.quoteapp.services.QuoteService
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.LocalQuoteManager
import kotlinx.coroutines.delay

// Separate function for loading quotes and completing initialization
private suspend fun loadQuotes(context: Context) {
    val firebaseService = FirebaseService()
    val quoteService = QuoteService()

    // Load local quotes
    val savedQuotes = LocalQuoteManager.getSavedQuotes(context)
    savedQuotes.forEach { savedQuote ->
        if (!QuoteData.localQuotes.contains(savedQuote)) {
            QuoteData.localQuotes.add(savedQuote)
        }
    }

    // Load remote quotes
    quoteService.retrieveQuotes(QuoteData.quotes, firebaseService)

    // Wait for quotes to load
    while (QuoteData.quotes.isEmpty()) {
        delay(1)
    }

    // Update allQuotes
    QuoteData.updateAllQuotes()
    QuoteData.isLoading.value = false


}

suspend fun loadAllDataThenSignalCompletion(context: Context, onComplete: () -> Unit){
    loadQuotes(context)

    onComplete()
}