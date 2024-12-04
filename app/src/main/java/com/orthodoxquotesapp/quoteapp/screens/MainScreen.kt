package com.orthodoxquotesapp.quoteapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orthodoxquotesapp.quoteapp.AddQuoteButton
import com.orthodoxquotesapp.quoteapp.FirebaseService
import com.orthodoxquotesapp.quoteapp.QuoteCard
import com.orthodoxquotesapp.quoteapp.QuoteService
import com.orthodoxquotesapp.quoteapp.TooTopButton
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote
import com.orthodoxquotesapp.quoteapp.dataclasses.TabItem
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.LocalQuoteManager
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    onComplete: () -> Unit,
    navController: NavController,
    favoritesPagerState: PagerState,
) {

    // UTILITIES
    val firebaseService = FirebaseService()
    val quoteService = QuoteService()
    val context = LocalContext.current
    // UTILITIES


    // QUOTES MANAGEMENT
    val quotes: SnapshotStateList<Quote> = remember { mutableStateListOf() }
    val localQuotes: SnapshotStateList<Quote> = remember { mutableStateListOf() }
    val allQuotes = remember { mutableStateListOf<Quote>() }
    // QUOTES MANAGEMENT


    var isLoading by remember { mutableStateOf(true) } // Loading state for Firestore quotes and splashscreen
    LaunchedEffect(Unit) {

        // Load local quotes from SharedPreferences
        val savedQuotes = LocalQuoteManager.getSavedQuotes(context)

        // Ensure no duplicates are added
        savedQuotes.forEach { savedQuote ->
            if (!localQuotes.contains(savedQuote)) {
                localQuotes.add(savedQuote)
            }
        }

        // Retrieve quotes asynchronously
        quoteService.retrieveQuotes(quotes, firebaseService)

        // Wait until quotes are loaded
        while (quotes.isEmpty()) {
            delay(1) // Check every 1ms if quotes are loaded
        }

        // Combine and shuffle quotes only once
        if (allQuotes.isEmpty()) {
            allQuotes.addAll(localQuotes + quotes)
            allQuotes.shuffle()
        }

        isLoading = false // Remote quotes are loaded, so no more loading state

        // Call onComplete after quotes are loaded
        onComplete()
    } // Splashscreen conditions

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabItems = listOf(
        TabItem("Home"),
        TabItem("Favorites")
    )

    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    val verticalPagerState = rememberPagerState(pageCount = {allQuotes.size})

    LaunchedEffect(selectedTabIndex){
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage){
        selectedTabIndex = pagerState.currentPage
    }

    MaterialTheme {
        // Handles the screens and navigation between them
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> {
                    // VerticalPager for quotes
                    VerticalPager(
                        state = verticalPagerState,
                        userScrollEnabled = true,
                    ) { verticalPage ->
                        QuoteCard(quote = allQuotes[verticalPage], modifier = Modifier.fillMaxSize())
                    }
                }
                1 -> {
                    // Your Favorites screen placeholder
                    FavoritesScreen(
                        pagerState = favoritesPagerState
                    )
                }
            }
        }

        // Tab navigation bar
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(item.title) },
                )
            }
        }

        // DEBUG BUTTONS //
        //DebugButtons(allQuotes, localQuotes, context)
        // DEBUG BUTTONS //

        // TooTop and AddQuote Buttons
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
        ) {
            AddQuoteButton(
                { newQuote ->
                    // Add the new quote to the quotes list
                    localQuotes.add(newQuote)
                    allQuotes.add(newQuote)
                    LocalQuoteManager.saveQuotes(context, localQuotes)


                    Log.d("AddButton", "New Quote Added...")
                    Log.d("AddButton", "New quotes size: " + quotes.size)
                    quotes.forEach { quote ->
                        Log.d("AddButton", "AddButton: ${quote.quote}, Author: ${quote.author}")
                    }

                }, modifier = Modifier
                    .align(Alignment.BottomEnd) // Align this button at the bottom right
                    .padding(end = 8.dp)
            )

            TooTopButton(
                pagerState = if (selectedTabIndex == 0) verticalPagerState else favoritesPagerState,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 8.dp)
            )
        }
    }
}