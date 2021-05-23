package com.utn.MascotApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMisPublicacionesBinding
import kotlinx.android.synthetic.main.fragment_main_menu.*

class MisPublicacionesFragment : Fragment() {
    private var _binding: FragmentMisPublicacionesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisPublicacionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mainMenuItem -> {
                    findNavController().navigate(R.id.action_misPublicacionesFragment_to_mainMenuFragment)
                }
                R.id.publicarItem -> {
                    findNavController().navigate(R.id.action_misPublicacionesFragment_to_publicarFragment2)
                }
            }
            true
        }
    }
}