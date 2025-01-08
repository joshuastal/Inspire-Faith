package com.orthodoxquotesapp.quoteapp.composables.readings_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orthodoxquotesapp.quoteapp.retrofit_things.Story

@Composable
fun SaintItem(story: Story) {
    Column(modifier = Modifier.padding(8.dp)) {

        // Display saint name
        val wordsToRemove = listOf("Our", "Venerable", "Holy", "Righteous")
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
                    if (layoutResult.lineCount > 1 && dynamicFontSize != 18.sp) {
                        dynamicFontSize = 18.sp
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