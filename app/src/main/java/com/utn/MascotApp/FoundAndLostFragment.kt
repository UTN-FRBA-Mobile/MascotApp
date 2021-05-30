 package com.utn.MascotApp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.utn.MascotApp.databinding.FragmentFoundLostBinding

class FoundAndLostFragment : Fragment() {


    private var _binding: FragmentFoundLostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoundLostBinding.inflate(inflater, container, false)
        return binding.root
    }


    private lateinit var btnCamera: Button
    private lateinit var btnUpdatePhoto: Button

    private val REQUEST_GALLERY = 1001
    private val REQUEST_CAMERA = 1002


    var photo: Uri? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.imgPhoto.setOnClickListener() {
//
////            val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
////
////            // Inflate a custom view using layout inflater
////            val view = inflater.inflate(n,null)
//
//
//            val popupWindow = PopupWindow(
//                    view, // Custom view to show in popup window
//                    LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
//                    LinearLayout.LayoutParams.WRAP_CONTENT // Window height
//            )
//
//
//            // Create a new slide animation for popup window enter transition
//            val slideIn = Slide()
//            slideIn.slideEdge = Gravity.TOP
//            popupWindow.enterTransition = slideIn
//
//            // Slide animation for popup window exit transition
//            val slideOut = Slide()
//            slideOut.slideEdge = Gravity.RIGHT
//            popupWindow.exitTransition = slideOut
//
//
//            // Get the widgets reference from custom view
//            val tv = view.findViewById<TextView>(R.id.text_view)
//            val buttonPopup = view.findViewById<Button>(R.id.button_popup)
//
//            // Set click listener for popup window's text view
//            tv.setOnClickListener {
//                // Change the text color of popup window's text view
//                tv.setTextColor(Color.RED)
//            }
//
//            // Set a click listener for popup's button widget
//            buttonPopup.setOnClickListener {
//                // Dismiss the popup window
//                popupWindow.dismiss()
//            }
//
//            // Set a dismiss listener for popup window
//            popupWindow.setOnDismissListener {
//                Toast.makeText(context, "Popup closed", Toast.LENGTH_SHORT).show()
//            }
//
//
//            // Finally, show the popup window on app
//            TransitionManager.beginDelayedTransition(binding.root)
//            popupWindow.showAtLocation(
//                    binding.imgPhoto, // Location to display popup window
//                    Gravity.CENTER, // Exact position of layout to display popup
//                    0, // X offset
//                    0 // Y offset
//            )
//
//
//        }

        btnCamera = view.findViewById(R.id.button_camera)
        btnCamera.setOnClickListener() {
//            findNavController().navigate(R.id.cameraFragment)
            openCamera_click()
        }

        btnUpdatePhoto = view.findViewById(R.id.button_upload_photo)
        btnUpdatePhoto.setOnClickListener() {
//            findNavController().navigate(R.id.uploadPhotoFragment)
            openGallery_click()
        }

//        binding.imgPhoto.setOnClickListener(){
//            var window = PopupWindow(context)
//            val view = layoutInflater.inflate(R.layout.layout_pupop, nu)
//        }

    }

    private fun getSystemService() {

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





