package com.example.weatherforecast_app.data.model


data class LocationResponse(
    val features: List<Feature>,
    val licence: String,
    val type: String
)

data class Feature(
    val bbox: List<Double>,
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)

data class Geometry(
    val coordinates: List<Double>,
    val type: String
)

data class Properties(
    val address: AddressInfo,
    val addresstype: String,
    val category: String,
    val display_name: String,
    val importance: Double,
    val name: String,
    val osm_id: Long,
    val osm_type: String,
    val place_id: Int,
    val place_rank: Int,
    val type: String
)

data class AddressInfo(
    val road: String?,
    val house_number: String?,
    val neighbourhood: String?,
    val village: String?,
    val town: String?,
    val city: String?,
    val county: String?,
    val state: String?,
    val postcode: String?,
    val country: String?,
    val country_code: String?
)