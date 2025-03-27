package com.example.weatherforecast_app.data.repo.location_repo

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.example.weatherforecast_app.data.local.location.ILocationLocalDataSource
import com.example.weatherforecast_app.data.model.LocationInfo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImp private constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val localDataSource: ILocationLocalDataSource
): ILocationRepository {


    @SuppressLint("MissingPermission")
    override fun getFreshLocation(onLocationReceived: (Location?) -> Unit) {
        Log.i("getFreshLocation", "getFreshLocation: ")

        val locationRequest = LocationRequest.Builder(10000)
            .setMinUpdateDistanceMeters(100f)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
//        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
//            .addOnSuccessListener {
//                    location ->
//                onLocationReceived(location)
//                Log.i("getFreshLocation", "getFreshLocation: ${location.latitude} + ${location.longitude} ")
//            }.addOnFailureListener { exception ->
//                Log.e("Location", "Error fetching location: ${exception.message}")
//            }

        fusedLocationClient.requestLocationUpdates(locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    Log.i("getFreshLocation", "getFreshLocation: ${location.lastLocation?.latitude} + ${location.lastLocation?.longitude} ")
                    onLocationReceived(location.lastLocation)
                }
            },
            Looper.getMainLooper())
    }

    override fun getAllFavLocations(): Flow<List<LocationInfo>> {
        return localDataSource.getAllFavLocations()
    }

    override suspend fun insertLocation(location: LocationInfo): Long {
        return localDataSource.insertLocation(location)

    }

    override suspend fun deleteLocation(location: LocationInfo): Int {
        return localDataSource.deleteLocation(location)
    }

    companion object{
        // Without volatile, a thread could cache instance locally and
        // keep checking the old value even if another thread already updated it.
        @Volatile
        private var instance: LocationRepositoryImp? = null

        fun getInstance(fusedLocationClient: FusedLocationProviderClient, localDataSource: ILocationLocalDataSource): LocationRepositoryImp {
            //only one thread can enter the block at a time.
            return instance ?: synchronized(this) {
                instance ?: LocationRepositoryImp(fusedLocationClient, localDataSource)
                    .also {
                        instance = it
                    }
            }
        }

    }

}

