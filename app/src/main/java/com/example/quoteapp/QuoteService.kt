package com.example.quoteapp

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList

class QuoteService {
     fun retrieveQuotes(quotesList: SnapshotStateList<Quote>, firebaseService: FirebaseService, onComplete: () -> Unit): SnapshotStateList<Quote> {

         var counter = 0

        firebaseService.quotesToList( // quotesToList adds quotes into the list that is entered into the parameters of retrieveQuotes
            quotesList, // Pass the empty list to the method
            onComplete = { quotes ->
                // This block runs on success
                quotes.forEach { quote ->
                    counter++ // Increment the counter for each quote
                    Log.d("QuoteService", "QuoteService $counter: ${quote.quote}, Author: ${quote.author}")
                }
                Log.d("QuoteService", "ALL QUOTES RECEIVED: TOTAL $counter")

                onComplete() // Return the list of quotes

            },
            onFailure = { exception ->
                // This block runs on failure
                Log.e("QuoteService", "Error fetching quotes: ", exception) // Log the error
            }
        )
        return quotesList // Return the list of quotes
    }
}