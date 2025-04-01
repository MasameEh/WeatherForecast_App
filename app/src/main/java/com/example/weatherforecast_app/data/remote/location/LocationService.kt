package com.example.weatherforecast_app.data.remote.location


import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.model.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {

    @GET("search")
    suspend fun searchLocationByName(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 5
    ): List<LocationInfo>

    @GET("reverse")
    suspend fun searchLocationByCoordinate(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("format") format: String = "geojson",
        @Query("accept-language") language: String = "en"
    ): LocationResponse
}