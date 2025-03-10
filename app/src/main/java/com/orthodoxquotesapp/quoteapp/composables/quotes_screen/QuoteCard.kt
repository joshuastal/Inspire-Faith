package com.orthodoxquotesapp.quoteapp.composables.quotes_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orthodoxquotesapp.quoteapp.theme.extendedColors
import com.orthodoxquotesapp.quoteapp.composables.buttons.FavoriteIconButton
import com.orthodoxquotesapp.quoteapp.composables.buttons.ShareIconButton
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote

@Composable
fun QuoteCard(quote: Quote, modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        OutlinedCard(
            border = BorderStroke(1.5f.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.extendedColors.cardBackground, // Add a fill color
            ),
            modifier = Modifier
                .wrapContentHeight() // Wrap the height to fit the content
                .fillMaxWidth() // Fill the width but allow height to adjust
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Add padding to the card content
                ) {
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