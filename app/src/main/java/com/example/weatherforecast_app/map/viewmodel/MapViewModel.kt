package com.example.weatherforecast_app.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.model.LocationResponse
import com.example.weatherforecast_app.data.repo.location_repo.ILocationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


private const val TAG = "MapViewModel"
class MapViewModel(
    private val repo: ILocationRepository
): ViewModel()  {

    private val _selectedLocation = MutableStateFlow<LatLng?>(null)
    val selectedLocation: StateFlow<LatLng?> = _selectedLocation.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _searchedLocation = MutableStateFlow<LocationResponse?>(null)
    val searchedLocation: StateFlow<LocationResponse?> = _searchedLocation.asStateFlow()

    private val _searchResults = MutableStateFlow<List<LocationInfo>>(emptyList())
    val searchResults: StateFlow<List<LocationInfo>> = _searchResults

    private val mutableMsg: MutableSharedFlow<String> = MutableSharedFlow()
    val message = mutableMsg.asSharedFlow()

    fun updateSelectedLocation(location: LatLng) {
        _selectedLocation.value = location
        _showBottomSheet.value = true
    }

    fun dismissBottomSheet() {
        _showBottomSheet.value = false
    }

    fun insertLocationIntoFav(location : LocationInfo){

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val result  = repo.insertLocation(location)
                if(result > 0){
                    mutableMsg.emit("Added to Favorites")
                }else{
                    mutableMsg.emit("Error!! Couldn't be added, try again")
                }
            }catch (ex: Exception){
                mutableMsg.emit("Error!! Couldn't be added, try again")
            }

        }

    }

    fun searchLocationByCoordinate(latitude: Double, longitude: Double, language: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.searchLocationByCoordinate(latitude, longitude, language)
            Log.i(TAG, "searchLocationByCoordinate: $latitude $longitude")
            result.catch { e->
                Log.i(TAG, "err: ${e.message}")
            }.collect{
                _searchedLocation.value = it
            }
        }
    }

    fun searchLocationByName(query: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.searchLocationByName(query)
            result.catch { e->
                Log.i(TAG, "err: ${e.message}")
                _searchResults.value = emptyList()
            }.collect{
                _searchResults.value = it
            }
        }
    }

    fun clear(){
        _searchResults.value = emptyList()
    }
}

class MapViewModelFactory(private val repo: ILocationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repo) as T
    }
}