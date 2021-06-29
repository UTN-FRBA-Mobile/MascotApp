package com.utn.MascotApp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.utn.MascotApp.R
import kotlinx.android.synthetic.main.fragment_location.*



class LocationFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottonSiguienteALoclaidad.setOnClickListener{
            val nombreMascota =this.arguments?.getString("nombreMascota")
            val tipoMascota =this.arguments?.getString("tipoMascota")
            val razaMascota =this.arguments?.getString("razaMascota")
            val sexoMascota =this.arguments?.getString("sexoMascota")
            val tamanoMascota =this.arguments?.getString("tamañoMascota")
            val colorMascota =this.arguments?.getString("colorMascota")
            val edadMascota =this.arguments?.getString("edadMascota")
            val fechaMascota =this.arguments?.getString("fechaMascota")
            val direccion = view.findViewById(R.id.editTextDireccion) as EditText
            val direcNumero = view.findViewById(R.id.editTextNumero) as EditText
            val coordenadas = view.findViewById(R.id.editTextCoordenadas) as EditText
            val descripcion = view.findViewById(R.id.editTextDescripcion) as EditText

            val action = LocationFragmentDirections.actionLocationFragmentToPhotosFragment(colorMascota,
                tipoMascota,razaMascota,sexoMascota,tamanoMascota,edadMascota,fechaMascota,nombreMascota,
                direccion.text.toString() + " " + direcNumero.text.toString(),
                coordenadas.text.toString(),descripcion.text.toString())
            findNavController().navigate(action)}


        bottonAtrasAFiltro.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_filtros) }
    }

}