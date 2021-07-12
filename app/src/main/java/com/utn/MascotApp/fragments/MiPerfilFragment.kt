package com.utn.MascotApp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.utn.MascotApp.BottomNavBar
import com.utn.MascotApp.Publications
import com.utn.MascotApp.R
import com.utn.MascotApp.TarjetaPublicacionSmallAdapter
import com.utn.MascotApp.databinding.FragmentProfileBinding
import com.utn.MascotApp.models.User
import kotlinx.android.synthetic.main.fragment_main_menu.*
import kotlinx.android.synthetic.main.fragment_main_menu.bottom_navigation
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*


class MiPerfilFragment : Fragment() {
    private val REQUEST_GALLERY = 1001

    private var _binding: FragmentProfileBinding? = null
    private val publications = mutableListOf<Publications>()
    private lateinit var tarjetaPublicacionSmallAdapter: TarjetaPublicacionSmallAdapter
    private var uri: Uri? = null
    private val binding get() = _binding!!
    var userProfile = User();
    private lateinit var foundOrLost: String
    var imagePath : String = "URL"

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

    override fun onStart(){
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerViewMascotasVista()
        initUserProfileData()

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
                fileUpload()
                updateUserProfile()
                dialog.dismiss()
            };

        dialogBuilder.setNegativeButton(R.string.udpate_user_confirmation_alert_cancel) { dialog, which ->
                dialog.dismiss()
        }

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.udpate_user_confirmation_alert_title)

        binding.guardarCambios.setOnClickListener {
            alert.show()
        }

        binding.cardView.setOnClickListener {
            openGallery_click()
        }
    }

    private fun openGallery_click() {
        if (context?.let { it1 -> ActivityCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED) {
            val permissionFiles = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissionFiles, REQUEST_GALLERY)
        } else {
            viewGallery()
        }
    }

    private fun viewGallery() {
        val intentGalery = Intent(Intent.ACTION_PICK)
        intentGalery.type = "image/*"
        startActivityForResult(intentGalery, REQUEST_GALLERY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_GALLERY -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    viewGallery()
                else
                    Toast.makeText(context, R.string.image_access_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY) {
            userProfileImageView.setImageURI(data?.data)
            uri = data?.data
        }
    }

    private fun fileUpload() {
        if (uri == null) return

        imagePath = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/user-profile-images/$imagePath")

        userProfile.profilePictureImagePath = imagePath
        ref.putFile(uri!!)
            .addOnSuccessListener {
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
                Toast.makeText(requireActivity(),
                    R.string.udpate_user_success_toast, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireActivity(),
                    R.string.udpate_user_fail_toast, Toast.LENGTH_SHORT).show()
            }
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
                    ).getBytes(1024*1024).addOnSuccessListener {
                        val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)


                        userProfileImageView.setImageBitmap(bmp)
                    }
                        .addOnFailureListener { exception ->
                            println("Error getting documents: $exception")
                        }
                }

            }

    }
}
