package com.utn.MascotApp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utn.MascotApp.databinding.TarjetaMascotaBinding

class TarjetaMascotaHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = TarjetaMascotaBinding.bind(view)

    fun bind(publications: Publications){
        Picasso.get().load(publications.imagePath).into(binding.cardImagenMascota)
        binding.cardTitle.text = publications.breed
        binding.cardDireccion.text = publications.address
        binding.cardCommentary.text = publications.description
//      TODO falta hacer un formateo de esta fecha
        binding.fechaPublicacion.text = publications.createdAt.toDate().toString()
//      fin TODO
        binding.petName.text = publications.name
        binding.petSexAndAge.text = publications.species + " " +publications.color

    }



}