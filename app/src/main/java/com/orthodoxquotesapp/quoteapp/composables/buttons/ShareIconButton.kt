package com.orthodoxquotesapp.quoteapp.composables.buttons

import android.content.Intent
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote

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