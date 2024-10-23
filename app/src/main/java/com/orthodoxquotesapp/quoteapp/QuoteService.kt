package com.orthodoxquotesapp.quoteapp

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote

class QuoteService {
    // Calls the quotesToList function from FirebaseService and returns the list
    // Also logs the quotes to the console and prints the amount of quotes received
    fun retrieveQuotes(quotesList: SnapshotStateList<Quote>,
                       firebaseService: FirebaseService,
                       ):
             SnapshotStateList<Quote> {

         var counter = 0

        firebaseService.quotesToList( // quotesToList adds quotes into the list that is entered into the parameters of retrieveQuotes
            quotesList, // Pass the empty list to the method
            onComplete = { quotes ->
                // This block runs on success
                quotes.forEach { quote ->
                    counter++ // Increment the counter for each quote
                    Log.d("QuoteService", "QuoteService $counter: ${quote.quote}, Author: ${quote.author}")
                }
                Log.d("QuoteService", "ALL QUOTES RECEIVED: TOTAL " + quotes.size)


            },
            onFailure = { exception ->
                // This block runs on failure
                Log.e("QuoteService", "Error fetching quotes: ", exception) // Log the error
            }
        )
        return quotesList // Return the list of quotes
    }



}