package com.example.weatherforecast_app.favorite_weather_details.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast_app.data.repo.user_pref.IUserPreferenceRepository
import com.example.weatherforecast_app.data.repo.weather_repo.IWeatherRepository
import com.example.weatherforecast_app.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


private const val TAG = "FavoriteDetailsViewMode"

class FavoriteDetailsViewModel(
    private val weatherRepo: IWeatherRepository,
    private val userPrefRepo: IUserPreferenceRepository
): ViewModel() {
    private val mutableCurrentWeather: MutableStateFlow<ResponseState> = MutableStateFlow(
        ResponseState.Loading)
    val currentWeatherData = mutableCurrentWeather.asStateFlow()

    private val mutableWeeklyWeather: MutableStateFlow<ResponseState> = MutableStateFlow(
        ResponseState.Loading)
    val weeklyWeatherData = mutableWeeklyWeather.asStateFlow()


    fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String = "en",
        tempUnit: String?
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val result = weatherRepo.getCurrentWeather(latitude, longitude, language, tempUnit)
            result
                .catch {
                        ex->
                    Log.i(TAG, "getCurrentWeather: ${ex.message}")
                    mutableCurrentWeather.value = ResponseState.Failure(ex)
                }
                .collect{
                    mutableCurrentWeather.value = ResponseState.Success(it)
                }
        }

    }

    fun getWeeklyWeather(
        latitude: Double,
        longitude: Double,
        language: String = "en",
        tempUnit: String?
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val result = weatherRepo.getWeatherForFiveDays(latitude, longitude, language, tempUnit)
            result.catch {
                    ex->
                Log.i(TAG, "getWeeklyWeather: $ex")
                mutableWeeklyWeather.value = ResponseState.Failure(ex)
            }.collect{
                mutableWeeklyWeather.value = ResponseState.Success(it)
                Log.i(TAG, "getWeeklyWeather: ${it.weatherDTOList}")
            }
        }
    }


    fun getTemperatureUnitPref(): String?{

        val temperatureUnitPref = when(userPrefRepo.getTemperatureUnit()){
            "Celsius" -> "metric"
            "Fahrenheit" -> "imperial"
            else -> null
        }
        return temperatureUnitPref
    }

    fun getWindUnitPref(): String?{
        return userPrefRepo.getWindSpeedUnit()
    }

}

class FavoriteDetailsFactory(
    private val weatherRepo: IWeatherRepository,
    private val userPrefRepo: IUserPreferenceRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteDetailsViewModel(weatherRepo, userPrefRepo) as T
    }
}