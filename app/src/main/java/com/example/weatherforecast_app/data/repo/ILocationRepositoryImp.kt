package com.example.weatherforecast_app.data.repo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class ILocationRepositoryImp(private val context: Context): ILocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

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



}

