package com.orthodoxquotesapp.quoteapp.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.orthodoxquotesapp.quoteapp.composables.ToBeImplemented
import com.orthodoxquotesapp.quoteapp.objects.QuoteData
import com.orthodoxquotesapp.quoteapp.retrofit_things.CalendarViewModel
import com.orthodoxquotesapp.quoteapp.screens.homeUtilities.loadAllDataThenSignalCompletion
import com.orthodoxquotesapp.quoteapp.services.FirebaseService
import com.orthodoxquotesapp.quoteapp.services.QuoteService
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.LocalQuoteManager
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onComplete: () -> Unit,
    navController: NavController // Add this parameter
){
    val context = LocalContext.current

    Scaffold(
        topBar = {
            // Settings button on top of screen
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Existing content wrapped in a Box with padding
        Box(modifier = Modifier.padding(paddingValues)) {
            ToBeImplemented("Home Screen", Icons.Default.Home)
        }
    }

    LaunchedEffect(Unit) {
        loadAllDataThenSignalCompletion(context, onComplete)
    }
}

