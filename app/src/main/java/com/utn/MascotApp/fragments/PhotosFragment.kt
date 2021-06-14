package com.utn.MascotApp.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import java.io.ByteArrayOutputStream
import java.util.*

class PhotosFragment : Fragment() {

    private var _binding: FragmentMisPublicacionesBinding? = null
    private val binding get() = _binding!!


    private val REQUEST_GALLERY = 1001
    private val REQUEST_CAMERA = 1002


    var photo: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


      /*  imgPhoto.isDrawingCacheEnabled = true
        imgPhoto.buildDrawingCache()
        val bitmap = (imgPhoto.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }*/

       // Toast.makeText(context, colorMascota, Toast.LENGTH_LONG).show()

        bottonSiguienteALoclaidad.setOnClickListener{

            val colorMascota =this.arguments?.getString("colorMascota")
            val tipoMascota =this.arguments?.getString("tipoMascota")
            val razaMascota =this.arguments?.getString("razaMascota")
            val sexoMascota =this.arguments?.getString("sexoMascota")
            val tamanioMascota =this.arguments?.getString("tamanioMascota")
            val edadMascota : Int =this.requireArguments().getInt("edadMascota")
            val fechaMascota =this.arguments?.getString("fechaMascota")
            val nombreMascota =this.arguments?.getString("nombreMascota")

            val action = PhotosFragmentDirections.actionPhotosFragmentToLocationFragment(colorMascota,tipoMascota,razaMascota,sexoMascota,tamanioMascota,edadMascota,fechaMascota,nombreMascota)
            findNavController().navigate(action) }


        bottonAtrasAFiltro.setOnClickListener {
            val action = PhotosFragmentDirections.actionPhotosFragmentToFiltros()
            findNavController().navigate(action) }


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