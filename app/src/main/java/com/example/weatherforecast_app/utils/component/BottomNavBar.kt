package com.example.weatherforecast_app.utils.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.weatherforecast_app.ScreensRoute


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