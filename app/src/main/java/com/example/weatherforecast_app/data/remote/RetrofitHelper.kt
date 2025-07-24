package com.example.weatherforecast_app.data.remote

import com.example.weatherforecast_app.data.remote.location.LocationService
import com.example.weatherforecast_app.data.remote.weather.WeatherService
import com.example.weatherforecast_app.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okhttp = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "WeatherApp/1.0 (samehms116@gmail.com)")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofitWeather =
        Retrofit.Builder()
            .baseUrl(Constants.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp)
            .build()

    private val retrofitLocation =
        Retrofit.Builder()
            .baseUrl(Constants.LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp)
            .build()


    val weatherService: WeatherService = retrofitWeather.create(WeatherService::class.java)
    val locationService: LocationService = retrofitLocation.create(LocationService::class.java)
}