package com.example.weatherforecast_app.main;

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast_app.data.repo.location_repo.ILocationRepository;


class LocationViewModel (private val repo: ILocationRepository): ViewModel(){

    fun getFreshLocation(onLocationReceived: (Location?) -> Unit){
        repo.getFreshLocation(onLocationReceived)
    }
}


class LocationFactory(private val repo: ILocationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(repo) as T
    }
}