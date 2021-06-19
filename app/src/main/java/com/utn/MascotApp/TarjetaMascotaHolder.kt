package com.utn.MascotApp

import android.view.View
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utn.MascotApp.databinding.TarjetaMascotaBinding
import com.utn.MascotApp.fragments.MainMenuFragmentDirections
import com.utn.MascotApp.fragments.MisPublicacionesFragmentDirections

class TarjetaMascotaHolder(view: View, private val navController: NavController, private val actionFrom: String): RecyclerView.ViewHolder(view) {
    // *************************************************
    // actionFrom: "MascotaVistas" or "MisPublicaciones"
    // *************************************************

    private val binding = TarjetaMascotaBinding.bind(view)

    fun bind(publications: Publications, actionFrom: String){
        Picasso.get().load(publications.imagePath).into(binding.cardImagenMascota)
        binding.cardTitle.text = publications.breed
        binding.cardDireccion.text = publications.address
        binding.cardCommentary.text = publications.description
//      TODO falta hacer un formateo de esta fecha
        binding.fechaPublicacion.text = publications.createdAt.toDate().toString()
//      fin TODO
        binding.petName.text = publications.name
        binding.petSexAndAge.text = publications.species + " " +publications.color
        binding.cardVerDetalles.setOnClickListener {
            if (actionFrom == "MascotaVistas") {
                val action = MainMenuFragmentDirections.actionMainMenuFragmentToMascotInfoFragment(
                    publications.imagePath, publications.name,
                    publications.description, 11, "sex",
                    publications.color,  publications.breed,  publications.lastSeen.toString(),
                    publications.address,
                    actionFrom)
                navController.navigate(action)
            }
            if (actionFrom == "MisPublicaciones") {
                val action = MisPublicacionesFragmentDirections.actionMisPublicacionesFragmentToMascotInfoFragment(
                    publications.imagePath, publications.name,
                    publications.description, 11, "sex",
                    publications.color,  publications.breed,  publications.lastSeen.toString(),
                    publications.address,
                    actionFrom)
                navController.navigate(action)
            }

        }

    }



}