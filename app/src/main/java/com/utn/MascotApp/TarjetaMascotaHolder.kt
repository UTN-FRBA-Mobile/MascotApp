package com.utn.MascotApp

import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utn.MascotApp.databinding.TarjetaMascotaBinding
import com.bumptech.glide.Glide
import com.utn.MascotApp.fragments.MainMenuFragmentDirections
import com.utn.MascotApp.fragments.MascotaVistasFragment
import kotlinx.android.synthetic.main.fragment_main_menu.*


class TarjetaMascotaHolder(view: View, private val navController: NavController): RecyclerView.ViewHolder(view) {
    private val binding = TarjetaMascotaBinding.bind(view)

    fun bind(image:String){
        Picasso.get().load(image).into(binding.cardImagenMascota)
        binding.cardTitle.text = "ME PERDI EN"
        binding.cardDireccion.text = "Av. Directorio 1524"
        binding.fechaPublicacion.text = "12 Marzo 2021"
        binding.petName.text = "Puchini"
        binding.petSexAndAge.text = "Hembra - 2 Años"
        binding.cardVerDetalles.setOnClickListener {
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToDetallesTarjetaFragment(image) // Aca declaramos lo que le vamos a mandar, yo en este caso solo "image"
            navController.navigate(action)
        }
    }

}