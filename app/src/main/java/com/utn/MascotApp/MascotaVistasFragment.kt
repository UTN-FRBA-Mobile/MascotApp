package com.utn.MascotApp

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.utn.MascotApp.databinding.FragmentMascotaVistasBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MascotaVistasFragment: Fragment(R.layout.tarjeta_mascota){
    //private var listener: OnFragmentInteractionListener? = null
    private lateinit var mascotaAdapter: MascotaAdapter
    private var _binding: FragmentMascotaVistasBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onStart(){
        super.onStart()

        val service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // Para parsear el json
            .baseUrl("https://dog.ceo/api/breeds/image/random")
            .build()
            .create(IDogsApi::class.java)

        service.get_dog_image().enqueue(object: Callback<Mascotas>{

            override fun onResponse(call: Call<Mascotas>, response: Response<Mascotas>) {
                Toast.makeText(activity, response.body()!!.toString(), Toast.LENGTH_SHORT).show()

                mascotaAdapter = MascotaAdapter(response.body()!!.mascotas)
                binding.list.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = mascotaAdapter
                }
                binding.list.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
            override fun onFailure(call: Call<Mascotas>, error: Throwable) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
                //Toast.makeText(activity, "No se encontraron mascotas! :(", Toast.LENGTH_SHORT).show()
            }
        })
    }

}