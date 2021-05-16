package com.utn.MascotApp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utn.MascotApp.databinding.TarjetaMascotaBinding

class TarjetaMascotaHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = TarjetaMascotaBinding.bind(view)

    fun bind(image:String){
        Picasso.get().load(image).into(binding.cardImagenMascota)
        binding.cardTitle.text = "Card Title"
        binding.cardSecondaryText.text = "Secondary Text"
    }
}