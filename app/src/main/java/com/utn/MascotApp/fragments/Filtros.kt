package com.utn.MascotApp

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.utn.MascotApp.databinding.FragmentMisPublicacionesBinding
import kotlinx.android.synthetic.main.fragment_filtros.*
import kotlinx.android.synthetic.main.fragment_filtros.bottom_navigation
import kotlinx.android.synthetic.main.fragment_main_menu.*
import java.time.format.DateTimeFormatter


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
         var edadmascot : Int
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.misPublicacionesItem -> {
                    findNavController().navigate(R.id.action_filtros_to_misPublicacionesFragment)
                }

                R.id.mainMenuItem -> {
                    findNavController().navigate(R.id.action_filtros_to_mainMenuFragment)
                }
            }
            true
        }

        bottonSiguienteAPhoto.setOnClickListener {


            val tipoMascota = spinnerTipoMascota.getItemAtPosition(spinnerTipoMascota.selectedItemPosition).toString()
            val razaMascota = spinnerRazaMascota.getItemAtPosition(spinnerRazaMascota.selectedItemPosition).toString()
            val sexoMascota = spinnerSexoMascota.getItemAtPosition(spinnerSexoMascota.selectedItemPosition).toString()
            val tamanioMascota = spinnerTamañoMascota.getItemAtPosition(spinnerTamañoMascota.selectedItemPosition).toString()
            val colorMascota = view.findViewById(R.id.editTextColor) as EditText
            val edadMascota = view.findViewById(R.id.editTextEdad) as EditText
            val fechaMascota = view.findViewById(R.id.calendario) as EditText
            val nombreMascota = view.findViewById(R.id.editTextNombre) as EditText

            try{
                if (edadMascota.text.toString().toInt()  == null){
                     edadmascot = 1
                } else {
                    edadmascot = edadMascota.text.toString().toInt()
                }

            }catch (e: NumberFormatException){
                Toast.makeText(context, "Se asume edad 1.", Toast.LENGTH_LONG)
                edadmascot = 1
            }





           // Toast.makeText(context, fechaMascota.text, Toast.LENGTH_LONG).show()

            val action = FiltrosDirections.actionFiltrosToPhotosFragment(colorMascota.text.toString(),tipoMascota,
                razaMascota,sexoMascota,tamanioMascota,edadmascot,fechaMascota.text.toString(),nombreMascota.text.toString())
            findNavController().navigate(action)

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
                } else if
                        (position == 2) {
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



