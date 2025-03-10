package com.orthodoxquotesapp.quoteapp.retrofit_things

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {

    // Define a MutableState to hold the list of calendar data
    private val _calendarData: MutableState<List<CalendarDay>> = mutableStateOf(emptyList())
    // Expose the calendar data as a read-only state
    val calendarData: MutableState<List<CalendarDay>> = _calendarData

    fun fetchTodayCalendarData() {
        viewModelScope.launch {
            try {
                val api = RetrofitInstance.retrofit.create(CalendarApi::class.java)
                val response = api.getCalendarData("gregorian") // Make the API call

                if (response.isSuccessful) {
                    // Use an empty list if response body is null
                    _calendarData.value = response.body()?.let { listOf(it) } ?: emptyList()
                    Log.d("API Response", "Calendar Data: ${_calendarData.value}")


                } else {
                    Log.e("API Error", "Error fetching data: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("API Error", "Exception: ${e.message}")
            }
        }
    }

}
