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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentPhotosBinding

class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }


    private lateinit var btnCamera: Button
    private lateinit var btnUpdatePhoto: Button

    private val REQUEST_GALLERY = 1001
    private val REQUEST_CAMERA = 1002


    var photo: Uri? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCamera = view.findViewById(R.id.button_camera)
        btnCamera.setOnClickListener() {
            openCamera_click()
        }

        btnUpdatePhoto = view.findViewById(R.id.button_upload_photo)
        btnUpdatePhoto.setOnClickListener() {
            openGallery_click()
        }
    }


    private fun openGallery_click() {
//             verificar q version de android esta instalada en el telefono
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        if (context?.let { it1 -> ActivityCompat.checkSelfPermission(it1, android.Manifest.permission.READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED) {
            var permissionFiles = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
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
            binding.imgPhoto.setImageURI(data?.data)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA) {
            binding.imgPhoto.setImageURI(photo)
        }

    }

}