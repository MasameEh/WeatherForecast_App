package com.example.weatherforecast_app.map.view

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.map.viewmodel.MapViewModel
import com.example.weatherforecast_app.utils.getLocationName
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState





@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel) {
    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.message.collect{
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Log.i("Map", "MapScreen: ")
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng( 30.0444, 31.2357), 5f)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { clickedPosition ->
                Log.d(
                    "Map",
                    "User selected: ${clickedPosition.latitude}, ${clickedPosition.longitude}"
                )
                viewModel.updateSelectedLocation(LatLng(clickedPosition.latitude, clickedPosition.longitude))
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(clickedPosition)
                )
            }
        ) {
            selectedLocation?.let {Marker(
                state = MarkerState(position = it),
                title = "Selected Location",
                snippet = "Lat: ${it.latitude}, Lng: ${it.longitude}"
            )  }

        }
    }
    TopAppBar(
        title = { Text("Select Location") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f), // Slight transparency
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .padding(top = 40.dp, start = 10.dp, end = 10.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Transparent),
        navigationIcon = {
            IconButton(
                onClick = {

                },

                ) {
                Icon(
                    imageVector = Icons.Sharp.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissBottomSheet() },
            sheetState = bottomSheetState
        ) {
            selectedLocation?.let {
                val address = getLocationName(LocalContext.current, it.latitude, it.longitude)
                val city = address?.locality
                    ?: address?.getAddressLine(0)
                        ?.substringBefore(",") // Extracts  the city
                        ?.replace(Regex("\\d+|\\b(?:Street|St|Rd|Avenue|Ave|Blvd|P.O. Box|PO Box)\\b", RegexOption.IGNORE_CASE), "")
                        ?.trim()
                    ?: "Unknown City"

                val country = address?.countryName ?: "Unknown Country"

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$city, $country",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Latitude: ${it.latitude.toFloat()}, Longitude: ${it.longitude.toFloat()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Button(onClick = {
                            val locationClicked = LocationInfo(
                                longitude = it.longitude,
                                latitude = it.latitude,
                                city = city,
                                country = country
                            )
                            viewModel.insertLocationIntoFav(locationClicked)
                            viewModel.dismissBottomSheet()
                        }) {
                            Text("Add to Favorite",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Button(onClick = {viewModel.dismissBottomSheet() }) {
                            Text("Cancel",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    }
}