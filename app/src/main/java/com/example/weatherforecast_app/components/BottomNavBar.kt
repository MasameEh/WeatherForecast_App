package com.example.weatherforecast_app.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.main.ScreensRoute
import com.example.weatherforecast_app.ui.theme.MediumBlue
import com.example.weatherforecast_app.ui.theme.onSecondaryColor


data class NavigationItem(
    @StringRes val titleResId: Int,
    val icon: ImageVector,
    val route: ScreensRoute
)


val navigationItems = listOf(
    NavigationItem(
        titleResId = R.string.home,
        icon = Icons.Default.Home,
        route = ScreensRoute.Home
    ),
    NavigationItem(
        titleResId = R.string.fav,
        icon = Icons.Default.Favorite,
        route = ScreensRoute.Favorites
    ),
    NavigationItem(
        titleResId = R.string.alert,
        icon = Icons.Default.Notifications,
        route = ScreensRoute.WeatherAlerts
    ),
    NavigationItem(
        titleResId = R.string.settings,
        icon = Icons.Default.Settings,
        route = ScreensRoute.Settings
    )
)

@Composable
fun BottomNavigationBar(onItemSelected: (NavigationItem) -> Unit){

    NavigationBar(
        containerColor = MediumBlue,
        tonalElevation = 30.dp
    ) {
        var selectedNavigationIndex by rememberSaveable {
            mutableIntStateOf(0)
        }

        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = false,
                colors = NavigationBarItemColors(
                    selectedIndicatorColor = Color.White.copy(alpha = .2f),
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    disabledIconColor = onSecondaryColor,
                    disabledTextColor = Color.White
                ),
                selected = selectedNavigationIndex == index,
                onClick = {
                    selectedNavigationIndex = index
                    onItemSelected(item)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.titleResId}"       ,
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.titleResId),
                        style = MaterialTheme.typography.labelSmall,

                    )
                }
            )

        }
    }
}