package com.orthodoxquotesapp.quoteapp.composables.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.orthodoxquotesapp.quoteapp.dataclasses.Quote
import com.orthodoxquotesapp.quoteapp.sharedpreferencesmanagers.LocalQuoteManager

@Composable
fun AddQuoteButton(onAddQuote: (Quote) -> Unit, modifier: Modifier = Modifier) {

    var isDialogOpen by remember { mutableStateOf(false) }
    var author by remember { mutableStateOf("") }
    var quote by remember { mutableStateOf("") }
    val context = LocalContext.current

    Button(
        modifier = modifier
            .size(55.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        onClick = {
            isDialogOpen = true
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add quote",
            modifier = Modifier
                .size(36.dp)
        )
    }


    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text("Add Quote") },
            text = {
                Column {
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Author") },
                        placeholder = { Text("Enter author") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = quote,
                        onValueChange = { quote = it },
                        label = { Text("Quote") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.FormatQuote,
                                contentDescription = "Quote"
                            )
                        },
                        placeholder = { Text("Enter a quote") }
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            confirmButton = {
                Button(
                    onClick = {
                        if (author.isNotBlank() && quote.isNotBlank()) {
                            val newQuote = Quote(author, quote)
                            onAddQuote(newQuote) // Create the Quote object

                            // Save the updated quotes list locally
                            val updatedQuotes = LocalQuoteManager.getSavedQuotes(context)
                            updatedQuotes.add(newQuote)
                            LocalQuoteManager.saveQuotes(context, updatedQuotes)

                            isDialogOpen = false // Close the dialog
                            author = "" // Clear the text field
                            quote = "" // Clear the text field
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { isDialogOpen = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}