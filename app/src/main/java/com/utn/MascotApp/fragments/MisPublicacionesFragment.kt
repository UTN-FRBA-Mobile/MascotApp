package com.utn.MascotApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.utn.MascotApp.MascotaAdapter
import com.utn.MascotApp.Publications
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMisPublicacionesBinding
import kotlinx.android.synthetic.main.fragment_main_menu.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.Timestamp


class MisPublicacionesFragment : Fragment() {
    private val fromBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim)}
    private val toBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim)}
    private var _binding: FragmentMisPublicacionesBinding? = null
    private val publications = mutableListOf<Publications>()
    private lateinit var mascotaAdapter: MascotaAdapter
    private var publicar_button_clicked = false
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisPublicacionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mainMenuItem -> {
                    findNavController().navigate(R.id.action_misPublicacionesFragment_to_mainMenuFragment)
                }
                R.id.publicarItem -> {
                    onPublicarButtonClicked()
                }
            }
            true
        }
    }

    private fun onPublicarButtonClicked() {
        setVisibility(publicar_button_clicked)
        setAnimation(publicar_button_clicked)
        setClickable(publicar_button_clicked)
        publicar_button_clicked = !publicar_button_clicked
    }

    private fun setVisibility(clicked: Boolean){
        if (!clicked){
            binding.floatingActionButtonEncontreUnaMascota.visibility = View.VISIBLE
            binding.floatingActionButtonPerdiMiMascota.visibility = View.VISIBLE
        } else {
            binding.floatingActionButtonEncontreUnaMascota.visibility = View.INVISIBLE
            binding.floatingActionButtonPerdiMiMascota.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean){
        if (!clicked){
            binding.floatingActionButtonEncontreUnaMascota.startAnimation(fromBotton)
            binding.floatingActionButtonPerdiMiMascota.startAnimation(fromBotton)
        } else {
            binding.floatingActionButtonEncontreUnaMascota.startAnimation(toBotton)
            binding.floatingActionButtonPerdiMiMascota.startAnimation(toBotton)
        }
    }

    private fun setClickable(clicked: Boolean){
        if (!clicked){
            binding.floatingActionButtonEncontreUnaMascota.isClickable = true
            binding.floatingActionButtonPerdiMiMascota.isClickable = true
        } else {
            binding.floatingActionButtonEncontreUnaMascota.isClickable = false
            binding.floatingActionButtonPerdiMiMascota.isClickable = false
        }
    }

    override fun onStart(){
        super.onStart()
        initRecyclerViewMascotasVista()
    }

    fun initRecyclerViewMascotasVista(){
        mascotaAdapter = MascotaAdapter(publications)
        binding.listaTarjetasMascotas.layoutManager = LinearLayoutManager(context)
        binding.listaTarjetasMascotas.adapter = mascotaAdapter
        binding.listaTarjetasMascotas.visibility = View.VISIBLE
        getPublicationsFromDB()

    }

    private fun getPublicationsFromDB(){
        var collection_publications : MutableList<Publications> = arrayListOf()
        val db = FirebaseFirestore.getInstance()
        val createdBy = FirebaseAuth.getInstance().currentUser?.uid
        db.collection("publications")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        var map : Map<String, Any> = document.data
//                      TODO este if no se si funciona, no me trae solo mis publicaciones......
                        if (createdBy == map.get("createdBy").toString()){
//                      fin TODO
                                var publication = Publications(
                                        address = map.get("address").toString(),
                                        color = map.get("color").toString(),
                                        imagePath = map.get("imagePath").toString(),
//                                      TODO hay que cargar las imágenes de la URL real.
//                                      dejo esta hardcodeada  imagePath = "https://laughingcolours.com/wp-content/uploads/2019/06/k-s-pets-services-ecil-hyderabad-pet-care-takers-1knoqwn9vh-1.jpg",
//                                      FIN TODO
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
                            }
                        }
                    publications.clear()
                    publications.addAll(collection_publications)
                    mascotaAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }
    }
}
