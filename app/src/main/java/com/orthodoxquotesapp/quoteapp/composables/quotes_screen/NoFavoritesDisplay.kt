package com.orthodoxquotesapp.quoteapp.composables.quotes_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoFavoritesDisplay(modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text( // QUOTE BODY
            text = "No favorites yet...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}