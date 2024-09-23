package com.example.quoteapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


//private lateinit var quotes: MutableList<Quote>
private lateinit var  quotes: SnapshotStateList<Quote>
private lateinit var firebaseService: FirebaseService
private lateinit var quoteService: QuoteService

class MainActivity : ComponentActivity() {

    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the services and the quotes list
        //quotes = mutableListOf()
        quotes = SnapshotStateList<Quote>()
        firebaseService = FirebaseService()
        quoteService = QuoteService()

        quoteService.retrieveQuotes(quotes)


        setContent {
            Greeting(quotes, Modifier
                .padding(top = 20.dp)
                .verticalScroll(rememberScrollState())
            )
        }
        Log.d(TAG, "Quotes displayed: ${quotes.size}")

    }
}

@Composable
fun Greeting(quotes: MutableList<Quote>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        quotes.forEach { quote ->
            Text(
                text = "Quote: ${quote.quote}\nAuthor: ${quote.author}",
                modifier = Modifier.padding(8.dp) // Add padding for better readability
            )
        }
    }
}


