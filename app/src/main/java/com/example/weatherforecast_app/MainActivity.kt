package com.example.weatherforecast_app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.weatherforecast_app.data.remote.RetrofitHelper
import com.example.weatherforecast_app.data.remote.WeatherRemoteDataSourceImp
import com.example.weatherforecast_app.data.repo.ILocationRepository
import com.example.weatherforecast_app.data.repo.ILocationRepositoryImp
import com.example.weatherforecast_app.data.repo.WeatherRepositoryImp
import com.example.weatherforecast_app.favorites.view.FavoritesScreen
import com.example.weatherforecast_app.home.view.HomeScreen
import com.example.weatherforecast_app.settings.view.SettingsScreen
import com.example.weatherforecast_app.ui.theme.WeatherForecast_AppTheme
import com.example.weatherforecast_app.utils.component.BottomNavigationBar
import com.example.weatherforecast_app.weather_alerts.view.WeatherAlertsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
    private val TAG = "MainActivity"
    private lateinit var locationRepository: ILocationRepository
    private val LOCATION_REQUEST_CODE = 50;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        locationRepository = ILocationRepositoryImp(this)
        setContent {
            WeatherForecast_AppTheme(dynamicColor = false) {
                navHostController = rememberNavController()
                MainScreen()
            }
        }
        Log.i(TAG, "IO_PARALLELISM_PROPERTY_NAME: ${IO_PARALLELISM_PROPERTY_NAME.length}")

//        val repo = WeatherRepositoryImp.getInstance(WeatherRemoteDataSourceImp())
//        lifecycleScope.launch(context = Dispatchers.IO){
//            val weather = repo.getCurrentWeather(
//                latitude = 30.59, longitude = 31.5019,
//            )
//
//            weather
//                .catch { ex ->   Log.i(TAG, "EX: ${ex.message}: ")}
//                .collect{
//                    Log.i(TAG, "isSuccessful: city: ${it.name} and ${it.placeInfo} ")
//                }
//        }

    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
        if(checkPermissions()){
            if (isLocationEnabled(this)){
                Log.i(TAG, "onStart: ")
                locationRepository.getFreshLocation {
                        location ->
                    Log.i(TAG, "Location: $location")
                }
            }else{
                Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
                enableLocationServices()
            }
        }else{
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
    @Composable
    fun SetUpNavHost(modifier: Modifier = Modifier){

        NavHost(
            navController = navHostController,
            startDestination = ScreensRoute.Home,
        ){
            composable<ScreensRoute.Home>{
                HomeScreen()
            }

            composable<ScreensRoute.WeatherAlerts>{
                WeatherAlertsScreen()
            }

            composable<ScreensRoute.Favorites>{
                FavoritesScreen()
            }

            composable<ScreensRoute.Settings>{
                SettingsScreen()
            }

        }
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier){
        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0.dp),
            bottomBar = {BottomNavigationBar({selectedItem -> navHostController.navigate(selectedItem.route)})}
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

