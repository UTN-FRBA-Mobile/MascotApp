package com.utn.MascotApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMascotInfoBinding
import java.time.LocalDate.now


class MascotInfoFragment : Fragment() {


    private var _binding: FragmentMascotInfoBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMascotInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageView = view.findViewById(R.id.imageView) as ImageView
        var imagev = this.arguments?.getString("image")

        Picasso.get().load(imagev).into(imageView)

        val nameView = view.findViewById(R.id.name) as TextView
        var namev = this.arguments?.getString("name")
        nameView.text =  "Nombre: " + namev


        val addressView = view.findViewById(R.id.address) as TextView
        var addresv = this.arguments?.getString("address")
        addressView.text = "Me perdí en: " + addresv


        val descriptionView = view.findViewById(R.id.description) as TextView
        var descriptionv = this.arguments?.getString("description")
        descriptionView.text = "Descripción: " + descriptionv


        val edadView = view.findViewById(R.id.age) as TextView
        var edadv = this.arguments?.getInt("age")
        edadView.text = "Edad: " + edadv

        val sexoView = view.findViewById(R.id.sex) as TextView
        var sexov = this.arguments?.getString("sex")
        sexoView.text = "Sexo: " +sexov

        val colorView = view.findViewById(R.id.color) as TextView
        var colorv = this.arguments?.getString("color")
        colorView.text = "Color: " + colorv


        val breedView = view.findViewById(R.id.breed) as TextView
        var breedv = this.arguments?.getString("breed")
        breedView.text = "Raza: " + breedv


        val lastSeenView = view.findViewById(R.id.lastSeen) as TextView
        var lastSeenv = this.arguments?.getString("lastSeen")
        var date =  lastSeenv?.replace("\"", "");


        //TODO fecha
        lastSeenView.text = "Fecha: " + now().toString()

    }

}
