package com.orthodoxquotesapp.quoteapp.composables

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToBeImplemented(screen: String, icon: ImageVector){
    Column(
        modifier = Modifier.fillMaxSize(), // Makes Column take the full screen
        verticalArrangement = Arrangement.Center, // Centers vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$icon",
            modifier = Modifier.size(64.dp)
        )

        Spacer(Modifier.size(12.dp))

        Text(
            "$screen to be implemented...",
            fontSize = 24.sp
        )

    }
}