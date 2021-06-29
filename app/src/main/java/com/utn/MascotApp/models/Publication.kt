package com.utn.MascotApp.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.IgnoreExtraProperties
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

@IgnoreExtraProperties
data class Publication(
    var type: String? = "",
    var species: String? = "",
    var breed: String? = "",
    var createdAt: Date = Date(),
    var lastSeen: Date = Date(),
    var color: String? = "",
    var size: String? = "",
    var name: String? = "",
    var description: String? = "",
    var address: String? = "",
    var geolocation: GeoPoint = GeoPoint(0.0,0.0),
    var imagePath: String = "",
    var createdBy: String? = "",
    var age: Int? = null,
    var sex: String? = "",
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "type" to type,
            "species" to species,
            "breed" to breed,
            "createdAt" to createdAt,
            "lastSeen" to lastSeen,
            "color" to color,
            "size" to size,
            "name" to name,
            "description" to description,
            "address" to address,
            "geolocation" to geolocation,
            "createdBy" to createdBy,
            "imagePath" to imagePath,
        )
    }
}