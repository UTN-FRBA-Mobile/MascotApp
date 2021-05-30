package com.utn.MascotApp.fragments

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.utn.MascotApp.databinding.FragmentInfoMascotaBinding

class MascotaInfoFragment : Fragment() {



    private var _binding: FragmentInfoMascotaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoundLostBinding.inflate(inflater, container, false)
        return binding.root
    }


    private lateinit var btnCamera: Button
    private lateinit var btnUpdatePhoto: Button

    private val REQUEST_GALLERY = 1001
    private val REQUEST_CAMERA = 1002


    var photo: Uri? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCamera = view.findViewById(R.id.button_camera)
        btnCamera.setOnClickListener() {
//            findNavController().navigate(R.id.cameraFragment)
            openCamera_click()
        }

        btnUpdatePhoto = view.findViewById(R.id.button_upload_photo)
        btnUpdatePhoto.setOnClickListener() {
//            findNavController().navigate(R.id.uploadPhotoFragment)
            openGallery_click()
        }
    }


}