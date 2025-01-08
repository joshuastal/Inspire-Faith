package com.orthodoxquotesapp.quoteapp.composables.readings_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

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