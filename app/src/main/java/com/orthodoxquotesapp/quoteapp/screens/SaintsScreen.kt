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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
        val wordsToRemove = listOf("Our", "Venerable", "Holy", "Righteous" )
        val storyTitle = story.title
            .split(" ")
            .filterNot { word -> wordsToRemove.contains(word) }
            .joinToString(" ")

        var dynamicFontSize by remember { mutableStateOf(21.sp) }
        var isFontAdjusted by remember { mutableStateOf(false) }

        Text(
            text = storyTitle
                .replace("Saint", "St.")
                .replace("Father", "Fr."),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            fontSize = dynamicFontSize,
            textAlign = TextAlign.Center,
            onTextLayout = { layoutResult ->
                if (!isFontAdjusted) {
                    if (layoutResult.lineCount > 1 && dynamicFontSize != 18.5.sp) {
                        dynamicFontSize = 18.5.sp
                        isFontAdjusted = true
                    } else if (layoutResult.lineCount <= 1 && dynamicFontSize != 21.sp) {
                        dynamicFontSize = 21.sp
                        isFontAdjusted = true
                    }
                }
            },
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
        .replace("<p>", "")
        .replace("<i>", "")
        .replace("</i>", "")
    // Remove opening paragraph tag (optional)

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







