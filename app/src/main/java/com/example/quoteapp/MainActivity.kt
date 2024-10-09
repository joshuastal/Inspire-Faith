package com.example.quoteapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.quoteapp.ui.theme.QuoteAppTheme



//private lateinit var quotes: MutableList<Quote>
private lateinit var  quotes: SnapshotStateList<Quote>
private lateinit var firebaseService: FirebaseService
private lateinit var quoteService: QuoteService

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Incorporate the splash screen and ensure it stays on screen until quotes are loaded
        var isQuotesLoaded by mutableStateOf(false)
        installSplashScreen().apply { setKeepOnScreenCondition { !isQuotesLoaded} }
        enableEdgeToEdge()


        // Initialize the services and the quotes list
        quotes = mutableStateListOf<Quote>()
        firebaseService = FirebaseService()
        quoteService = QuoteService()

        // This is the step that actually makes the list contain the quotes
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(quotes: MutableList<Quote>, modifier: Modifier = Modifier) {
    MaterialTheme(
    ) {
        Box(modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)

        ) {
                //DisplayAllQuotes(quotes = quotes)
            val pagerState = rememberPagerState(pageCount = {
                quotes.size
            })

            VerticalPager(
                state = pagerState
            ) { page ->
                // Display a single quote for each page
                QuoteCard(quote = quotes[page], modifier = Modifier.fillMaxSize())
            }
        }
    }
}


@Composable
fun DisplayAllQuotes(quotes: MutableList<Quote>, modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
    ) {
        quotes.forEach { quote -> // For each quote in quotes (quote is the object the for loop is looking at)
            Text(
                text = "Quote: ${quote.quote}\nAuthor: ${quote.author}",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp) // Add padding for better readability
            )
        }
    }
}

@Composable
fun QuoteCard(quote: Quote, modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
           .padding(16.dp)
           .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(text = quote.quote,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontSize = 17.sp,
                modifier = Modifier
            )
            Text(
                text = quote.author,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier

            )
        }
    }
}

