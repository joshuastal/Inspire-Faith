package com.orthodoxquotesapp.quoteapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
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
import com.orthodoxquotesapp.quoteapp.composables.buttons.AddQuoteButton
import com.orthodoxquotesapp.quoteapp.services.FirebaseService
import com.orthodoxquotesapp.quoteapp.composables.quotes_screen.QuoteCard
import com.orthodoxquotesapp.quoteapp.services.QuoteService
import com.orthodoxquotesapp.quoteapp.composables.buttons.TooTopButton
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote
import com.orthodoxquotesapp.quoteapp.dataclasses.TabItem
import com.orthodoxquotesapp.quoteapp.objects.QuoteData
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.LocalQuoteManager
import kotlinx.coroutines.delay

@Composable
fun QuotesScreen(
    navController: NavController,
    favoritesPagerState: PagerState,
) {

    // UTILITIES
    val firebaseService = FirebaseService()
    val quoteService = QuoteService()
    val context = LocalContext.current
    // UTILITIES


    // QUOTES MANAGEMENT
    val quotes = QuoteData.quotes
    val localQuotes = QuoteData.localQuotes
    val allQuotes = QuoteData.allQuotes
    // QUOTES MANAGEMENT

    val isLoading = QuoteData.isLoading.value




    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabItems = listOf(
        TabItem("Home"),
        TabItem("Favorites")
    )

    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    val verticalPagerState = rememberPagerState(pageCount = { allQuotes.size })

    LaunchedEffect(selectedTabIndex){
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage){
        selectedTabIndex = pagerState.currentPage
    }

    MaterialTheme {
        if (isLoading || allQuotes.isEmpty()) {
            // Show loading indicator
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            // Your existing UI code - no changes needed here
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                when (page) {
                    0 -> {
                        VerticalPager(
                            state = verticalPagerState,
                            userScrollEnabled = true,
                        ) { verticalPage ->
                            QuoteCard(quote = allQuotes[verticalPage], modifier = Modifier.fillMaxSize())
                        }
                    }
                    1 -> {
                        FavoritesScreen(
                            pagerState = favoritesPagerState
                        )
                    }
                }
            }

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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp)
            ) {
                AddQuoteButton(
                    { newQuote ->
                        // Update shared data
                        QuoteData.localQuotes.add(newQuote)
                        QuoteData.allQuotes.add(newQuote)
                        LocalQuoteManager.saveQuotes(context, QuoteData.localQuotes)

                        Log.d("AddButton", "New Quote Added...")
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
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
}