package com.utn.MascotApp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.firebase.auth.FirebaseAuth
import com.utn.MascotApp.Publications
import com.utn.MascotApp.models.User
import com.utn.MascotApp.R
import kotlinx.android.synthetic.main.fragment_main_menu.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.Timestamp
import com.utn.MascotApp.TarjetaPublicacionSmallAdapter
import com.utn.MascotApp.databinding.FragmentProfileBinding
import com.utn.MascotApp.fragments.MiPerfilFragmentDirections
import kotlinx.android.synthetic.main.fragment_main_menu.bottom_navigation
import kotlinx.android.synthetic.main.fragment_profile.*


class MiPerfilFragment : Fragment() {
    val DEFAULT_NO_IMAGE =
        "https://www.lasommeliere.com/themes/lasommeliere/assets/_custom/images/no-image.png"
    private val fromBotton: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_bottom_anim
        )
    }
    private val toBotton: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_bottom_anim
        )
    }
    private var _binding: FragmentProfileBinding? = null
    private val publications = mutableListOf<Publications>()
    private lateinit var tarjetaPublicacionSmallAdapter: TarjetaPublicacionSmallAdapter
    private var publicar_button_clicked = false
    private val binding get() = _binding!!
    var userProfile = User();
    var newUserProfileData = User();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mainMenuItem -> {
                    findNavController().navigate(R.id.action_miPerfilFragment_to_mainMenuFragment)
                }
                R.id.publicarItem -> {
                    onPublicarButtonClicked()
                }
            }
            true
        }
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

        binding.userPhoneEditText.addTextChangedListener {
            userProfile.phone = binding.userPhoneEditText.text.toString()
            binding.guardarCambios.setEnabled(true)
        }

        binding.userNameEditText.addTextChangedListener {
            userProfile.fullName = binding.userNameEditText.text.toString()
            binding.guardarCambios.setEnabled(true)
        }

        binding.userAddressEditText.addTextChangedListener {
            userProfile.address = binding.userAddressEditText.text.toString()
            binding.guardarCambios.setEnabled(true)
        }

        val dialogBuilder  = AlertDialog.Builder(requireActivity())

        dialogBuilder.setMessage(R.string.udpate_user_confirmation_alert)
            // if the dialog is cancelable
            .setPositiveButton(R.string.udpate_user_confirmation_alert_ok) { dialog, which ->
                updateUserProfile()
                dialog.dismiss()
            };

        dialogBuilder.setNegativeButton(R.string.udpate_user_confirmation_alert_cancel) { dialog, which ->
                dialog.dismiss()
        }

        val alert = dialogBuilder.create()
        alert.setTitle("Test")

        binding.guardarCambios.setOnClickListener {
            alert.show()
        }

    }

    private fun updateUserProfile() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val storage = Firebase.storage
        val imagesRef = storage.reference.child("user-profile-images")
        db.collection("users").document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                print("Failure")
            }
            .addOnFailureListener {
                print("Success")
            }
    }

    private fun onPublicarButtonClicked() {
        setVisibility(publicar_button_clicked)
        setAnimation(publicar_button_clicked)
        setClickable(publicar_button_clicked)
        publicar_button_clicked = !publicar_button_clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.floatingActionButtonEncontreUnaMascota.visibility = View.VISIBLE
            binding.floatingActionButtonPerdiMiMascota.visibility = View.VISIBLE
        } else {
            binding.floatingActionButtonEncontreUnaMascota.visibility = View.INVISIBLE
            binding.floatingActionButtonPerdiMiMascota.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.floatingActionButtonEncontreUnaMascota.startAnimation(fromBotton)
            binding.floatingActionButtonPerdiMiMascota.startAnimation(fromBotton)
        } else {
            binding.floatingActionButtonEncontreUnaMascota.startAnimation(toBotton)
            binding.floatingActionButtonPerdiMiMascota.startAnimation(toBotton)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.floatingActionButtonEncontreUnaMascota.isClickable = true
            binding.floatingActionButtonPerdiMiMascota.isClickable = true
        } else {
            binding.floatingActionButtonEncontreUnaMascota.isClickable = false
            binding.floatingActionButtonPerdiMiMascota.isClickable = false
        }
    }

    override fun onStart() {
        super.onStart()
        initRecyclerViewMascotasVista()
        initUserProfileData()
    }

    fun initRecyclerViewMascotasVista() {
        tarjetaPublicacionSmallAdapter = TarjetaPublicacionSmallAdapter(publications)
        binding.publicationsList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.publicationsList.adapter = tarjetaPublicacionSmallAdapter
        binding.publicationsList.visibility = View.VISIBLE

//        val layout = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL, false)
//        binding.publicationsList.layoutManager = layout

        getPublicationsFromDB()

    }

    private fun getPublicationsFromDB() {
        var collection_publications: MutableList<Publications> = arrayListOf()
        val db = FirebaseFirestore.getInstance()
        val createdBy = FirebaseAuth.getInstance().currentUser?.uid
        val storage = Firebase.storage
        val imagesRef = storage.reference.child("publication-images")
        db.collection("publications")
            .whereEqualTo("createdBy", createdBy)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var map: Map<String, Any> = document.data
                    imagesRef.child(
                        map.get("imagePath").toString()
                    ).downloadUrl.addOnSuccessListener {
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
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }

    }

    private fun initUserProfileData() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val storage = Firebase.storage
        val imagesRef = storage.reference.child("user-profile-images")
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { userData ->

                var map: MutableMap<String, Any>? = userData.data

                binding.userAddressEditText.setText(map?.get("address").toString())
                binding.userEmailEditText.setText(FirebaseAuth.getInstance().currentUser!!.email)
                binding.userNameEditText.setText(map?.get("fullName").toString())
                binding.userPhoneEditText.setText(map?.get("phone").toString())

                userProfile.address = map?.get("address").toString()
                userProfile.fullName = map?.get("fullName").toString()
                userProfile.id = userId
                userProfile.profilePictureImagePath = map?.get("profilePictureImagePath").toString()

                if (map?.get("profilePictureImagePath").toString() != "") {
                    imagesRef.child(
                        map?.get("profilePictureImagePath").toString()
                    ).downloadUrl.addOnSuccessListener {
                        print(it)
                    }
                        .addOnFailureListener { exception ->
                            println("Error getting documents: $exception")
                        }
                }

            }

    }
}
