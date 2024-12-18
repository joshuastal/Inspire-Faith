package com.orthodoxquotesapp.quoteapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orthodoxquotesapp.quoteapp.retrofit_things.CalendarViewModel
import com.orthodoxquotesapp.quoteapp.retrofit_things.Reading

@Composable
fun ReadingsScreen() {
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
            calendarDay.readings.forEachIndexed { index, reading ->
                ReadingItem(reading)

                // Add space only between items, not after the last item
                if (index < calendarDay.readings.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Log.d("Saints", "Stories: ${calendarDay.stories}") // Returns a list of Stories
            Log.d("Saints", "Saints: ${calendarDay.saints}") // Returns a list of Saints who's indexes correspond to the story index
        }
    }
}

@Composable
fun ReadingItem(reading: Reading) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Display book and short display


        val gospels = listOf("Matthew", "Mark", "Luke", "John")

        val readingType = if (reading.book in gospels) "Gospel" else "Epistle"



        val originalReference = "${reading.display} (KJV) (${readingType})"
        val reference = originalReference.replace(".", ":")

        Text(
            text = reference,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp
        )

        // Concatenate all passage contents into a single string
        val passagesText = reading.passage.joinToString(separator = " ") { passage ->
            passage.content
        }

        // Display all passages as a single block of text
        Text(
            passagesText,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 28.sp),
            textAlign = TextAlign.Justify,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}



