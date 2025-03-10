package com.orthodoxquotesapp.quoteapp.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.orthodoxquotesapp.quoteapp.composables.ToBeImplemented
import com.orthodoxquotesapp.quoteapp.objects.QuoteData
import com.orthodoxquotesapp.quoteapp.services.FirebaseService
import com.orthodoxquotesapp.quoteapp.services.QuoteService
import com.orthodoxquotesapp.quoteapp.screens.homeUtilities.loadQuotesAndSignalCompletion
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.LocalQuoteManager
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onComplete: () -> Unit
){
    ToBeImplemented("Home Screen")

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        loadQuotesAndSignalCompletion(context, onComplete)
    }

}

