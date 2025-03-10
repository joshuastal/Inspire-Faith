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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Church
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.House
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.orthodoxquotesapp.quoteapp.alarmmanager.AlarmReceiver
import com.orthodoxquotesapp.quoteapp.composables.BottomNavigationBar
import com.orthodoxquotesapp.quoteapp.dataclasses.BottomNavigationItem
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.FavoritesManager
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.LocalQuoteManager
import com.orthodoxquotesapp.quoteapp.theme.QuoteAppTheme
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
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 5)
            set(Calendar.SECOND, 0)
        }

        // If the time has passed for today, schedule it for tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
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
        title = "Quotes",
        selectedIcon = Icons.Filled.FormatQuote,
        selectedIconSize = 24.dp,
        unselectedIcon = Icons.Outlined.FormatQuote,
    ),
    BottomNavigationItem(
        title = "Calendar",
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth
    ),
    BottomNavigationItem(
      title = "Home",
        selectedIcon = Icons.Filled.House,
        unselectedIcon = Icons.Outlined.House
    ),
    BottomNavigationItem(
        title = "Prayers",
        selectedIcon = Icons.Filled.Church,
        unselectedIcon = Icons.Outlined.Church,
    ),
    BottomNavigationItem(
        title = "Readings",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.Book
    )
)


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
