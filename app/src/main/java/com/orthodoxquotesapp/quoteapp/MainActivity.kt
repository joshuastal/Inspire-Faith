package com.orthodoxquotesapp.quoteapp

import android.app.AlarmManager
import android.app.PendingIntent
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
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.orthodoxquotesapp.quoteapp.alarmmanager.AlarmReceiver
import com.orthodoxquotesapp.quoteapp.dataclasses.BottomNavigationItem
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote
import com.orthodoxquotesapp.quoteapp.dataclasses.TabItem
import com.orthodoxquotesapp.quoteapp.screens.FavoritesScreen
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.FavoritesManager
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.LocalQuoteManager
import com.orthodoxquotesapp.quoteapp.theme.QuoteAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FavoritesManager.init(this)

        var isQuotesLoaded by mutableStateOf(false)

        installSplashScreen().setKeepOnScreenCondition { !isQuotesLoaded }
        enableEdgeToEdge()

        scheduleAlarm()

        setContent {
            QuoteAppTheme {
                val navController = rememberNavController()

                // Scaffold allows for persistent BottomNavigationBar
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


    // Schedule the daily notification
    private fun scheduleAlarm(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 1)
            set(Calendar.MINUTE, 48)
            set(Calendar.SECOND, 0)
        }

        Log.d("DailyNotification", "Scheduling alarm for: ${calendar.time}") // Add this log

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            60000,  // time window (1 minute)
            pendingIntent
        )
    }
}


// Different screens in the app
var bottomNavBarItems = listOf(
    BottomNavigationItem(
        title = "Readings",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.Book
    ),
    BottomNavigationItem(
        title = "Quotes",
        selectedIcon = Icons.Filled.FormatQuote,
        selectedIconSize = 24.dp,
        unselectedIcon = Icons.Outlined.FormatQuote,
    ),
    BottomNavigationItem(
        title = "Saints",
        selectedIcon = Icons.Filled.Group,
        unselectedIcon = Icons.Outlined.Group
    )
)

@Composable
fun BottomNavigationBar(
    navController: NavController,
    bottomNavBarItems: List<BottomNavigationItem>,
    modifier: Modifier = Modifier
) {
    // Get the current route from the navController
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // Track selected item based on the current route
    var selectedIndex by rememberSaveable { mutableStateOf(1) } // Default to index 0

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
                    val isSelected = selectedIndex == index
                    val size = if (isSelected) item.selectedIconSize else item.unselectedIconSize
                    val adjustedSize = if (isSelected) {
                        size * 1.2f // Scale up the filled icon slightly
                    } else {
                        size
                    }
                    val containerSize = 30.dp // Fixed size for the container
                    Box(
                        modifier = Modifier
                            .size(containerSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            modifier = if (size != Dp.Unspecified) Modifier.size(adjustedSize) else Modifier
                        )
                    }
                },
                label = { Text(item.title) }
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
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Author") },
                        placeholder = { Text("Enter author") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = quote,
                        onValueChange = { quote = it },
                        label = { Text("Quote") },
                        leadingIcon = { Icon(Icons.Default.FormatQuote, contentDescription = "Quote") },
                        placeholder = { Text("Enter a quote") }
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
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
