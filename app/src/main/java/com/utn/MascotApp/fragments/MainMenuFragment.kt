package com.utn.MascotApp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.utn.MascotApp.PagerViewAdapter
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMainMenuBinding
import com.utn.MascotApp.models.Publication
import kotlinx.android.synthetic.main.fragment_main_menu.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class MainMenuFragment : Fragment() {
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

    private var publicar_button_clicked = false

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var listadoBtn: ImageButton
    private lateinit var mapaBtn: ImageButton
    private lateinit var logOutBtn: Button

    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerViewAdapter: PagerViewAdapter

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.publicarItem -> {
                    onPublicarButtonClicked()

                    floatingActionButton_PerdiMiMascota.setOnClickListener() {
                        findNavController().navigate(R.id.action_mainMenuFragment_to_filtros)
                    }

                }
                R.id.misPublicacionesItem -> {
                    findNavController().navigate(R.id.action_mainMenuFragment_to_misPublicacionesFragment)
                }
            }
            true
        }
        listadoBtn = binding.listadoBtn
        mapaBtn = binding.mapaBtn
        mViewPager = binding.viewPager

        //TODO: Mover esto a donde se ponga el boton de logOut
        logOutBtn = binding.logOut

        logOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val prefs = this.requireActivity()
                .getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            findNavController().navigate(R.id.authFragment)
        }
        //Fin TODO

        mapaBtn.setOnClickListener {
            mViewPager.currentItem = 0
        }

        listadoBtn.setOnClickListener {
            mViewPager.currentItem = 1
        }

        mPagerViewAdapter = PagerViewAdapter(childFragmentManager)
        mViewPager.adapter = mPagerViewAdapter
        mViewPager.offscreenPageLimit = 2


        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                changeTabs(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        mViewPager.currentItem = 0
        mapaBtn.setImageResource(R.drawable.map_white)


//        DESCARGAR Imagen
//        private val storage = Firebase.storage
//
//        private val imagesRef = storage.reference.child("publication-images")
//        imagesRef.child("chihuahua-sombrero.jpg").downloadUrl.addOnSuccessListener {
//            println("downloaded successfully -> " + it)
//        }.addOnFailureListener {
//            println("Error downloading file -> " + it.message)
//        }


//        val publication: Publication = Publication(
//            type = "found",
//            species = "dog",
//            breed = "Galgo",
//            createdAt = Date(),
//            lastSeen = Date(),
//            color = "blue",
//            size = "large",
//            name = "Firulais",
//            description = "Encontramos a Firulais en Parque Las Heras",
//            imagePath = "url",
//            address = "Parque Las Heras",
//            geolocation = GeoPoint(-34.5837944, -58.4091335),
//            createdBy = FirebaseAuth.getInstance().currentUser?.uid
//        )

        // AGREGAR PUBLICACION

//        private val db = FirebaseFirestore.getInstance()
//
//        db.collection("publications").add(publication)
//            .addOnSuccessListener {
//                println("Added document succesfully")
//            }
//            .addOnFailureListener { exception ->
//                println("Error adding document: $exception")
//            }
//
        // LEER PUBLICACIONES DE LA DB

//        db.collection("publications")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    println("${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                println("Error getting documents: $exception")
//            }

        var count = 0
        val users = arrayOf("0G0Qm99ydFOl9tjK4H899U8QskP2", "2Uvtjd0Tc0cvJA3Bxn6yt1eec623", "mxs4lKN2fASp6p9eAQVSYTcCnz32", "qWj2WI29rkNBOao3T6pASnWqqEq1", "sYqOlvDfHpNwriHglQYe5F0vJ4g2")
        val species = arrayOf("cat", "dog")
        val types = arrayOf("lost", "found")
        val dogBreeds = arrayOf("Akita", "Beagle", "Boxer", "Caniche", "Dogo", "Mestizo", "Schnauzer", "Siberiano")
        val catBreeds = arrayOf("Angora", "Bengal", "Himalayo", "Mestizo", "Persa", "Ragdoll", "Siames")
        val colors = arrayOf("Negro", "Blanco", "Gris", "Marron", "Beige")
        val sexs = arrayOf("Macho", "Hembra")
        val sizes = arrayOf("Pequeño", "Mediano", "Grande", "Gigante")
        while(count < 50) {
            val userID = users[Random.nextInt(0, 5)]
            val specie = species[Random.nextInt(0, 2)]
            var breed = ""
            if (specie == "dog") {
                breed = dogBreeds[Random.nextInt(0, 8)]
            } else {
                breed = catBreeds[Random.nextInt(0, 7)]
            }
            val color = colors[Random.nextInt(0, 5)]
            val sex = sexs[Random.nextInt(0, 2)]
            val size = sizes[Random.nextInt(0, 4)]
            val type = types[Random.nextInt(0, 2)]
            val geolocation = GeoPoint(Random.nextDouble(-50.0, 50.0), Random.nextDouble(-50.0, 50.0))
            val publication = Publication(type, specie, breed, Date(), Date(), color, size, specie + " " + count, "This is a description", "This is an address", geolocation, breed + ".jpg", userID)


            println(publication)
            count++



            db.collection("publications").add(publication)
                .addOnSuccessListener {
                    println("Added document succesfully")
                }
                .addOnFailureListener { exception ->
                    println("Error adding document: $exception")
                }



        }

    }


    private fun changeTabs(position: Int) {
        if (position == 0) {
            mapaBtn.setImageResource(R.drawable.map_white)
            listadoBtn.setImageResource(R.drawable.list)
        }

        if (position == 1) {
            listadoBtn.setImageResource(R.drawable.list_white)
            mapaBtn.setImageResource(R.drawable.map)
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

}