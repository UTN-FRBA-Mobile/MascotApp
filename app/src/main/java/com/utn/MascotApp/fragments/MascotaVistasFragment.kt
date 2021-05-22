package com.utn.MascotApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.utn.MascotApp.IDogsApi
import com.utn.MascotApp.MascotaAdapter
import com.utn.MascotApp.databinding.FragmentMascotaVistasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MascotaVistasFragment: Fragment(){
    //private var listener: OnFragmentInteractionListener? = null
    private lateinit var mascotaAdapter: MascotaAdapter
    private var _binding: FragmentMascotaVistasBinding? = null
    private val dogImages = mutableListOf<String>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMascotaVistasBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart(){
        super.onStart()
        initRecyclerViewMascotasVista()


    }

    fun initRecyclerViewMascotasVista(){
        mascotaAdapter = MascotaAdapter(dogImages)
        binding.listaTarjetasMascotas.layoutManager = LinearLayoutManager(context)
        binding.listaTarjetasMascotas.adapter = mascotaAdapter
        binding.listaTarjetasMascotas.visibility = View.VISIBLE
        searchPetsByBreed("boxer")

    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create()) // Para parsear el json
            .build()
    }

    private fun searchPetsByBreed(breed:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(IDogsApi::class.java).get_dogs_image_by_breed("$breed/images")
            val dogs = call.body()
            activity?.runOnUiThread{
                binding.progressBar.visibility = View.VISIBLE
                // Esto correrá en el hilo principal, y como lo debemos rellenar con las imgs en este ultimo...
                if (call.isSuccessful){
                    val images : List<String> = dogs?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    mascotaAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }else{
                    showError()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun showError(){
        Toast.makeText(context, "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show()
    }

}