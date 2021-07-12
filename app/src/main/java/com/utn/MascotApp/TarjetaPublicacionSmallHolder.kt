package com.utn.MascotApp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.utn.MascotApp.databinding.TarjetaPublicacionSmallBinding
import java.text.SimpleDateFormat

class TarjetaPublicacionSmallHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = TarjetaPublicacionSmallBinding.bind(view)

    private val storage = Firebase.storage
    private val imagesRef = storage.reference.child("publication-images")


    fun bind(publications: Publications) {
        Picasso.get().load(publications.imagePath).into(binding.cardImagenMascota)

        binding.petName.text = publications.name
        if (publications.species.equals("Gato")) {
            binding.petName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cat_icon, 0)
        }
        binding.petSexAndAge.text = publications.sex + " - " + publications.age
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        binding.fechaPublicacion.text = sdf.format(publications.lastSeen.toDate())

    }


}