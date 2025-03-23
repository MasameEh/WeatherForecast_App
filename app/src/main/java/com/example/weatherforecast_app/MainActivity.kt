package com.example.weatherforecast_app

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.example.weatherforecast_app.data.remote.WeatherRemoteDataSourceImp
import com.example.weatherforecast_app.data.repo.ILocationRepository
import com.example.weatherforecast_app.data.repo.ILocationRepositoryImp
import com.example.weatherforecast_app.data.repo.WeatherRepositoryImp
import com.example.weatherforecast_app.favorites.view.FavoritesScreen
import com.example.weatherforecast_app.home.view.HomeScreen
import com.example.weatherforecast_app.home.viewmodel.HomeFactory
import com.example.weatherforecast_app.home.viewmodel.HomeViewModel
import com.example.weatherforecast_app.map.MapScreen
import com.example.weatherforecast_app.settings.view.SettingsScreen
import com.example.weatherforecast_app.ui.theme.WeatherForecast_AppTheme
import com.example.weatherforecast_app.utils.component.BottomNavigationBar
import com.example.weatherforecast_app.weather_alerts.view.WeatherAlertsScreen

import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME

private const val LOCATION_REQUEST_CODE = 550;
class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
    private val TAG = "MainActivity"
    private lateinit var locationRepository: ILocationRepository
    private lateinit var homeViewModel: HomeViewModel


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_blue));
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false // Ensures light icons
        }
        locationRepository = ILocationRepositoryImp(this)

        setContent {
            WeatherForecast_AppTheme(dynamicColor = false) {
                navHostController = rememberNavController()
                MainScreen()
            }
        }
        Log.i(TAG, "IO_PARALLELISM_PROPERTY_NAME: ${IO_PARALLELISM_PROPERTY_NAME.length}")

    }



    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
        homeViewModel = ViewModelProvider(this,
            HomeFactory(
                WeatherRepositoryImp.getInstance(
                    WeatherRemoteDataSourceImp()
                )
            )
        ).get(HomeViewModel::class.java)

        if(checkPermissions()){
            if (isLocationEnabled(this)){
                Log.i(TAG, "onStart: ")
                locationRepository.getFreshLocation {
                        location ->
                    Log.i(TAG, "Location: $location")
                    location?.let {
                        homeViewModel.updateLocation(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    }
                }
            }else{
                Log.i(TAG, "onStart: enable location services ")
                Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
                showEnableLocationDialog()
            }
        }else{
            Log.i(TAG, "onStart: requestPermissions ")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionsResult: ")
        if(requestCode == LOCATION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isLocationEnabled(this)){
                    Log.i(TAG, "onStart: ")
                    locationRepository.getFreshLocation {
                            location ->
                        Log.i(TAG, "Location: $location")
                        location?.let {
                            homeViewModel.updateLocation(
                                latitude = it.latitude,
                                longitude = it.longitude
                            )
                        }
                    }
                }else{
                    Log.i(TAG, "onStart: enable location services ")
                    Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
                    enableLocationServices()
                }
            }else{
                // Permissions denied
                Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showEnableLocationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Location Required")
            .setMessage("Please enable location services to get weather updates.")
            .setPositiveButton("Enable") { _, _ -> enableLocationServices() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("You have denied location permissions. Please enable them in app settings.")
            .setPositiveButton("Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @Composable
    fun SetUpNavHost(modifier: Modifier = Modifier){

        NavHost(
            navController = navHostController,
            startDestination = ScreensRoute.Home,
        ){
            composable<ScreensRoute.Home>{
                HomeScreen(homeViewModel)
            }

            composable<ScreensRoute.WeatherAlerts>{
                WeatherAlertsScreen()
            }

            composable<ScreensRoute.Favorites>{
                FavoritesScreen({navHostController.navigate(ScreensRoute.Map)})
            }

            composable<ScreensRoute.Settings>{
                SettingsScreen()
            }

            composable<ScreensRoute.Map>{ backstackEntry->
                val entry = backstackEntry.toRoute<ScreensRoute.Map>()
                MapScreen()
            }

        }
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        val snackBarHostState = remember { SnackbarHostState() }
        var showBottomAppBar = remember { mutableStateOf(true) }

        LaunchedEffect(navHostController) {
            navHostController.addOnDestinationChangedListener { _, destination, _ ->
                Log.i("route", "MainScreen: ${destination.route} ${destination.id} ${destination.label}")
                when(destination.route){
                    "com.example.weatherforecast_app.ScreensRoute.Map" -> showBottomAppBar.value = false
                    else -> showBottomAppBar.value = true
                }
            }
        }

        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0.dp),
            bottomBar = {
                if(showBottomAppBar.value){
                    BottomNavigationBar({ selectedItem -> navHostController.navigate(selectedItem.route) })
                }
            },
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) { innerPadding ->
            SetUpNavHost(Modifier.padding(innerPadding))
        }
    }

    private fun checkPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                ||  ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun enableLocationServices() {
        // implicit intent
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }


}



