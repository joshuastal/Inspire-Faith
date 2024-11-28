package com.orthodoxquotesapp.quoteapp.retrofit_things

data class CalendarDay(
    val abbreviated_reading_indices: List<Int>,
    val day: Int,
    val fast_exception: Int,
    val fast_exception_desc: String,
    val fast_level: Int,
    val fast_level_desc: String,
    val feast_level: Int,
    val feast_level_description: String,
    val feasts: List<String>,
    val julian_day_number: Int,
    val month: Int,
    val pascha_distance: Int,
    val readings: List<Reading>,
    val saints: List<String>,
    val service_notes: List<String>,
    val stories: List<Story>,
    val summary_title: String,
    val titles: List<String>,
    val tone: Int,
    val weekday: Int,
    val year: Int
)