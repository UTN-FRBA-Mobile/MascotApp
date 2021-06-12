package com.utn.MascotApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMascotInfoBinding
import kotlinx.android.synthetic.main.fragment_mascot_info.view.*
import kotlinx.android.synthetic.main.marker_info_contents.view.*


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
//        var publii = this.arguments["publication"];
        val imageView = view.findViewById(R.id.imageView) as ImageView
        var imagev = this.arguments?.getString("image")

//        imageView.setImageResource(imagev)
    }


}
