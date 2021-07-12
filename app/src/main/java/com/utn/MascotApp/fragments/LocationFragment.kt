package com.utn.MascotApp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.utn.MascotApp.DatePickerFragment
import com.utn.MascotApp.R
import kotlinx.android.synthetic.main.fragment_filtros.*
import kotlinx.android.synthetic.main.fragment_location.*



class LocationFragment : Fragment() {

    private lateinit var directionValidation: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val petNameParam =this.arguments?.getString("petName")
        val petTypeParam =this.arguments?.getString("petType")
        val petBreedParam =this.arguments?.getString("petBreed")
        val petSexParam =this.arguments?.getString("petSex")
        val petSizeParam =this.arguments?.getString("petSize")
        val petColorParam =this.arguments?.getString("petColor")
        val perAgeParam =this.arguments?.getString("petAge")
        val petLastSeenParam = this.arguments?.getString("petLastSeen")
        val petDirectionParam =this.arguments?.getString("petDirection")
        val petNumberParam =this.arguments?.getString("petNumber")
        val petCoordinatesParam =this.arguments?.getString("petCoordinates")
        val petDescriptionParam =this.arguments?.getString("perDescription")
        val foundOrLostParam =this.arguments?.getString("foundOrLost")

        if (petLastSeenParam != null) { petLastSeen.editText?.setText(petLastSeenParam) }
        if (petDirectionParam != null) { petDirection.editText?.setText(petDirectionParam) }
        if (petNumberParam != null) { petNumber.editText?.setText(petNumberParam) }
        if (petCoordinatesParam != null) { petCoordinates.editText?.setText(petCoordinatesParam) }
        if (petDescriptionParam != null) { petDescription.editText?.setText(petDescriptionParam) }




        bottonSiguienteALoclaidad.setOnClickListener{

            if(petDirection.getEditText()?.getText().toString() == "" || petNumber.getEditText()?.getText().toString() == "" || petCoordinates.getEditText()?.getText().toString() == "") {

                if(petDirection.getEditText()?.getText().toString() == ""){
                petDirection?.error = "*Campo obligatorio"
                }
                if(petNumber.getEditText()?.getText().toString() == "") {
                    petNumber?.error = "*Campo obligatorio"
                }
                if(petCoordinates.getEditText()?.getText().toString() == "") {
                    petCoordinates?.error = "*Campo obligatorio"
                }

            return@setOnClickListener
            }

            val action = LocationFragmentDirections.actionLocationFragmentToPhotosFragment(
                petNameParam,
                petTypeParam,
                petBreedParam,
                petSexParam,
                petSizeParam,
                petColorParam,
                perAgeParam,
                petLastSeen.getEditText()?.getText().toString(),
                petDirection.getEditText()?.getText().toString(),
                petNumber.getEditText()?.getText().toString(),
                petCoordinates.getEditText()?.getText().toString(),
                petDescription.getEditText()?.getText().toString(),
                foundOrLostParam)
            findNavController().navigate(action)}


        bottonAtrasAFiltro.setOnClickListener {
            val action = LocationFragmentDirections.actionLocationFragmentToFiltros(
                petNameParam,
                petTypeParam,
                petBreedParam,
                petSexParam,
                petSizeParam,
                petColorParam,
                perAgeParam,
                petLastSeen.getEditText()?.getText().toString(),
                petDirection.getEditText()?.getText().toString(),
                petNumber.getEditText()?.getText().toString(),
                petCoordinates.getEditText()?.getText().toString(),
                petDescription.getEditText()?.getText().toString(),
                foundOrLostParam)
            findNavController().navigate(action) }

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
        petLastSeen.editText?.setText("$day-$month-$year")

    }
//////////////////////////////////////////
}