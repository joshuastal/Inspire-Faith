package com.orthodoxquotesapp.quoteapp.retrofit_things

data class Passage(
    val book: String,
    val chapter: Int,
    val content: String,
    val paragraph_start: Boolean,
    val verse: Int
)