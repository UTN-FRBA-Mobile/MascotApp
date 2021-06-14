package com.utn.MascotApp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentFirstLoginBinding

class FirstLoginFragment : Fragment() {
    private var _binding: FragmentFirstLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.logInButton.setOnClickListener{
            findNavController().navigate(R.id.action_firstLoginFragment_to_authFragment)
        }
    }
}