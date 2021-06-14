package com.utn.MascotApp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMisPublicacionesBinding
import com.utn.MascotApp.models.Publication
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.*


class LocationFragment : Fragment() {



    private var _binding: FragmentMisPublicacionesBinding? = null
    private val binding get() = _binding!!

    val publication: Publication = Publication(
        type = "found",
        species = "dog",
        breed = "Chihuahua",
        createdAt = Date(),
        lastSeen = Date(),
        color = "blue",
        size = "small",
        name = "Tony",
        description = "Encontramos a Tony en Parque Las Heras",
        imagePath = "url",
        address = "Parque Las Heras",
        geolocation = GeoPoint(-34.5837944, -58.4091335),
        createdBy = FirebaseAuth.getInstance().currentUser?.uid
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        bottonPublicar.setOnClickListener{

            publication.color =this.arguments?.getString("colorMascota")
            publication.species =this.arguments?.getString("tipoMascota")
            publication.breed =this.arguments?.getString("razaMascota")
            val sexoMascota =this.arguments?.getString("sexoMascota")
            publication.size =this.arguments?.getString("tamanioMascota")
            val edadMascota : Int =this.requireArguments().getInt("edadMascota")
            val fechaMascota =this.arguments?.getString("fechaMascota")
            publication.description = editTextDescripcion.text.toString()
            publication.address = editTextDireccion.text.toString()  + "  " + editTextNumero.text.toString()
            publication.name = this.arguments?.getString("nombreMascota")

            val db = FirebaseFirestore.getInstance()

            db.collection("publications").add(publication)
                .addOnSuccessListener {
                    println("Added document succesfully")

                }
                .addOnFailureListener { exception ->
                    println("Error adding document: $exception")
                }

            val citiesRef = db.collection("publications")


            findNavController().navigate(R.id.action_locationFragment_to_mainMenuFragment)
        }


        bottonAtrasAPhoto.setOnClickListener{
            findNavController().navigate(R.id.action_locationFragment_to_photosFragment) }

    }



}