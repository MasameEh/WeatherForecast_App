package com.example.weatherforecast_app.main

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.data.local.location.LocationLocalDataSourceImp
import com.example.weatherforecast_app.data.local.AppDatabase

import com.example.weatherforecast_app.data.remote.WeatherRemoteDataSourceImp
import com.example.weatherforecast_app.data.repo.location_repo.LocationRepositoryImp
import com.example.weatherforecast_app.data.repo.weather_repo.WeatherRepositoryImp
import com.example.weatherforecast_app.favorites.view.FavoritesScreen
import com.example.weatherforecast_app.favorites.viewmodel.FavoriteViewModel
import com.example.weatherforecast_app.favorites.viewmodel.FavoriteViewModelFactory
import com.example.weatherforecast_app.home.view.HomeScreen
import com.example.weatherforecast_app.home.viewmodel.HomeFactory
import com.example.weatherforecast_app.home.viewmodel.HomeViewModel
import com.example.weatherforecast_app.map.view.MapScreen
import com.example.weatherforecast_app.map.viewmodel.MapViewModel
import com.example.weatherforecast_app.map.viewmodel.MapViewModelFactory
import com.example.weatherforecast_app.settings.view.SettingsScreen
import com.example.weatherforecast_app.ui.theme.WeatherForecast_AppTheme
import com.example.weatherforecast_app.components.BottomNavigationBar
import com.example.weatherforecast_app.data.local.Alert.AlertLocalDataSourceImp
import com.example.weatherforecast_app.data.local.preferences.CacheHelper
import com.example.weatherforecast_app.data.repo.alert_repo.AlertRepositoryImp
import com.example.weatherforecast_app.data.repo.user_pref.UserPreferenceRepositoryImp
import com.example.weatherforecast_app.favorite_weather_details.view.FavoriteDetailsScreen
import com.example.weatherforecast_app.favorite_weather_details.viewmodel.FavoriteDetailsFactory
import com.example.weatherforecast_app.favorite_weather_details.viewmodel.FavoriteDetailsViewModel
import com.example.weatherforecast_app.settings.viewmodel.SettingsViewModel
import com.example.weatherforecast_app.settings.viewmodel.SettingsViewModelFactory
import com.example.weatherforecast_app.utils.Constants.REQUEST_CODE_NOTIFICATIONS
import com.example.weatherforecast_app.utils.LanguageHelper
import com.example.weatherforecast_app.weather_alerts.view.WeatherAlertsScreen
import com.example.weatherforecast_app.weather_alerts.viewmodel.AlertsViewModel
import com.example.weatherforecast_app.weather_alerts.viewmodel.AlertsViewModelFactory
import com.google.android.gms.location.LocationServices



class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
    private val TAG = "MainActivity"
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var userLang: String
    private val LOCATION_REQUEST_CODE = 550;


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()

        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_blue));
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false // Ensures light icons
        }

        settingsViewModel = ViewModelProvider(
            this, SettingsViewModelFactory(
                UserPreferenceRepositoryImp.getInstance(
                    CacheHelper.getInstance(this)
                )
            )
        )[SettingsViewModel::class.java]

        locationViewModel = ViewModelProvider(
            this,
            LocationFactory(
                LocationRepositoryImp.getInstance(LocationServices.getFusedLocationProviderClient(
                    this
                ), LocationLocalDataSourceImp(
                    AppDatabase.getInstance(this).getLocationsDao()
                )
                )
            )
        )[LocationViewModel::class.java]

        userLang = settingsViewModel.getLanguagePref() ?: "System Default"

        LanguageHelper.setAppLocale(this, userLang)

        setContent {
            WeatherForecast_AppTheme(dynamicColor = false) {
                navHostController = rememberNavController()
                MainScreen()
            }
        }


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
                locationViewModel.getFreshLocation {
                    location ->
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
                    locationViewModel.getFreshLocation {
                            location ->
                        location?.let {
                            homeViewModel.updateLocation(
                                latitude = it.latitude,
                                longitude = it.longitude
                            )
                        }
                    }
                }else{
                    Log.i(TAG, "onStart: enable location services ")
                    Toast.makeText(this, "Please enable location services so you see the latest weather status!", Toast.LENGTH_LONG).show()
                    enableLocationServices()
                }
            }else{
                // Permissions denied
                Toast.makeText(this, "Location permissions denied", Toast.LENGTH_LONG).show()
            }
        }

        if (requestCode == REQUEST_CODE_NOTIFICATIONS) {
            Toast.makeText(this, "Permission denied! Alerts will not appear", Toast.LENGTH_LONG).show()
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
                WeatherAlertsScreen(
                    ViewModelProvider(
                        this@MainActivity,
                        AlertsViewModelFactory(
                            AlertRepositoryImp.getInstance(
                                AlertLocalDataSourceImp(
                                    dao =  AppDatabase.getInstance(this@MainActivity).getLocationsDao()
                                )
                            ))
                    )[AlertsViewModel::class.java]
                )
            }

            composable<ScreensRoute.Favorites>{

                FavoritesScreen(ViewModelProvider(
                    this@MainActivity, FavoriteViewModelFactory(
                        LocationRepositoryImp.getInstance(
                            LocationServices.getFusedLocationProviderClient(
                                this@MainActivity
                            ), LocationLocalDataSourceImp(
                                AppDatabase.getInstance(this@MainActivity).getLocationsDao()
                            )
                        )
                    )
                )[FavoriteViewModel::class.java],
                    { navHostController.navigate(ScreensRoute.Map) },
                    { latitude, longitude, city -> navHostController.navigate(ScreensRoute.FavoriteDetails(latitude,longitude, city )) }
                )
            }

            composable<ScreensRoute.Settings>{
                SettingsScreen(settingsViewModel)
            }

            composable<ScreensRoute.Map>{

                MapScreen(
                    ViewModelProvider(
                        this@MainActivity, MapViewModelFactory(
                            LocationRepositoryImp.getInstance(
                                LocationServices.getFusedLocationProviderClient(
                                    this@MainActivity
                                ), LocationLocalDataSourceImp(
                                    AppDatabase.getInstance(this@MainActivity)
                                        .getLocationsDao()
                                )
                            )
                        )
                    )[MapViewModel::class.java]
                )
            }

            composable<ScreensRoute.FavoriteDetails>{backstackEntry ->
                val entry = backstackEntry.toRoute<ScreensRoute.FavoriteDetails>()
                val viewModel = ViewModelProvider(
                    this@MainActivity,
                        FavoriteDetailsFactory(
                            WeatherRepositoryImp.getInstance(
                                WeatherRemoteDataSourceImp()
                            )
                        )
                )[FavoriteDetailsViewModel::class.java]
                FavoriteDetailsScreen(viewModel, entry.latitude, entry.longitude, entry.city)
            }
        }
    }


    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        val snackBarHostState = remember { SnackbarHostState() }
        val showBottomAppBar = remember { mutableStateOf(true) }

        LaunchedEffect(navHostController) {
            navHostController.addOnDestinationChangedListener { _, destination, _ ->
                Log.i("route", "MainScreen: ${destination.route} ${destination.id} ${destination.label}")
                when(destination.route){
                    "com.example.weatherforecast_app.main.ScreensRoute.Map" -> showBottomAppBar.value = false
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
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun enableLocationServices() {
        // implicit intent
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }



}



