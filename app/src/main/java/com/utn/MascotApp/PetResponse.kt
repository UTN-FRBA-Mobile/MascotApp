package com.utn.MascotApp

import com.google.android.gms.maps.model.LatLng

data class PetResponse(
    val geometry: Geometry,
    val name: String,
    val vicinity: String,
    val rating: Float
) {

    data class Geometry(
        val location: GeometryLocation
    )

    data class GeometryLocation(
        val lat: Double,
        val lng: Double
    )
}

fun PetResponse.toPlace(): Pet = Pet(
    name = name,
    latLng = LatLng(geometry.location.lat, geometry.location.lng),
    address = vicinity,
    rating = rating
)