package com.utn.MascotApp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView



class MascotaAdapter(private val publications_list: List<Publications>, private val navController: NavController, private val actionFrom: String): RecyclerView.Adapter<TarjetaMascotaHolder>(){
    // *************************************************
    // actionFrom: "MascotaVistas" or "MisPublicaciones"
    // *************************************************

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetaMascotaHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TarjetaMascotaHolder(
            layoutInflater.inflate(R.layout.tarjeta_mascota, parent, false),
            navController
        )
    }

    override fun onBindViewHolder(holder: TarjetaMascotaHolder, position: Int) {
        val item = publications_list[position]
        holder.bind(item, actionFrom)
    }

    override fun getItemCount(): Int = publications_list.size

}