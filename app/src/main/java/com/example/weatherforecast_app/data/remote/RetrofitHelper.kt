package com.example.weatherforecast_app.data.remote

import com.example.weatherforecast_app.data.remote.location.LocationService
import com.example.weatherforecast_app.data.remote.weather.WeatherService
import com.example.weatherforecast_app.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private val retrofitWeather =
        Retrofit.Builder()
            .baseUrl(Constants.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val retrofitLocation =
        Retrofit.Builder()
            .baseUrl(Constants.LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    val weatherService: WeatherService = retrofitWeather.create(WeatherService::class.java)
    val locationService: LocationService = retrofitLocation.create(LocationService::class.java)
}