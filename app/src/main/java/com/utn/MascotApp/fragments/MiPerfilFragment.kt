package com.utn.MascotApp.fragments

import android.content.Context
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
import com.utn.MascotApp.Publications
import com.utn.MascotApp.R
import kotlinx.android.synthetic.main.fragment_main_menu.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.Timestamp
import com.utn.MascotApp.BottomNavBar
import com.utn.MascotApp.TarjetaPublicacionSmallAdapter
import com.utn.MascotApp.databinding.FragmentProfileBinding
import com.utn.MascotApp.fragments.MiPerfilFragmentDirections
import kotlinx.android.synthetic.main.fragment_main_menu.bottom_navigation
import kotlinx.android.synthetic.main.fragment_profile.*


class MiPerfilFragment : Fragment() {
    val DEFAULT_NO_IMAGE = "https://www.lasommeliere.com/themes/lasommeliere/assets/_custom/images/no-image.png"
    private val fromBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim)}
    private val toBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim)}
    private var _binding: FragmentProfileBinding? = null
    private val publications = mutableListOf<Publications>()
    private lateinit var tarjetaPublicacionSmallAdapter: TarjetaPublicacionSmallAdapter
    private var publicar_button_clicked = false
    private val binding get() = _binding!!
    private lateinit var foundOrLost: String

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
    val foundOrLostParam = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        foundOrLost = "lost"
        val action = MiPerfilFragmentDirections.actionMiPerfilFragmentToFiltros(petNameParam, petTypeParam, petBreedParam,
            petSexParam, petSizeParam, petColorParam, petAgeParam, petLastSeen, petDirectionParam, petNumberParam, petcoordinatesParam,
            petDescriptionParam, foundOrLost).actionId
        BottomNavBar().setBottomNavBar(bottom_navigation, "MiPerfil", findNavController(), R.id.action_miPerfilFragment_to_mainMenuFragment, action, null)
        binding.verPublicaciones.setOnClickListener {
//            val action = MiPerfilFragmentDirections.actionMiPerfilFragmentToMisPublicacionesFragment(
//                publications.toTypedArray()
//            )
            findNavController().navigate(R.id.action_miPerfilFragment_to_misPublicacionesFragment)
        }

        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val prefs = this.requireActivity()
                .getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            findNavController().navigate(R.id.firstLoginFragment)
        }

    }

    override fun onStart(){
        super.onStart()
        initRecyclerViewMascotasVista()
    }

    fun initRecyclerViewMascotasVista(){
        tarjetaPublicacionSmallAdapter = TarjetaPublicacionSmallAdapter(publications)
        binding.publicationsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.publicationsList.adapter = tarjetaPublicacionSmallAdapter
        binding.publicationsList.visibility = View.VISIBLE

//        val layout = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL, false)
//        binding.publicationsList.layoutManager = layout

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
                                    sex = "Macho",
                                    age = "2"
                                )
                                collection_publications.add(publication)
                                publications.clear()
                                publications.addAll(collection_publications)
                                tarjetaPublicacionSmallAdapter.notifyDataSetChanged()
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
