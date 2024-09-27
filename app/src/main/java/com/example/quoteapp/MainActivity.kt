package com.example.quoteapp

import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.quoteapp.ui.theme.QuoteAppTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay


//private lateinit var quotes: MutableList<Quote>
private lateinit var  quotes: SnapshotStateList<Quote>
private lateinit var firebaseService: FirebaseService
private lateinit var quoteService: QuoteService

class MainActivity : ComponentActivity() {

    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()


        // Incorporate the splash screen and ensure it stays on screen until quotes are loaded
        var isQuotesLoaded by mutableStateOf(false)
        installSplashScreen().apply { setKeepOnScreenCondition { !isQuotesLoaded} }


        // Initialize the services and the quotes list
        //quotes = mutableListOf()
        quotes = mutableStateListOf<Quote>()
        firebaseService = FirebaseService()
        quoteService = QuoteService()

        quoteService.retrieveQuotes(quotes, firebaseService) {
            isQuotesLoaded = true
        }
        var logCounter = 0

        setContent {
            QuoteAppTheme {
                logCounter += 1
                MainScreen(quotes)
                Log.d(TAG, "This is called from inside setContent: ${quotes.size} call # $logCounter")
            }
        }
    }



}

@Composable
fun MainScreen(quotes: MutableList<Quote>, modifier: Modifier = Modifier) {

    MaterialTheme(
    ) {
        Box(modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
        ) {
                DisplayQuotes(quotes = quotes)
            }
        }
    }

@Composable
fun DisplayQuotes(quotes: MutableList<Quote>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        quotes.forEach { quote -> // For each quote in quotes (quote is the object the for loop is looking at)
            Text(
                text = "Quote: ${quote.quote}\nAuthor: ${quote.author}",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp) // Add padding for better readability
            )
        }
    }
}
