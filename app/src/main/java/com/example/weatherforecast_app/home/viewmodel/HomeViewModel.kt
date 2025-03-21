package com.example.weatherforecast_app.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast_app.WeatherDtoResponse
import com.example.weatherforecast_app.WeatherResponseState
import com.example.weatherforecast_app.data.model.Coordinate
import com.example.weatherforecast_app.data.repo.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(private val repo: IWeatherRepository): ViewModel() {

    private val mutableWeatherData: MutableStateFlow<WeatherDtoResponse> = MutableStateFlow(WeatherDtoResponse.Loading)
    val weatherData = mutableWeatherData.asStateFlow()

    private val _locationStateFlow = MutableStateFlow<Coordinate?>(null)
    val locationStateFlow = _locationStateFlow.asStateFlow()

    fun updateLocation(latitude: Double, longitude: Double) {
        Log.i(TAG, "updateLocation: latitude = $latitude longitude =$longitude")
        _locationStateFlow.value = Coordinate(latitude, longitude)
    }

    fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String = "en"
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getCurrentWeather(latitude, longitude)
            result
                .catch {
                    ex->
                    Log.i(TAG, "getCurrentWeather: ${ex.message}")
                    mutableWeatherData.value = WeatherDtoResponse.Failure(ex)
                }
                .collect{
                    mutableWeatherData.value = WeatherDtoResponse.Success(it)
                }
        }

    }
}



class HomeFactory(private val repo: IWeatherRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}