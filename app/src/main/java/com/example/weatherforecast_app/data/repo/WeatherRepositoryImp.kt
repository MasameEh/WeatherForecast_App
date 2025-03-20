package com.example.weatherforecast_app.data.repo

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.data.remote.IWeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImp private constructor(
    private val remoteDataSource: IWeatherRemoteDataSource
) : IWeatherRepository {

    override suspend fun getWeatherForFiveDays(
        latitude: Double,
        longitude: Double,
        language: String
    ): Flow<WeatherResponse> {
        return remoteDataSource.getWeatherForFiveDays(latitude, longitude, language)
    }

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String
    ): Flow<WeatherDTO> {
        return remoteDataSource.getCurrentWeather(latitude, longitude, language)
    }


    companion object{
        //Without volatile, a thread could cache instance locally and
        // keep checking the old value even if another thread already updated it.
        @Volatile
        private var instance: WeatherRepositoryImp? = null

        fun getInstance(remoteDataSource: IWeatherRemoteDataSource): WeatherRepositoryImp {
            //only one thread can enter the block at a time.
            return instance ?: synchronized(this) {
                instance ?: WeatherRepositoryImp(remoteDataSource)
                    .also {
                    instance = it
                }
            }
        }

    }
}