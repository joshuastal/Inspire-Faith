package com.orthodoxquotesapp.quoteapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.orthodoxquotesapp.quoteapp.retrofit_things.Story

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


@Composable
fun SaintItem(story: Story) {
    Column(modifier = Modifier.padding(8.dp)) {

        // Display saint name
        val wordsToRemove = listOf("Our", "Venerable", "Holy", )
        val storyTitle = story.title
            .split(" ")
            .filterNot { word -> wordsToRemove.contains(word) }
            .joinToString(" ")

        Text(
            text = storyTitle
                .replace("Saint", "St.")
                .replace("Father", "Fr."),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            fontSize =
                if(storyTitle.length in 52..59) 18.sp
                else 21.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )


        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp
        )

        // Display story
        Log.d("StoryTitle", "${storyTitle}: ${storyTitle.length}") // Returns a list of Stories
        DisplayStory(story.story)
    }
}

@Composable
fun DisplayStory(story: String) {
    // Replace </p> with a newline, and <p> tags can be removed
    val formattedStory = story
        .replace("</p>", "\n")  // Replace closing paragraph tag with a double newline
        .replace("<p>", "")      // Remove opening paragraph tag (optional)

    // Split the formatted story into paragraphs based on newlines
    val paragraphs = formattedStory.split("\n") // Paragraphs are now separated by new lines

    // Display the story with space between paragraphs
    Column(modifier = Modifier.fillMaxWidth()) {
        paragraphs.forEach { paragraph ->
            Text(
                text = paragraph,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 28.sp),
                textAlign = TextAlign.Justify,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}







