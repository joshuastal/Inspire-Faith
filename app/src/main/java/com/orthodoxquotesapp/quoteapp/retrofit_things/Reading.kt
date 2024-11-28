package com.orthodoxquotesapp.quoteapp.retrofit_things

data class Reading(
    val book: String,
    val description: String,
    val display: String,
    val passage: List<Passage>,
    val short_display: String,
    val source: String
)