package com.orthodoxquotesapp.quoteapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.orthodoxquotesapp.quoteapp.retrofit_things.CalendarViewModel
import com.orthodoxquotesapp.quoteapp.retrofit_things.Passage
import com.orthodoxquotesapp.quoteapp.retrofit_things.Reading

@Composable
fun ReadingsScreen(navController: NavController) {
    val calendarViewModel: CalendarViewModel = viewModel()

    // Fetch calendar data when the Composable is first launched
    LaunchedEffect(Unit) {
        calendarViewModel.fetchCalendarData()
    }

    val calendarData = calendarViewModel.calendarData.value

    LazyColumn(
        modifier = Modifier.padding(8.dp) // Add some padding if needed
    ) {
        items(calendarData) { calendarDay ->
            calendarDay.readings?.forEach { reading ->
                ReadingItem(reading)
            }
        }
    }
}

@Composable
fun ReadingItem(reading: Reading) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Display book and short display

        val originalReference = reading.short_display
        val reference         = originalReference.replace(".", ":")

        Text(
            reference,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        // Concatenate all passage contents into a single string
        val passagesText = reading.passage.joinToString(separator = " ") { passage ->
            passage.content
        }

        // Display all passages as a single block of text
        Text(
            passagesText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun PassageItem(passage: Passage) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        // Display each passage's content
        Text(passage.content)
    }
}
