package com.utn.MascotApp.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.utn.MascotApp.FiltrosDirections
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMisPublicacionesBinding
import com.utn.MascotApp.databinding.FragmentPhotosBinding
import com.utn.MascotApp.models.Publication
import kotlinx.android.synthetic.main.fragment_filtros.*
import kotlinx.android.synthetic.main.fragment_photos.*
import java.util.*

class PhotosFragment : Fragment() {

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

    private val REQUEST_GALLERY = 1001
    private val REQUEST_CAMERA = 1002


    var photo: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val colorMascota =this.arguments?.getString("colorMascota")
        val tipoMascota =this.arguments?.getString("tipoMascota")
        val razaMascota =this.arguments?.getString("razaMascota")
        val sexoMascota =this.arguments?.getString("sexoMascota")
        val tamanioMascota =this.arguments?.getString("tamanioMascota")
        val edadMascota : Int? =this.arguments?.getInt("edadMascota")
        val edadMascota1 : Int = edadMascota!!
        val fechaMascota =this.arguments?.getString("fechaMascota")


        Toast.makeText(context, colorMascota, Toast.LENGTH_LONG).show()

        bottonAtrasAFiltro.setOnClickListener {
                  //findNavController().navigate(R.id.action_photosFragment_to_filtros)
            val action = PhotosFragmentDirections.actionPhotosFragmentToFiltros(colorMascota,tipoMascota,razaMascota,sexoMascota,tamañoMascota,edadMascota,fechaMascota)
            findNavController().navigate(action)
              }




        bottonSiguienteALoclaidad.setOnClickListener{

        publication.color = colorMascota

        val db = FirebaseFirestore.getInstance()

        db.collection("publications").add(publication)
            .addOnSuccessListener {
                println("Added document succesfully")
            }
            .addOnFailureListener { exception ->
                println("Error adding document: $exception")
            }

            val citiesRef = db.collection("publications")

            var query = citiesRef.whereEqualTo("breed","Tony")

        }

        button_camera.setOnClickListener() {
                         openCamera_click()
                     }
        button_upload_photo.setOnClickListener() {
                         openGallery_click()
                     }
    }


    private fun openGallery_click() {
//             verificar q version de android esta instalada en el telefono
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        if (context?.let { it1 -> ActivityCompat.checkSelfPermission(it1, android.Manifest.permission.READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED) {
            val permissionFiles = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissionFiles, REQUEST_GALLERY)
        } else {
            viewGallery()
        }
//        }
    }


    private fun openCamera_click() {
//         verificar q version de android esta instalada en el telefono
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

        if (context?.let { it1 -> ActivityCompat.checkSelfPermission(it1, android.Manifest.permission.CAMERA) } == PackageManager.PERMISSION_DENIED
            || context?.let { it1 -> ActivityCompat.checkSelfPermission(it1, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED
        ) {
            val permissionCamera = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissionCamera, REQUEST_CAMERA)
        } else {
            openCamera()
        }
//            }else{
//                openCamera()
//            }
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
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA) {
            imgPhoto.setImageURI(photo)
        }

    }

}