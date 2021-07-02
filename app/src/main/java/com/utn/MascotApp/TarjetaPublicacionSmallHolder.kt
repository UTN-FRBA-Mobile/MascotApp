package com.utn.MascotApp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.utn.MascotApp.databinding.TarjetaPublicacionSmallBinding

class TarjetaPublicacionSmallHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = TarjetaPublicacionSmallBinding.bind(view)

    private val storage = Firebase.storage
    private val imagesRef = storage.reference.child("publication-images")


    fun bind(publications: Publications){
        Picasso.get().load(publications.imagePath).into(binding.cardImagenMascota)

        binding.petName.text = publications.name
        if (publications.species.equals("cat")) {
            binding.petName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cat_icon, 0)
        }
        binding.petSexAndAge.text = publications.sex + " - " + publications.age
//      TODO falta hacer un formateo de esta fecha
        binding.fechaPublicacion.text = publications.lastSeen.toDate().toString()
//      fin TODO

    }



}