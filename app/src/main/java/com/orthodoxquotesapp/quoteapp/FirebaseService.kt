package com.orthodoxquotesapp.quoteapp

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote

class FirebaseService{
    private val db = Firebase.firestore

    // Takes in an empty list and adds the quotes from firestore to it
    fun quotesToList(
        quotesList: SnapshotStateList<Quote>,
        onComplete: (List<Quote>) -> Unit,
        onFailure: (Exception) -> Unit)
    {
        db.collection("Quotes").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val quote = Quote(
                        author = document.getString("Author") ?: "Unknown",
                        quote = document.getString("Quote") ?: "No quote available"
                    )
                    quotesList.add(quote) // Add each quote to the provided list
                }
                onComplete(quotesList) // Return the shuffled list
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}