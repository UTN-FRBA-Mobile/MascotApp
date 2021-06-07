package com.utn.MascotApp.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*
import kotlin.collections.ArrayList

@IgnoreExtraProperties
data class Pets(
    var species: String? = "",
    var breed: String? = "",
    var createdAt: Date = Date(),
    var color: String? = "",
    var size: String? = "",
    var name: String? = "",
    var description: String = "",
    var imagePath: String = "",
    var owner: String? = ""
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "species" to species,
            "breed" to breed,
            "createdAt" to createdAt,
            "color" to color,
            "size" to size,
            "name" to name,
            "description" to description,
            "imagePath" to imagePath,
            "owner" to owner,
        )
    }
}