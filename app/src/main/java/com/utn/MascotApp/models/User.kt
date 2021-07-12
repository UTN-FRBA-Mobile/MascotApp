package com.utn.MascotApp.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*
import kotlin.collections.ArrayList

@IgnoreExtraProperties
data class User(
    var id: String? = "",
    var fullName: String? = "",
    var address: String? = "",
    var phone: String? = "",
    var profilePictureImagePath: String = "",
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "fullName" to fullName,
            "phone" to phone,
            "profilePictureImagePath" to profilePictureImagePath,
            "address" to address,
        )
    }
}