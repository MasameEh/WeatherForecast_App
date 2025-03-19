package com.example.weatherforecast_app

import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.weatherforecast_app.data.remote.RetrofitHelper
import com.example.weatherforecast_app.favorites.view.FavoritesScreen
import com.example.weatherforecast_app.home.view.HomeScreen
import com.example.weatherforecast_app.settings.view.SettingsScreen
import com.example.weatherforecast_app.ui.theme.WeatherForecast_AppTheme
import com.example.weatherforecast_app.weather_alerts.view.WeatherAlertsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherForecast_AppTheme(dynamicColor = false) {
                navHostController = rememberNavController()
                MainScreen()
            }
        }
//        Log.i(TAG, "IO_PARALLELISM_PROPERTY_NAME: ${IO_PARALLELISM_PROPERTY_NAME.length}")
//        lifecycleScope.launch(context = Dispatchers.IO){
//            val res = RetrofitHelper.weatherService.getWeatherForFiveDays(
//                latitude= 30.59,
//                longitude= 31.5019,
//                )
//
//            withContext(context = Dispatchers.Main){
//                if(res.isSuccessful){
//                    Toast.makeText(this@MainActivity, "DONE Retrieving", Toast.LENGTH_SHORT).show()
//                    Log.i(TAG, "isSuccessful: city: ${res.body()?.city}  ${res.body()?.weatherDataList?.get(0)}")
//                }else{
//                    Log.i(TAG, "failed ")
//                }
//            }
//        }
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
}


data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: ScreensRoute
)


val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = ScreensRoute.Home
    ),
    NavigationItem(
        title = "WeatherAlerts",
        icon = Icons.Filled.Create,
        route = ScreensRoute.WeatherAlerts
    ),
    NavigationItem(
        title = "Favorite",
        icon = Icons.Default.Favorite,
        route = ScreensRoute.Favorites
    ),
    NavigationItem(
        title = "Setting",
        icon = Icons.Default.Settings,
        route = ScreensRoute.Settings
    )
)



@Composable
fun BottomNavigationBar(onItemSelected: (NavigationItem) -> Unit){

    NavigationBar(

    ) {
        var selectedNavigationIndex by rememberSaveable {
            mutableIntStateOf(0)
        }

        navigationItems.forEachIndexed {index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex == index,
                onClick = {
                    selectedNavigationIndex = index
                    onItemSelected(item)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        item.title,
                        color = if (index == selectedNavigationIndex)
                            Color.Black
                        else Color.Gray
                    )
                }
            )

        }
    }
}