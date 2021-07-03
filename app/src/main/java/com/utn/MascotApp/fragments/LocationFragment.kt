package com.utn.MascotApp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.utn.MascotApp.DatePickerFragment
import com.utn.MascotApp.R
import kotlinx.android.synthetic.main.fragment_filtros.*
import kotlinx.android.synthetic.main.fragment_location.*



class LocationFragment : Fragment() {
    private var petDate2 = "hola"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottonSiguienteALoclaidad.setOnClickListener{
            val petName =this.arguments?.getString("nombreMascota")
            val petType =this.arguments?.getString("tipoMascota")
            val petBreed =this.arguments?.getString("razaMascota")
            val petsex =this.arguments?.getString("sexoMascota")
            val petsize =this.arguments?.getString("tamañoMascota")
            val petColor =this.arguments?.getString("colorMascota")
            val edadMascota =this.arguments?.getString("edadMascota")


            val petDirection1 = petDirection.getEditText()?.getText().toString()
            val petNumber1 = petNumber.getEditText()?.getText().toString()
            val petCoordenadas1 = petCoordenadas.getEditText()?.getText().toString()
            val editTextDescripcion1 = editTextDescripcion.getEditText()?.getText().toString()


            val action = LocationFragmentDirections.actionLocationFragmentToPhotosFragment(petColor,
                petType,petBreed,petsex,petsize,edadMascota,
                petDate2,petName,
                petDirection1  + " " + petNumber1,
                petCoordenadas1,editTextDescripcion1)
            findNavController().navigate(action)}


        bottonAtrasAFiltro.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_filtros) }

        ///////////Calendario///////////
        petDate.setOnClickListener {
            showDatePickerDialog()

        }
//////////////////////////////////////////

    }

    ///////////Calendario///////////
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(parentFragmentManager, "datePicker")
    }

    @SuppressLint("SetTextI18n")
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        petDate1.editText?.setText("$day-$month-$year")
        petDate2 = "$day-$month-$year"

    }
//////////////////////////////////////////
}