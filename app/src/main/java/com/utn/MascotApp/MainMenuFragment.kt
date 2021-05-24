package com.utn.MascotApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class MainMenuFragment : Fragment() {
    private lateinit var btnBuscarMascotaPerdida: Button
    private lateinit var logOut: Button

    lateinit var btnPepe: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
        println("El usuario es" + FirebaseAuth.getInstance().currentUser)
        Toast.makeText(context, "El usuario es" + FirebaseAuth.getInstance().currentUser, Toast.LENGTH_SHORT).show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBuscarMascotaPerdida =  view.findViewById(R.id.button_encontre_una_mascota)
        btnBuscarMascotaPerdida.setOnClickListener() {
            // findNavController().navigate(R.id.foundAndLostFragment)
        }

        logOut = view.findViewById(R.id.logOut)
        logOut.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.authFragment)

        }


    }
}