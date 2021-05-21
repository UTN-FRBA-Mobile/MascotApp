package com.utn.MascotApp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utn.MascotApp.databinding.TarjetaMascotaBinding

class TarjetaMascotaHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = TarjetaMascotaBinding.bind(view)

    fun bind(image:String){
        Picasso.get().load(image).into(binding.cardImagenMascota)
        binding.cardTitle.text = "ME PERDI EN"
        binding.cardDireccion.text = "Av. Directorio 1524"
        binding.fechaPublicacion.text = "12 Marzo 2021"
        binding.petName.text = "Puchini"
        binding.petSexAndAge.text = "Hembra - 2 Años"
    }
}