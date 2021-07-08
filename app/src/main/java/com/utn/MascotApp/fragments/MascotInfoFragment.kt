package com.utn.MascotApp.fragments

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMascotInfoBinding
import kotlinx.android.synthetic.main.fragment_mascot_info.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate.now
import java.util.*


class MascotInfoFragment : Fragment() {


    private var _binding: FragmentMascotInfoBinding? = null
    private val binding get() = _binding!!

    lateinit var mShare: Button
    lateinit var mCall: Button

    private val permiso = 1


    val db = FirebaseFirestore.getInstance()

    val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMascotInfoBinding.inflate(inflater, container, false)
        return binding.root
    }


    //    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageView = view.findViewById(R.id.imageView) as ImageView
        var imagev = this.arguments?.getString("image")

        Picasso.get().load(imagev).into(imageView)

        val nameView = view.findViewById(R.id.name) as TextView
        var namev = this.arguments?.getString("name")
        nameView.text = "Nombre: $namev"


        val addressView = view.findViewById(R.id.address) as TextView
        var addresv = this.arguments?.getString("address")
        addressView.text = "Me perdí en: $addresv"


        val descriptionView = view.findViewById(R.id.description) as TextView
        var descriptionv = this.arguments?.getString("description")
        descriptionView.text = "Descripción: $descriptionv"


        val edadView = view.findViewById(R.id.age) as TextView
        var edadv = this.arguments?.getInt("age")
        edadView.text = "Edad: $edadv"

        val sexoView = view.findViewById(R.id.sex) as TextView
        var sexov = this.arguments?.getString("sex")
        sexoView.text = "Sexo: $sexov"

        val colorView = view.findViewById(R.id.color) as TextView
        var colorv = this.arguments?.getString("color")
        colorView.text = "Color: $colorv"


        val breedView = view.findViewById(R.id.breed) as TextView
        var breedv = this.arguments?.getString("breed")
        breedView.text = "Raza: $breedv"


        val lastSeenView = view.findViewById(R.id.lastSeen) as TextView
        var lastSeenv = this.arguments?.getString("lastSeen")

        lastSeenView.text = "Fecha: $lastSeenv"
        var actionFromv = this.arguments?.getString("actionFrom")
        if (actionFromv == "MascotaVistas") {
            call.visibility = View.VISIBLE
            call.text = "CONTACTAR"

// Contactar con el usuario que creó la publicación
            mCall = view.findViewById(R.id.call)

            var userid = this.arguments?.getString("createdBy")

            var phone = ""

            db.collection("users")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.id == userid) {
                            phone = document.data["phone"] as String
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting users: $exception")
                }

            mCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }

        }

        if (actionFromv == "MisPublicaciones") {
            call.visibility = View.VISIBLE
            call.text = "ELIMINAR"

            mCall = view.findViewById(R.id.call)
            mCall.setOnClickListener {
                var publicationId = ""
                val capitalCities = db.collection("publications").whereEqualTo("name", namev)
                    .whereEqualTo("imagePath", imagev).get()

                publicationId = capitalCities.result.documents[0].id

                try {
                    db.collection("publications").document(publicationId)
                        .delete()

                    try {

                        val imagesRef = storage.reference.child("imagev")
                        imagesRef.delete()

                    } catch (e: Exception) {
                        // TODO pantalla que no se pudo eliminar
                        println(e)
                    }
                    // TODO pantalla que se pudo eliminar
                } catch (e: Exception) {
                    // TODO pantalla que no se pudo eliminar
                    println(e)
                }


            }
        }


        // Buttom Share
        mShare = view.findViewById(R.id.share)
        mShare.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    permiso
                )
            }

            val sharingIntent = Intent(Intent.ACTION_SEND)

            // type of the content to be shared - text and image 
            sharingIntent.type = "*/*"

            val shareBody =
                "Nombre: $namev" + '\n' + "Me perdí en: $addresv" + '\n' + "Descripción: $descriptionv" + '\n' +
                        "Edad: $edadv" + '\n' + "Sexo: $sexov" + '\n' + "Color: $colorv" + '\n' + "Raza: $breedv"

            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)


//            // Instanciamos la imagen
////            val gf = BitmapFactory.decodeResource(resources, R.drawable.gf)
//
//            // Con la clase ContentValues() almacenamos los valores que nuestra imagen va compartir en
//            // las aplicaciones, definimos un nombre, formato de la imagen
//            val valoresgf = ContentValues()
//            valoresgf.put(MediaStore.Images.Media.TITLE, "$namev" )
//            valoresgf.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//
//            // Con la clase contentResolver() Proporcionamos el acceso de las aplicaciones a nuestra Galería de imágenes
//            // a contenido externo con EXTERNAL_CONTENT_URI
//            val urigf = contentResolver.insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                valoresgf
//            )
//
//
//            val outstreamgf: OutputStream?
//            try {
//                outstreamgf = contentResolver.openOutputStream(urigf!!)
//
//                // Optimizamos la imagen JPEG que será compartida, con calidad 100
//                gf.compress(Bitmap.CompressFormat.JPEG, 100, outstreamgf)
//                outstreamgf!!.close()
//            } catch (e: Exception) {
//                System.err.println(e.toString())
//            }
//
//            // Suministramos los datos de la imagen que compartiremos con EXTRA_STREAM
//            compartirgf.putExtra(Intent.EXTRA_STREAM, urigf)
//
//            // Iniciamos la Actividad (Activity) con la lógica de nuestro proyecto
//            startActivity(Intent.createChooser(compartirgf, getString(R.string.compartir_gf_txt)))
//        }
//            sharingIntent.putExtra(Intent.EXTRA_STREAM, urigf)

            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }
    }
}
