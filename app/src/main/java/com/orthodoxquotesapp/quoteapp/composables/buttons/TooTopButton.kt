package com.orthodoxquotesapp.quoteapp.composables.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

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