package com.example.quoteapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.quoteapp.ui.theme.QuoteAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FavoritesManager.init(this)

        var isQuotesLoaded by mutableStateOf(false)

        // Incorporate the splash screen
        installSplashScreen().setKeepOnScreenCondition{ !isQuotesLoaded }
        enableEdgeToEdge()

        setContent {
            QuoteAppTheme {
                MainScreen(onComplete = { isQuotesLoaded = true })
            }
        }
    }



}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(onComplete: () -> Unit ,modifier: Modifier = Modifier) {

    val firebaseService = FirebaseService()
    val quoteService = QuoteService()
    val quotes: SnapshotStateList<Quote> = remember { mutableStateListOf() }

    LaunchedEffect(Unit) {
        // Retrieve quotes asynchronously
        quoteService.retrieveQuotes(quotes, firebaseService)

        // Wait until quotes are loaded
        while (quotes.isEmpty()) {
            delay(1) // Check every 100ms if quotes are loaded
        }

        // Call onComplete after quotes are loaded
        onComplete()
    }


    val pagerState = rememberPagerState(pageCount = { quotes.size })
    val coroutineScope = rememberCoroutineScope()

    MaterialTheme(
    ) {
        Box(modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            VerticalPager(
                state = pagerState,
                userScrollEnabled = true,
                modifier = Modifier
            ) { page ->
                // Display a single quote for each page
                QuoteCard(quote = quotes[page], modifier = Modifier.fillMaxSize())
            }

            Row(

            ) {

                Button(
                    onClick = {
                        FavoritesManager.getFavorites()
                    },
                    modifier = Modifier

                ) {
                    Text("Get Favorites")
                }

                Spacer(modifier = Modifier.width(16.dp)) // Add some space between the buttons

                Button(
                    onClick = {
                        FavoritesManager.clearFavorites()
                    },
                    modifier = Modifier

                ) {
                    Text("Clear Favorites")
                }

                Spacer(modifier = Modifier.width(16.dp)) // Add some space between the buttons

                Button(
                    onClick = {
                        coroutineScope.launch {
                            // Call scroll to on pagerState
                            pagerState.scrollToPage(quotes.lastIndex - 1)
                        }
                    })
                {
                    Text("2nd to last")
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                AddQuoteButton(
                    { newQuote ->
                    // Add the new quote to the quotes list
                        quotes.add(newQuote)
                        Log.d("AddButton", "New Quote Added...")
                        Log.d("AddButton", "New quotes size: " + quotes.size)
                        quotes.forEach { quote ->
                            Log.d("AddButton", "AddButton: ${quote.quote}, Author: ${quote.author}")
                        }

                    }, modifier = Modifier
                    .align(Alignment.BottomEnd) // Align this button at the bottom right
                    .padding(end = 16.dp))// Add some padding from the edges)

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
        OutlinedCard(
            border = BorderStroke(2.dp, Color.White),
            modifier = Modifier
                .wrapContentHeight() // Wrap the height to fit the content
                .fillMaxWidth() // Fill the width but allow height to adjust
        ){
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Add padding to the card content
                ){
                    Text(
                        text = "\"${quote.quote}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Text(
                        text = quote.author,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp)) // Space between author and button
                    Row {
                        ShareIconButton(quote = quote)

                        Spacer(modifier = Modifier.width(20.dp)) // Space between share and favorite

                        FavoriteIconButton(quote = quote)
                    }
                }
            }
        }
    }
}


@Composable
fun FavoriteIconButton(quote: Quote) {
    // Store the initial state of whether the quote is favorited or not
    var isFavorited by remember { mutableStateOf(false) }

    // Use a side effect to get the favorite status initially and update the state accordingly
    LaunchedEffect(quote) {
        isFavorited = FavoritesManager.isFavorite(quote)
    }

    IconButton(
        onClick = {
            if (isFavorited) {
                FavoritesManager.removeFromFavorites(quote)
                FavoritesManager.getFavorites()
                Log.d("Favorites", "Favorite removed: ${quote.quote}, Author: ${quote.author}")

            } else {
                FavoritesManager.addToFavorites(quote)
                FavoritesManager.getFavorites()
                Log.d("Favorites", "Favorite added: ${quote.quote}, Author: ${quote.author}")
            }
            isFavorited = !isFavorited // Toggle the local state
        }
    ) {
        Icon(
            imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorited) "Unfavorite" else "Favorite",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ShareIconButton(quote: Quote){
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    IconButton(
        onClick = {
            // Create the text to copy
            val textToCopy = "\"${quote.quote}\"\n- ${quote.author}"
            // Copy the text to clipboard
            clipboardManager.setText(AnnotatedString(textToCopy))
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
        }
    ) {
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy quote",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
fun AddQuoteButton(onAddQuote: (Quote) -> Unit, modifier: Modifier = Modifier) {

    var isDialogOpen by remember { mutableStateOf(false) }
    var author by remember { mutableStateOf("") }
    var quote by remember { mutableStateOf("") }

        Button(
            modifier = modifier
                .size(55.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            onClick = {
                isDialogOpen = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add quote",
                modifier = Modifier.size(36.dp)
            )
        }


    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text("Add Quote") },
            text = {
                Column {
                    TextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = quote,
                        onValueChange = { quote = it },
                        label = { Text("Quote") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (author.isNotBlank() && quote.isNotBlank()) {
                            onAddQuote(Quote(author, quote)) // Create the Quote object
                            isDialogOpen = false // Close the dialog
                            author = "" // Clear the text field
                            quote = "" // Clear the text field
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { isDialogOpen = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

