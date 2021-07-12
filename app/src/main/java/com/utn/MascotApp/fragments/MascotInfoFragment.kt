package com.utn.MascotApp.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.utn.MascotApp.BottomNavBar
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMascotInfoBinding
import kotlinx.android.synthetic.main.fragment_main_menu.bottom_navigation
import kotlinx.android.synthetic.main.fragment_mascot_info.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MascotInfoFragment : Fragment() {


    private var _binding: FragmentMascotInfoBinding? = null
    private val binding get() = _binding!!

    lateinit var mShare: Button
    lateinit var mCall: Button

    private val permiso = 1

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
        nameView.text = "Nombre: " + (namev ?: "-")


        val addressView = view.findViewById(R.id.address) as TextView
        var addresv = this.arguments?.getString("address")
        addressView.text = "Me perdí en: " + (addresv ?: "-")


        val descriptionView = view.findViewById(R.id.description) as TextView
        var descriptionv = this.arguments?.getString("description")
        descriptionView.text = "Descripción: " + (descriptionv ?: "-")


        val edadView = view.findViewById(R.id.age) as TextView
        var edadv = this.arguments?.getInt("age")
        edadView.text = "Edad: " + (edadv ?: "-")

        val sexoView = view.findViewById(R.id.sex) as TextView
        var sexov = this.arguments?.getString("sex")
        sexoView.text = "Sexo: " + (sexov ?: "-")

        val colorView = view.findViewById(R.id.color) as TextView
        var colorv = this.arguments?.getString("color")
        colorView.text = "Color: " + (colorv ?: "-")


        val breedView = view.findViewById(R.id.breed) as TextView
        var breedv = this.arguments?.getString("breed")
        breedView.text = "Raza: " + (breedv ?: "-")


        val lastSeenView = view.findViewById(R.id.lastSeen) as TextView
        var lastSeenv = this.arguments?.getString("lastSeen")
        lastSeenView.text = "Fecha: " + (lastSeenv ?: "-")

        var actionFromv = this.arguments?.getString("actionFrom")
        val action = MascotInfoFragmentDirections.actionMascotInfoFragmentToFiltros(petNameParam, petTypeParam, petBreedParam,
            petSexParam, petSizeParam, petColorParam, petAgeParam, petLastSeen, petDirectionParam, petNumberParam, petcoordinatesParam,
            petDescriptionParam, foundOrLost).actionId

        if (actionFromv == "MascotaVistas") {
            BottomNavBar().setBottomNavBar(bottom_navigation, "MainMenu", findNavController(), null, action, R.id.action_mascotInfoFragment_to_miPerfilFragment)
            call.visibility = View.VISIBLE
            call.text = "CONTACTAR"

// Contactar con el usuario que creó la publicación
            mCall = view.findViewById(R.id.call)

            var userid = this.arguments?.getString("createdBy")
            var phone = ""
            var usuario_nombre = ""
            db.collection("users")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.id == userid) {
                            phone = document.data["phone"] as String
                            usuario_nombre = document.data["fullName"] as String
                            binding.usuario.text = usuario_nombre
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

            BottomNavBar().setBottomNavBar(bottom_navigation, "MiPerfil", findNavController(), R.id.action_mascotInfoFragment_to_mainMenuFragment, action, null)
            call.visibility = View.VISIBLE
            call.text = "ELIMINAR"

            binding.usuario.text = "Publicado por mi"
            mCall = view.findViewById(R.id.call)
            mCall.setOnClickListener {

                var publicationId = ""
//                try {
                // TODO - publicationid
                var pathimage = imagev?.substringAfter("%2F")?.substringBefore("?alt")
                val publicationsByNameAndImage =
                    db.collection("publications").whereEqualTo("name", namev)
                        .whereEqualTo("imagePath", pathimage).get()

                try {
                    Thread.sleep(1_000)
                    publicationId = publicationsByNameAndImage.result.documents[0].id
                } catch (e: Exception) {
                    println("Error get publication: $e")
                }


                try {
                    db.collection("publications").document(publicationId).delete()
                    try {
                        val imagesRef = storage.reference.child(pathimage.toString())
                        imagesRef.delete()
//                        val imagesRef = pathimage?.let { it1 -> storage.reference.child(it1) }
//                        imagesRef?.delete()
                    } catch (e: Exception) {
                        println("Error delete image storage: $e")
                    }

                    val action =
                        MascotInfoFragmentDirections.actionMascotInfoFragmentToSplashFragment("Delete")
                    findNavController().navigate(action)
                } catch (e: Exception) {
                    println("Error delete image storage: $e")
                }
            }
        }


        // Buttom Share
        mShare = view.findViewById(R.id.share)
        mShare.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
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
                "Nombre: " + (namev ?: "-") + '\n' +
                        "Me perdí en: " + (addresv ?: "-") + '\n' +
                        "Descripción: " + (descriptionv ?: "-") + '\n' +
                        "Edad: " + (edadv ?: "-") + '\n' +
                        "Sexo:" + (sexov ?: "-") + '\n' +
                        "Color:" + (colorv ?: "-") + '\n' +
                        "Raza: " + (breedv ?: "-")

            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
//            var resolver = requireActivity().contentResolver
//            val url = URL(imagev)
//            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//            connection.setDoInput(true)
//            connection.connect()
//            val input: InputStream = connection.getInputStream()
//            var btmp = BitmapFactory.decodeStream(input)
//
//            sharingIntent.putExtra(Intent.EXTRA_STREAM, btmp)

            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }
    }
}
