package com.example.weatherforecast_app.favorites.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.favorites.viewmodel.FavoriteViewModel
import com.example.weatherforecast_app.ui.theme.MediumBlue
import com.example.weatherforecast_app.ui.theme.Tertiary
import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.ui.theme.onSecondaryColor
import com.example.weatherforecast_app.utils.ResponseState

private const val TAG = "FavoritesScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(viewModel: FavoriteViewModel,
                    navigateToMap: () -> Unit,
                    navigateToFavoriteDetails: (Double, Double, String) -> Unit) {

    viewModel.getAllFavorites()
    val locationsState by viewModel.favoritesList.collectAsStateWithLifecycle()

    when(locationsState){
        is ResponseState.Failure -> {

        }
        ResponseState.Loading -> {
            Box(
                contentAlignment = Alignment.Center ,
                modifier = Modifier
                    .fillMaxSize()
                    .gradientBackground()
            ) {
                CircularProgressIndicator(color = onSecondaryColor)
            }
        }
        is ResponseState.Success -> {

            val successLocationsData = (locationsState as ResponseState.Success).data as List<LocationInfo>
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navigateToMap.invoke() },
                        containerColor = Tertiary,
                    ) {
                        Icon(Icons.Default.Add, tint = Color.White,contentDescription = "Add Location")
                    } },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Favorite Locations",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = Color.White
                        )
                    )
                },

                ){ innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .gradientBackground()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    FavoriteLocations(viewModel, successLocationsData, navigateToFavoriteDetails)
                }
            }
        }
    }


}

@Composable
fun FavoriteLocations(viewModel: FavoriteViewModel,
                      locations: List<LocationInfo>,
                      navigateToFavoriteDetails: (Double, Double, String) -> Unit){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp,),
    ) {
        items(
            locations.size,
            key = {"${locations[it].latitude}_${locations[it].longitude} "}
        ) {
            FavoriteLocationItem(viewModel, locations[it], navigateToFavoriteDetails)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteLocationItem(viewModel: FavoriteViewModel,
                         location: LocationInfo,
                         navigateToFavoriteDetails: (Double, Double, String) -> Unit){

    val dismissState = rememberSwipeToDismissBoxState()
   SwipeToDismissBox(
       state = dismissState,
       enableDismissFromStartToEnd = false,
       backgroundContent = {
           Box(
               modifier = Modifier.fillMaxSize(),
               contentAlignment = Alignment.CenterEnd
           ){
               Icon(
                   imageVector = Icons.Default.Delete,
                   contentDescription = "delete Icon",
                   tint = Color.Red,
               )
           }
       },
   ){
       when(dismissState.currentValue){
            SwipeToDismissBoxValue.StartToEnd ->{
            }

           SwipeToDismissBoxValue.EndToStart -> {
               Log.i(TAG, "FavoriteLocationItem: ")
               viewModel.deleteLocationFromFav(location)
           }
           SwipeToDismissBoxValue.Settled -> {

           }
       }
       ElevatedCard(
           elevation = CardDefaults.cardElevation(
               defaultElevation = 8.dp
           ),
           colors = CardDefaults.cardColors(
               containerColor = onSecondaryColor,

               ),
           modifier = Modifier
               .fillMaxWidth()
               .border(2.dp, onSecondaryColor, shape = RoundedCornerShape(15.dp))
               .clickable {
                   navigateToFavoriteDetails.invoke(location.latitude, location.longitude, location.city)
               }
       )  {
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(16.dp),
               horizontalArrangement = Arrangement.spacedBy(2.dp, alignment = Alignment.CenterHorizontally),
               verticalAlignment = Alignment.CenterVertically
           ){
               Icon(
                   imageVector = Icons.Default.LocationOn,
                   contentDescription = "show details",
                   tint = MediumBlue,
               )
               Text(
                   text= "${location.city}, ${location.country}",
                   style = MaterialTheme.typography.titleMedium,
                   fontWeight = FontWeight.Normal,
                   textAlign = TextAlign.Center,
                   color = MediumBlue,
                   modifier = Modifier.weight(2f)
                   //text= "$city, $country"
               )
               Icon(
                   imageVector = Icons.Default.KeyboardArrowRight,
                   contentDescription = "show details",
                   tint = MediumBlue,
               )
           }

       }
   }
}