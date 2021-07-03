package com.utn.MascotApp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.utn.MascotApp.databinding.FragmentMisPublicacionesBinding
import kotlinx.android.synthetic.main.fragment_filtros.*
import kotlinx.android.synthetic.main.fragment_filtros.bottom_navigation
import kotlinx.android.synthetic.main.fragment_profile.view.*


class Filtros : Fragment() {

    private val fromBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim)}
    private val toBotton: Animation by lazy{ AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim)}
    private var publicar_button_clicked = false
    private var _binding: FragmentMisPublicacionesBinding? = null
    private val binding get() = _binding!!
    private lateinit var arrayString: Array<String>
    private  var adapter: ArrayAdapter<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filtros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


///////////Llenado de spinners///////////
        arrayString  = resources.getStringArray(R.array.TipoMascota)
        adapter = context?.let { ArrayAdapter(it, R.layout.dropdownitem,arrayString) }
        petType.setAdapter(adapter)

        arrayString  = resources.getStringArray(R.array.SexoMascota)
        adapter = context?.let { ArrayAdapter(it, R.layout.dropdownitem,arrayString) }
        petSex.setAdapter(adapter)

        arrayString  = resources.getStringArray(R.array.TamañoMascota)
        adapter = context?.let { ArrayAdapter(it, R.layout.dropdownitem,arrayString) }
        petSize.setAdapter(adapter)


        petType.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    val textInputLayout1  = resources.getStringArray(R.array.RazaMascotaPerro)
                    val adapter1: ArrayAdapter<String>? = context?.let { ArrayAdapter(it, R.layout.dropdownitem,textInputLayout1) }
                    petBreed.setAdapter(adapter1)
                }
                1 -> {
                    val textInputLayout1  = resources.getStringArray(R.array.RazaMascotaGato)
                    val adapter1 = context?.let { ArrayAdapter(it, R.layout.dropdownitem,textInputLayout1) }
                    petBreed.setAdapter(adapter1)
                }
        } }



///////////Boton Siguiente///////////
        bottonSiguienteAPhoto.setOnClickListener {
            val petName1 = petName.getEditText()?.getText().toString()
            val petColor1 = petColor.getEditText()?.getText().toString()
            val petAge1 = petAge.getEditText()?.getText().toString()


            val action = FiltrosDirections.actionFiltrosToLocationFragment(
                petColor1,
                petType.text.toString(),
                petBreed.text.toString(),
                petSex.text.toString(),
                petSize.text.toString(),
                petAge1,
                petName1)
            findNavController().navigate(action)
        }
//////////////////////////////////////////


///////////Barra inferior///////////
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miPerfilItem -> {
                    findNavController().navigate(R.id.action_filtros_to_miPerfilFragment)
                }
                R.id.mainMenuItem -> {
                    findNavController().navigate(R.id.action_filtros_to_mainMenuFragment)
                }
            }
            true
        }
//////////////////////////////////////////




    }


///////////Funciones//////////


///////////Barra inferior///////////
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
//////////////////////////////////////////

}






