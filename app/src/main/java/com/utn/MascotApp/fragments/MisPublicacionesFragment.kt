package com.utn.MascotApp.fragments

import android.os.Bundle
import android.util.Log
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
import com.utn.MascotApp.BottomNavBar


class MisPublicacionesFragment : Fragment() {
    val DEFAULT_NO_IMAGE = "https://www.lasommeliere.com/themes/lasommeliere/assets/_custom/images/no-image.png"
    private val fromBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim)}
    private val toBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim)}
    private var _binding: FragmentMisPublicacionesBinding? = null
    private val publications = mutableListOf<Publications>()
    private lateinit var mascotaAdapter: MascotaAdapter
    private var publicar_button_clicked = false
    private val binding get() = _binding!!


    val petNameParam = null
    val petTypeParam = null
    val petBreedParam = null
    val petSexParam = null
    val petSizeParam = null
    val petColorParam = null
    val petAgeParam = null
    val petLastSeen = null
    val petDirectionParam = null
    val petNumberParam = null
    val petcoordinatesParam = null
    val petDescriptionParam = null
    val foundOrLost = "lost"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisPublicacionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.publicarItem -> {
                    onPublicarButtonClicked()

                    floatingActionButton_EncontreUnaMascota.setOnClickListener() {
                        findNavController().navigate(R.id.action_misPublicacionesFragment_to_filtros)
                    }

                }
                R.id.mainMenuItem -> {
                    findNavController().navigate(R.id.action_misPublicacionesFragment_to_mainMenuFragment)
                }
            }
            true
        }

//        var actionFromv = this.arguments?.getString("actionFrom")
        val action = MisPublicacionesFragmentDirections.actionMisPublicacionesFragmentToFiltros(petNameParam, petTypeParam, petBreedParam,
            petSexParam, petSizeParam, petColorParam, petAgeParam, petLastSeen, petDirectionParam, petNumberParam, petcoordinatesParam,
            petDescriptionParam, foundOrLost).actionId
        BottomNavBar().setBottomNavBar(bottom_navigation, "MisPublicaciones", findNavController(), R.id.action_misPublicacionesFragment_to_mainMenuFragment, action, null)


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
        mascotaAdapter = MascotaAdapter(publications,  findNavController(), "MisPublicaciones")
        binding.listaTarjetasMascotas.layoutManager = LinearLayoutManager(context)
        binding.listaTarjetasMascotas.adapter = mascotaAdapter
        binding.listaTarjetasMascotas.visibility = View.VISIBLE
        val publications = this.arguments?.get("PublicationList")
        getPublicationsFromDB()

    }

    private fun getPublicationsFromDB(){
        var collection_publications : MutableList<Publications> = arrayListOf()
        val db = FirebaseFirestore.getInstance()
        val createdBy = FirebaseAuth.getInstance().currentUser?.uid
        val storage = Firebase.storage
        val imagesRef = storage.reference.child("publication-images")
        db.collection("publications")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        var map : Map<String, Any> = document.data
                        if (createdBy == map.get("createdBy").toString()){
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
                                    geolocation = map.get("geolocation") as GeoPoint,
                                    age = "2",
                                    sex = "Macho"

                                )
                                collection_publications.add(publication)
                                publications.clear()
                                publications.addAll(collection_publications)
                                mascotaAdapter.notifyDataSetChanged()
                                // binding.progressBar.visibility = View.GONE
                            }
                            }
                        }

                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }

    }
}
