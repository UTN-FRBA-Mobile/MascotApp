package com.utn.MascotApp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView



class TarjetaPublicacionSmallAdapter(private val publications_list: List<Publications>): RecyclerView.Adapter<TarjetaPublicacionSmallHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetaPublicacionSmallHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TarjetaPublicacionSmallHolder(layoutInflater.inflate(R.layout.tarjeta_publicacion_small, parent, false))
    }

    override fun onBindViewHolder(holder: TarjetaPublicacionSmallHolder, position: Int) {
        val item = publications_list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = publications_list.size

}