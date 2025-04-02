package com.example.weatherforecast_app.map.view

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.map.viewmodel.MapViewModel
import com.example.weatherforecast_app.utils.LanguageHelper
import com.example.weatherforecast_app.utils.formatAddress
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
fun MapScreen(
    viewModel: MapViewModel,
    onLocationSelected: (LocationInfo) -> Unit,
    popBackStack: ()->Unit
) {

    val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()
    val showBottomSheet by viewModel.showBottomSheet.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val searchedLocation by viewModel.searchedLocation.collectAsStateWithLifecycle()

    val bottomSheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    BackHandler{
        popBackStack()
        viewModel.clear()
    }
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

    Column(modifier = Modifier.padding(10.dp)) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchLocationByName(it)
            },
            label = { Text("Search Location") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .background(color = Color.White)
        )

        searchResults.forEach { LocationInfo ->

            LocationInfo.displayName?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth().background(Color.White)
                        .clickable {
                            val location = LatLng(LocationInfo.latitude, LocationInfo.longitude)
                            viewModel.updateSelectedLocation(location)
                            cameraPositionState.move(
                                CameraUpdateFactory.newLatLngZoom(
                                    location,
                                    10f
                                )
                            )
                        }
                )
                HorizontalDivider()
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.dismissBottomSheet()

                               },
            sheetState = bottomSheetState
        ) {
            selectedLocation?.let {
//                val address = getLocationName(LocalContext.current, it.latitude, it.longitude)
//                val city = address?.locality
//                    ?: address?.getAddressLine(0)
//                        ?.substringBefore(",") // Extracts  the city
//                        ?.replace(Regex("\\d+|\\b(?:Street|St|Rd|Avenue|Ave|Blvd|P.O. Box|PO Box)\\b", RegexOption.IGNORE_CASE), "")
//                        ?.trim()
//                    ?: "Unknown City"
//
//                val country = address?.countryName ?: "Unknown Country"
                viewModel.searchLocationByCoordinate(it.latitude, it.longitude, LanguageHelper.getSystemLocale().language)

                searchedLocation?.let { location ->
                    val formattedAddress = location.features.firstOrNull()?.properties?.let { it1 ->
                        formatAddress(it1.address)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = formattedAddress ?: "Unknown",
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
                        ) {
                            Button(onClick = {
                                val locationClicked = LocationInfo(
                                    longitude = it.longitude,
                                    latitude = it.latitude,
                                    city = formattedAddress ?: "Unknown",
                                    country = ""
                                )
                                onLocationSelected(locationClicked)
                                viewModel.dismissBottomSheet()
                            }) {
                                Text(
                                    "Save",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                            Button(onClick = { viewModel.dismissBottomSheet() }) {
                                Text(
                                    "Cancel",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}