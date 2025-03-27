package com.example.weatherforecast_app.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.repo.location_repo.ILocationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


private const val TAG = "MapViewModel"
class MapViewModel(
    private val repo: ILocationRepository
): ViewModel()  {

    private val _selectedLocation = MutableStateFlow<LatLng?>(null)
    val selectedLocation: StateFlow<LatLng?> = _selectedLocation.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()


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
                    Log.i(TAG, "insertLocationIntoFav: res = $result")
                }
            }catch (ex: Exception){
                Log.i(TAG, "insertLocationIntoFav: ex = $ex")
                mutableMsg.emit("Error!! Couldn't be added, try again")
            }

        }

    }
}

class MapViewModelFactory(private val repo: ILocationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repo) as T
    }
}