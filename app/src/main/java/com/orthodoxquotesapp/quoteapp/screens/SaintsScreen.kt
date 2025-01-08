package com.orthodoxquotesapp.quoteapp.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orthodoxquotesapp.quoteapp.composables.readings_screen.SaintItem
import com.orthodoxquotesapp.quoteapp.retrofit_things.CalendarViewModel

@Composable
fun SaintsScreen() {
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
            calendarDay.stories.forEachIndexed { _, story ->
                SaintItem(story)
            }
            Log.d("Saints", "Stories: ${calendarDay.stories}") // Returns a list of Stories
        }
    }
}







