package com.orthodoxquotesapp.quoteapp.objects

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote

object QuoteData {
    val quotes = mutableStateListOf<Quote>()
    val localQuotes = mutableStateListOf<Quote>()
    val allQuotes = mutableStateListOf<Quote>()
    val isLoading = mutableStateOf(true)

    fun updateAllQuotes() {
        if (allQuotes.isEmpty()) {
            allQuotes.addAll(localQuotes + quotes)
            allQuotes.shuffle()
        }
    }
}