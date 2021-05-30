package com.utn.MascotApp

import com.google.android.gms.maps.model.LatLng


data class Pet(
    val name: String,
    val latLng: LatLng,
    val address: String,
    val rating: Float

)