package com.example.weatherforecast_app.data.repo.weather_repo

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.data.remote.weather.IWeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImp @Inject constructor(
    private val remoteDataSource: IWeatherRemoteDataSource
) : IWeatherRepository {

    override fun getWeatherForFiveDays(
        latitude: Double,
        longitude: Double,
        language: String,
        tempUnit: String?
    ): Flow<WeatherResponse> {
        return remoteDataSource.getWeatherForFiveDays(latitude, longitude, language, tempUnit)
    }

    override fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        tempUnit: String?
    ): Flow<WeatherDTO> {
        return remoteDataSource.getCurrentWeather(latitude, longitude, language, tempUnit)
    }


    companion object{
        //Without volatile, a thread could cache instance locally and
        // keep checking the old value even if another thread already updated it.
        @Volatile
        private var instance: WeatherRepositoryImp? = null

        fun getInstance(remoteDataSource: IWeatherRemoteDataSource): WeatherRepositoryImp {
            //only one thread can enter the block at a time.
            return instance ?: synchronized(this) {
                instance ?: WeatherRepositoryImp(remoteDataSource) //  Double-Checked Locking pattern
                    .also {
                    instance = it
                }
            }
        }

    }
}