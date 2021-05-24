package com.utn.MascotApp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentAuthBinding


class AuthFragment: Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var signUpButton: Button
    private lateinit var logInButton: Button
    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //signUpButton = view.findViewById(R.id.signUpButton)
        signUpButton = binding.signUpButton
        logInButton = binding.logInButton
        email = binding.email
        password = binding.password
        setup()
        checkSession()
    }

    private fun checkSession() {
        val prefs = this.getActivity()?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val sessionEmail = prefs?.getString("email", null)
        if (sessionEmail != null) {
            findNavController().navigate(R.id.mainMenuFragment)
        }
    }

    private fun setup() {
        signUpButton.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveSessionAndNavigateHome()
                    } else {
                        showAlert(it.exception.toString())
                    }
                }
            } else {
                showEmptyValuesAlert()
            }
        }

        logInButton.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveSessionAndNavigateHome()
                    } else {
                        showAlert(it.exception.toString())
                    }
                }
            } else {
                showEmptyValuesAlert()
            }
        }
    }

    private fun showAlert(errorMessage: String) {
        val alertBuilder = context?.let {
            AlertDialog.Builder(it)
                .setTitle("Error")
                .setMessage("Se ha producido un error autenticando al usuario, " + errorMessage)
                .setPositiveButton("Aceptar", null)
        }
        alertBuilder?.create()?.show()
    }

    private fun showEmptyValuesAlert() {
        val alertBuilder = context?.let {
            AlertDialog.Builder(it)
                .setTitle("Error")
                .setMessage("Por favor ingrese un Usuario y Password")
                .setPositiveButton("Aceptar", null)
        }
        alertBuilder?.create()?.show()
    }


    private fun saveSessionAndNavigateHome() {

        val prefs = this.requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email.text.toString())
        prefs.apply()
        findNavController().navigate(R.id.mainMenuFragment)
    }
}