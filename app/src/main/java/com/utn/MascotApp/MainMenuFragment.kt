package com.utn.MascotApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MainMenuFragment : Fragment() {

    lateinit var btnBuscarMascotaPerdida: Button
    lateinit var btnPepe: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBuscarMascotaPerdida =  view.findViewById(R.id.button_encontre_una_mascota)
        btnBuscarMascotaPerdida.setOnClickListener() {
            findNavController().navigate(R.id.foundAndLostFragment)
        }
    }
}