package com.utn.MascotApp

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

// Esta clase matchea con las publicaciones de Firebase
data class Publications(
        var address: String,
        var color: String,
        var imagePath: String,
        var description: String,
        var type: String,
        var breed: String,
        var createdAt: Timestamp,
        var lastSeen: Timestamp,
        var size: String,
        var createdBy: String,
        var species: String,
        var name: String,
        var geolocation: GeoPoint
        ){
}