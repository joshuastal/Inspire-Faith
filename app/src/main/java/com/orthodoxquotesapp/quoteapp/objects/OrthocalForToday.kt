package com.orthodoxquotesapp.quoteapp.objects

import androidx.compose.runtime.State
import com.orthodoxquotesapp.quoteapp.retrofit_things.CalendarDay
import com.orthodoxquotesapp.quoteapp.retrofit_things.CalendarViewModel

object OrthocalForToday {
    private val calendarViewModel = CalendarViewModel()

    // Expose the State object itself, not its initial value
    val calendarData: State<List<CalendarDay>> = calendarViewModel.calendarData

    fun loadData() {
        calendarViewModel.fetchTodayCalendarData()
    }
}