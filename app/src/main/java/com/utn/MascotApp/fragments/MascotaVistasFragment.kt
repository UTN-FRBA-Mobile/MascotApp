package com.utn.MascotApp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.utn.MascotApp.MascotaAdapter
import com.utn.MascotApp.Publications
import com.utn.MascotApp.databinding.FragmentMascotaVistasBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class MascotaVistasFragment: Fragment(){
    private lateinit var mascotaAdapter: MascotaAdapter
    private var _binding: FragmentMascotaVistasBinding? = null
    private val publications = mutableListOf<Publications>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMascotaVistasBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart(){
        super.onStart()
        initRecyclerViewMascotasVista()
    }

    fun initRecyclerViewMascotasVista(){
        mascotaAdapter = MascotaAdapter(publications,  findNavController(), "MascotaVistas")
        binding.listaTarjetasMascotas.layoutManager = LinearLayoutManager(context)
        binding.listaTarjetasMascotas.adapter = mascotaAdapter
        binding.listaTarjetasMascotas.visibility = View.VISIBLE
        getPublicationsFromDB()

    }

    private fun getPublicationsFromDB(){
        var collection_publications : MutableList<Publications> = arrayListOf()
        val db = FirebaseFirestore.getInstance()
        val storage = Firebase.storage
        val imagesRef = storage.reference.child("publication-images")
        db.collection("publications")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var map : Map<String, Any> = document.data
                    imagesRef.child(map.get("imagePath").toString()).downloadUrl.addOnSuccessListener {
                        var publication = Publications(
                            address = map.get("address").toString(),
                            color = map.get("color").toString(),
                            imagePath = it.toString(),
                            description = map.get("description").toString(),
                            type = map.get("type").toString(),
                            breed = map.get("breed").toString(),
                            createdAt = map.get("createdAt") as Timestamp,
                            lastSeen = map.get("lastSeen") as Timestamp,
                            size = map.get("size").toString(),
                            createdBy = map.get("createdBy").toString(),
                            species = map.get("species").toString(),
                            name = map.get("name").toString(),
                            geolocation = map.get("geolocation") as GeoPoint
                        )
                        collection_publications.add(publication)
                        publications.clear()
                        publications.addAll(collection_publications)
                        mascotaAdapter.notifyDataSetChanged()
                        binding.progressBar.visibility = View.GONE
                    }

                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
                showError()
            }
    }

    private fun getRetrofit(baseUrl: String):Retrofit{
        return Retrofit.Builder()
//            .baseUrl("https://dog.ceo/api/breed/") Example of baseUrl
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // Para parsear el json
            .build()
    }

    private fun showError(){
        Toast.makeText(context, "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show()
    }

}