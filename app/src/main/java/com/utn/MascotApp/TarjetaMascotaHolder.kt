package com.utn.MascotApp

import android.view.View
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utn.MascotApp.databinding.TarjetaMascotaBinding
import com.utn.MascotApp.fragments.MainMenuFragmentDirections
import com.utn.MascotApp.fragments.MisPublicacionesFragmentDirections
import java.text.SimpleDateFormat

class TarjetaMascotaHolder(view: View, private val navController: NavController): RecyclerView.ViewHolder(view) {
    // *************************************************
    // actionFrom: "MascotaVistas" or "MisPublicaciones"
    // *************************************************

    private val binding = TarjetaMascotaBinding.bind(view)

    fun bind(publications: Publications, actionFrom: String){
        Picasso.get().load(publications.imagePath).into(binding.cardImagenMascota)
        binding.cardTitle.text = publications.breed
        binding.cardDireccion.text = publications.address
        binding.cardCommentary.text = publications.description
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        var dateLastSeen = sdf.format(publications.lastSeen.toDate())
        binding.fechaPublicacion.text = dateLastSeen
        binding.petName.text = publications.name
        if (publications.species.equals("cat")) {
            binding.petName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cat_icon, 0)
        }
        binding.petSexAndAge.text = publications.species + " " +publications.color
        binding.cardVerDetalles.setOnClickListener {
            if (actionFrom == "MascotaVistas") {
                val action = MainMenuFragmentDirections.actionMainMenuFragmentToMascotInfoFragment(
                    publications.imagePath, publications.name,
                    publications.description, publications.age.toInt(), publications.sex,
                    publications.color,  publications.breed,  dateLastSeen,
                    publications.address, publications.createdBy,
                    actionFrom)
                navController.navigate(action)
            }
            if (actionFrom == "MisPublicaciones") {
                val action = MisPublicacionesFragmentDirections.actionMisPublicacionesFragmentToMascotInfoFragment(
                    publications.imagePath, publications.name,
                    publications.description, publications.age.toInt(), publications.sex,
                    publications.color,  publications.breed,  dateLastSeen,
                    publications.address,publications.createdBy,
                    actionFrom)
                navController.navigate(action)
            }

        }

    }



}