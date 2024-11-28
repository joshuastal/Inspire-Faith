package com.orthodoxquotesapp.quoteapp.retrofit_things

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://orthocal.info/") // Base URL for the API
            .addConverterFactory(GsonConverterFactory.create()) // Convert JSON to Kotlin data classes
            .build()
    }
}