package com.example.quoteapp

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseService{
    val db = Firebase.firestore

    private var counter = 0
    // Log quotes for debugging
    val logQuotes = db.collection("Quotes").get()
        .addOnSuccessListener { result ->
            for (document in result) {
                counter++
                Log.d("FirebaseService", "Firebase Quote $counter: ${document.id} => ${document.data}")
            }
        }
        .addOnFailureListener { exception ->
            Log.w("FirebaseService", "Error getting documents.", exception)
        }


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
                quotesList.shuffle() // Shuffle the list after adding all quotes
                onComplete(quotesList) // Return the shuffled list
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}