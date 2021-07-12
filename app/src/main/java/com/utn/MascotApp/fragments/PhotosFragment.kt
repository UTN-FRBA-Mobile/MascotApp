package com.utn.MascotApp.fragments


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.utn.MascotApp.FiltrosDirections
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentPhotosBinding
import com.utn.MascotApp.models.Publication
import kotlinx.android.synthetic.main.fragment_filtros.*
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.android.synthetic.main.fragment_photos.view.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class PhotosFragment : Fragment() {


    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!
    var simpleFormat2: DateTimeFormatter =  DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private var uri: Uri? = null
    var imagePath : String = "URL"
    var fechaMascota: String? = null
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var lat: Double = 10.0
    var long: Double = 10.0
    var coordenadas: String? = null


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
        createdBy = FirebaseAuth.getInstance().currentUser?.uid,
    )


    private val REQUEST_GALLERY = 1001
    private val REQUEST_CAMERA = 1002
    var photo: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val petNameParam =this.arguments?.getString("petName")
        val petTypeParam =this.arguments?.getString("petType")
        val petBreedParam =this.arguments?.getString("petBreed")
        val petSexParam =this.arguments?.getString("petSex")
        val petSizeParam =this.arguments?.getString("petSize")
        val petColorParam =this.arguments?.getString("petColor")
        val perAgeParam =this.arguments?.getString("petAge")
        val petLastSeenParam = this.arguments?.getString("petLastSeen")
        val petDirectionParam =this.arguments?.getString("petDirection")
        val petNumberParam =this.arguments?.getString("petNumber")
        val petCoordinatesParam =this.arguments?.getString("petCoordinates")
        val petDescriptionParam =this.arguments?.getString("perDescription")
        val foundOrLostParam =this.arguments?.getString("foundOrLost")


        val dialogBuilder  = AlertDialog.Builder(requireActivity())

        dialogBuilder.setMessage("Debe cargar una imagen de su mascota para continuar")
            // if the dialog is cancelable
            .setPositiveButton("Ok") { dialog, which ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Imagen de mascota")




        bottonPublicar.setOnClickListener{
            binding.progressBar1.visibility = View.VISIBLE;
            if(uri == null){
                alert.show()
                return@setOnClickListener
            }
            publication.name = petNameParam
            publication.species =petTypeParam
            publication.breed =petBreedParam
            publication.sex =petSexParam
            publication.size =petSizeParam
            publication.color =petColorParam
            if (perAgeParam != null) {
                publication.age = perAgeParam.toInt()
            }else {publication.age = 0}
            fechaMascota =this.arguments?.getString("petLastSeen")
            dateParse()
            publication.address =petDirectionParam + " " + petNumberParam
            coordenadas = petCoordinatesParam
            coorParse()
            publication.geolocation = GeoPoint(lat,long)
            publication.description = petDescriptionParam
            publication.type = foundOrLostParam


            fileUpload()
            publication.imagePath = imagePath


            val db = FirebaseFirestore.getInstance()
            db.collection("publications").add(publication)
                .addOnSuccessListener {
                    println("Added document succesfully")
                }
                .addOnFailureListener { exception ->
                    println("Error adding document: $exception")
                }
            db.collection("publications")

            binding.progressBar1.visibility = View.GONE;
            val action = PhotosFragmentDirections.actionPhotosFragmentToSplashFragment("Publication")
            findNavController().navigate(action)
        }

        bottonAtrasAPhoto.setOnClickListener{
            val action = PhotosFragmentDirections.actionPhotosFragmentToLocationFragment(
                petNameParam,
                petTypeParam,
                petBreedParam,
                petSexParam,
                petSizeParam,
                petColorParam,
                perAgeParam,
                petLastSeenParam,
                petDirectionParam,
                petNumberParam,
                petCoordinatesParam,
                petDescriptionParam,
                foundOrLostParam)
            findNavController().navigate(action) }

        button_camera.setOnClickListener() {
            openCamera_click()
        }
        button_upload_photo.setOnClickListener() {
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


    private fun openCamera_click() {
        if (context?.let { it1 -> ActivityCompat.checkSelfPermission(it1, Manifest.permission.CAMERA) } == PackageManager.PERMISSION_DENIED
            || context?.let { it1 -> ActivityCompat.checkSelfPermission(it1, Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED
        ) {
            val permissionCamera = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissionCamera, REQUEST_CAMERA)
        } else {
            openCamera()
        }

    }

    private fun viewGallery() {
        val intentGalery = Intent(Intent.ACTION_PICK)
        intentGalery.type = "image/*"
        startActivityForResult(intentGalery, REQUEST_GALLERY)
    }


    private fun openCamera() {
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "New Image")
        photo = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_GALLERY -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    viewGallery()
                else
                    Toast.makeText(context, "No puedes acceder a tus imágenes", Toast.LENGTH_SHORT).show()
            }
            REQUEST_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openCamera()
                else
                    Toast.makeText(context, "No puedes acceder a la cámara", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY) {
            imgPhoto.setImageURI(data?.data)
            uri = data?.data
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA) {
            imgPhoto.setImageURI(photo)
            uri = photo
        }
    }


    private fun fileUpload() {
        if (uri == null) return

        imagePath = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/publication-images/$imagePath")

        ref.putFile(uri!!)
            .addOnSuccessListener {
            }
    }

    private fun dateParse(){
        day = fechaMascota!!.substringBefore("-").toInt()
        month = fechaMascota!!.substringBeforeLast("-").substringAfter("-").toInt()
        year = fechaMascota!!.substringAfterLast("-").toInt() - 1900
    }

    private fun coorParse(){
        lat = coordenadas!!.substringBefore(",").toDouble()
        long = coordenadas!!.substringAfter(",").toDouble()
    }

}


