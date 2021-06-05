package com.utn.MascotApp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView



class MascotaAdapter(private val publications_list: List<Publications>): RecyclerView.Adapter<TarjetaMascotaHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetaMascotaHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TarjetaMascotaHolder(layoutInflater.inflate(R.layout.tarjeta_mascota, parent, false))
    }

    override fun onBindViewHolder(holder: TarjetaMascotaHolder, position: Int) {
        val item = publications_list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = publications_list.size

}