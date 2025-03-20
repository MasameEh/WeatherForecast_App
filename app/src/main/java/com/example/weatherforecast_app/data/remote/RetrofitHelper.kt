package com.example.weatherforecast_app.data.remote

import com.example.weatherforecast_app.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val Instance =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val weatherService: WeatherService = Instance.create(WeatherService::class.java)
}