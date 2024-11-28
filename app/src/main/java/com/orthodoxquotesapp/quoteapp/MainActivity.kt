package com.orthodoxquotesapp.quoteapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.orthodoxquotesapp.quoteapp.dataclasses.BottomNavigationItem
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote
import com.orthodoxquotesapp.quoteapp.dataclasses.TabItem
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.FavoritesManager
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.LocalQuoteManager
import com.orthodoxquotesapp.quoteapp.theme.QuoteAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit




class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FavoritesManager.init(this)

        var isQuotesLoaded by mutableStateOf(false)

        installSplashScreen().setKeepOnScreenCondition { !isQuotesLoaded }
        enableEdgeToEdge()

        // RETROFIT

        // RETROFIT


        setContent {
            QuoteAppTheme {
                val navController = rememberNavController()

                // Scaffold with persistent BottomNavigationBar
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController, bottomNavBarItems, modifier =
                        Modifier
                            .clip(RoundedCornerShape(25.dp))
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {
                        Navigation(
                            navController = navController,
                            onComplete = { isQuotesLoaded = true }
                        )
                    }
                }
            }
        }
    }
}

var bottomNavBarItems = listOf(
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavigationItem(
        title = "Readings",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.Book
    ),
)

@Composable
fun BottomNavigationBar(navController: NavController, bottomNavBarItems: List<BottomNavigationItem>, modifier: Modifier = Modifier) {
    // Get the current route from the navController
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // Track selected item based on the current route
    var selectedIndex by rememberSaveable { mutableStateOf(0) } // Default to index 0

    // Find the index of the currently active screen
    bottomNavBarItems.forEachIndexed { index, item ->
        if (item.title == currentRoute) {
            selectedIndex = index // Update the selected index if current route matches the title
        }
    }

    // Build the NavigationBar
    NavigationBar (modifier = modifier) {
        bottomNavBarItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    if (selectedIndex != index) {
                        selectedIndex = index
                        navController.navigate(item.title) {
                            // Prevent multiple copies of the same destination in the back stack
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selectedIndex == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        modifier = Modifier
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}

@Composable
fun MainScreen(
    onComplete: () -> Unit,
    navController: NavController,
    favoritesPagerState: PagerState,
    modifier: Modifier = Modifier
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
                                navController,
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


@Composable
fun QuoteCard(quote: Quote, modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        OutlinedCard(
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
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
                    Text( // QUOTE BODY
                        text = "\"${quote.quote}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontSize = 23.sp,
                        lineHeight = 32.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text( // AUTHOR
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
    val context = LocalContext.current
    val shareText = "\"${quote.quote}\" - ${quote.author}" // Format the quote and author

    IconButton(onClick = {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, null))
    }) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
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
    val context = LocalContext.current

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
            modifier = Modifier
                .size(36.dp)
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
                            val newQuote = Quote(author, quote)
                            onAddQuote(newQuote) // Create the Quote object

                            // Save the updated quotes list locally
                            val updatedQuotes = LocalQuoteManager.getSavedQuotes(context)
                            updatedQuotes.add(newQuote)
                            LocalQuoteManager.saveQuotes(context, updatedQuotes)

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

@Composable
fun TooTopButton(pagerState: PagerState, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope() // Remember a coroutine scope

    Button(
        modifier = modifier
            .size(55.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(0) // Scroll with animation
            }
        },

        ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "Go to first page",
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun DebugButtons(
    allQuotes: List<Quote>,
    localQuotes: SnapshotStateList<Quote>,
    context: Context,
){
    Column {
        Row {

            Button(
                onClick = {
                    FavoritesManager.getFavorites()
                },
                modifier = Modifier

            ) {
                Text("Get Favorites")
            }

            Spacer(modifier = Modifier.width(4.dp)) // Add some space between the buttons

            Button(
                onClick = {
                    FavoritesManager.clearFavorites()
                },
                modifier = Modifier

            ) {
                Text("Clear Favorites")
            }

            Spacer(modifier = Modifier.width(4.dp)) // Add some space between the buttons

            Button(
                onClick = {
                    allQuotes.forEach { quote ->
                        Log.d("MainScreen", "MainScreen: ${quote.quote}, Author: ${quote.author}")
                    }
                    Log.d("MainScreen", "MainScreen: " + allQuotes.size)
                })
            {
                Text("Get Quotes")
            }
        }

        Row {
            Button(
                onClick = {

                    if(!localQuotes.isEmpty()){
                        localQuotes.clear()
                        Log.d("MainScreen", "localQuotes cleared, size is now: " + localQuotes.size)
                        LocalQuoteManager.saveQuotes(context, localQuotes)
                    } else
                        Log.d("MainScreen", "localQuotes is already empty...")
                }
            ) {
                Text("Clear Local Quotes")
            }

            Spacer(modifier = Modifier.width(4.dp)) // Add some space between the buttons

            Button(modifier = Modifier,
                onClick = {
                    localQuotes.forEach { quote ->
                        Log.d("MainScreen", "LocalQuotes : ${quote.quote}, Author: ${quote.author}")
                    }
                    if(localQuotes.isEmpty())
                        Log.d("MainScreen", "localQuotes is empty...")
                }
            ) {
                Text("Get Local Quotes")
            }
        }
        Row {

        }

    }
}
