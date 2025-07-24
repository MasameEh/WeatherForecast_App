package com.example.weatherforecast_app.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast_app.utils.ResponseState
import com.example.weatherforecast_app.data.model.Coordinate
import com.example.weatherforecast_app.data.model.LocationResponse
import com.example.weatherforecast_app.data.repo.location_repo.ILocationRepository
import com.example.weatherforecast_app.data.repo.user_pref.IUserPreferenceRepository
import com.example.weatherforecast_app.data.repo.weather_repo.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val weatherRepo: IWeatherRepository,
    private val userPrefRepo: IUserPreferenceRepository,
    private val locationRepo: ILocationRepository
): ViewModel() {

    private val mutableCurrentWeather: MutableStateFlow<ResponseState> = MutableStateFlow(
        ResponseState.Loading)
    val currentWeatherData = mutableCurrentWeather.asStateFlow()

    private val mutableWeeklyWeather: MutableStateFlow<ResponseState> = MutableStateFlow(
        ResponseState.Loading)
    val weeklyWeatherData = mutableWeeklyWeather.asStateFlow()

    private val _searchedLocation = MutableStateFlow<LocationResponse?>(null)
    val searchedLocation: StateFlow<LocationResponse?> = _searchedLocation.asStateFlow()

    private val _locationStateFlow = MutableStateFlow<Coordinate?>(null)
    val locationStateFlow = _locationStateFlow.asStateFlow()

    fun updateLocation(latitude: Double, longitude: Double) {
        Log.i(TAG, "updateLocation: latitude = $latitude longitude =$longitude")
        _locationStateFlow.value = Coordinate(latitude, longitude)
    }

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


    fun searchLocationByCoordinate(latitude: Double, longitude: Double, language: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = locationRepo.searchLocationByCoordinate(latitude, longitude,language)
            result.catch { e->
                Log.i(TAG, "searchLocationByCoordinate: ${e.message}")
            }.collect{
                _searchedLocation.value = it

            }
        }
    }

    fun getTemperatureUnitPref(): String?{

         val temperatureUnitPref = when(userPrefRepo.getTemperatureUnit()){
             "Celsius" -> "metric"
             "Fahrenheit" -> "imperial"
             "Kelvin" -> "Kelvin"
             else -> null
        }
        return temperatureUnitPref
    }

    fun getWindUnitPref(): String?{
        return userPrefRepo.getWindSpeedUnit()
    }
}



class HomeFactory(private val weatherRepo: IWeatherRepository,
                  private val userPrefRepo: IUserPreferenceRepository,
                  private val locationRepo: ILocationRepository

): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(weatherRepo, userPrefRepo, locationRepo) as T
    }
}