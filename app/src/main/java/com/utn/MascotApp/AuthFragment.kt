package com.utn.MascotApp

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
    }

    private fun setup() {
        signUpButton.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        navigateHome()
                    } else {
                        showAlert()
                    }
                }
            }
        }

        logInButton.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        navigateHome()
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert() {
        val alertBuilder = context?.let {
            AlertDialog.Builder(it)
                .setTitle("Error")
                .setMessage("Se ha producido un error autenticando al usuario")
                .setPositiveButton("Aceptar", null)
        }
        alertBuilder?.create()?.show()
    }

    private fun navigateHome() {
        findNavController().navigate(R.id.mainMenuFragment)
    }
}