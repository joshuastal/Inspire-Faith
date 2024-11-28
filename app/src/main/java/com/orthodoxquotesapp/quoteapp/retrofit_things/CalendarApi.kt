package com.orthodoxquotesapp.quoteapp.retrofit_things


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CalendarApi {

    // Define the endpoint for the Gregorian calendar
    @GET("api/{cal}/")
    suspend fun getCalendarData(
        @Path("cal") cal: String // Replaces {cal} with "gregorian"
    ): Response<CalendarDay>  // Assuming the response is a list of CalendarDay objects
}