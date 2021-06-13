package com.utn.MascotApp

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract.Helpers.insert
import android.renderscript.Sampler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.compose.ui.input.key.Key.Companion.Insert
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.utn.MascotApp.databinding.FragmentMisPublicacionesBinding
import kotlinx.android.synthetic.main.fragment_filtros.*
import java.util.jar.Manifest


class Filtros : Fragment() {

    private val fromBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim)}
    private val toBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim)}

    private var publicar_button_clicked = false

    private var _binding: FragmentMisPublicacionesBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filtros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bottonSiguienteAPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_filtros_to_photosFragment)
        }



///////////Llenado de spinners///////////

        spinnerTipoMascota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 1) {
                    val listaRazaPerro = resources.getStringArray(R.array.RazaMascotaPerro)
                    val adapter = activity?.let {
                        ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, listaRazaPerro)
                    }
                    spinnerRazaMascota.adapter = adapter
                } else if (position == 2) {
                    val listaRazaGato = resources.getStringArray(R.array.RazaMascotaGato)
                    val adapter = activity?.let {
                        ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, listaRazaGato)
                    }
                    spinnerRazaMascota.adapter = adapter
                } else {
                    val listaTamanio = resources.getStringArray(R.array.RazaMascotaVacio)
                    val adapter = activity?.let {
                        ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, listaTamanio)
                    }
                    spinnerRazaMascota.adapter = adapter
                }}
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

///////////Calendario///////////
        calendario.setOnClickListener { showDatePickerDialog() }
    }
    ///////////Funciones//////////


    ///////////Calendario///////////
    fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(parentFragmentManager, "datePicker")
    }

    fun onDateSelected(day: Int, month: Int, year: Int) {
        calendario.setText("  $day / $month / $year")
    }
    ///////////Calendario//////////


    private fun onPublicarButtonClicked() {
        setVisibility(publicar_button_clicked)
        setAnimation(publicar_button_clicked)
        setClickable(publicar_button_clicked)
        publicar_button_clicked = !publicar_button_clicked
    }

    private fun setVisibility(clicked: Boolean){
        if (!clicked){
            binding.floatingActionButtonEncontreUnaMascota.visibility = View.VISIBLE
            binding.floatingActionButtonPerdiMiMascota.visibility = View.VISIBLE
        } else {
            binding.floatingActionButtonEncontreUnaMascota.visibility = View.INVISIBLE
            binding.floatingActionButtonPerdiMiMascota.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean){
        if (!clicked){
            binding.floatingActionButtonEncontreUnaMascota.startAnimation(fromBotton)
            binding.floatingActionButtonPerdiMiMascota.startAnimation(fromBotton)
        } else {
            binding.floatingActionButtonEncontreUnaMascota.startAnimation(toBotton)
            binding.floatingActionButtonPerdiMiMascota.startAnimation(toBotton)
        }
    }

    private fun setClickable(clicked: Boolean){
        if (!clicked){
            binding.floatingActionButtonEncontreUnaMascota.isClickable = true
            binding.floatingActionButtonPerdiMiMascota.isClickable = true
        } else {
            binding.floatingActionButtonEncontreUnaMascota.isClickable = false
            binding.floatingActionButtonPerdiMiMascota.isClickable = false
        }
    }
}