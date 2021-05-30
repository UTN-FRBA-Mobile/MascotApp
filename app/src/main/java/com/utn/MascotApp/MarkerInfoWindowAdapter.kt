package com.utn.MascotApp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker?): View? {
        val pet = marker?.tag as? Pet ?: return null

        val view = LayoutInflater.from(context).inflate(R.layout.marker_info_contents, null)
        view.findViewById<TextView>(R.id.text_view_title).text = pet.name
        view.findViewById<TextView>(R.id.text_view_address).text = pet.address
        view.findViewById<TextView>(R.id.text_view_rating).text = "Edad: %.2f".format(pet.rating)

        return view
    }

    override fun getInfoWindow(marker: Marker?): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }
}