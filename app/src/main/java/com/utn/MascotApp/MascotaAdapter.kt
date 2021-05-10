package com.utn.MascotApp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class MascotaAdapter(private val mascotas: List<Mascota>): RecyclerView.Adapter<MascotaAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemIndex = position - 1 // el primer item es el encabezado

        holder.itemView.findViewById<TextView>(R.id.card_title).text = "Perro " + itemIndex.toString()
        holder.itemView.findViewById<TextView>(R.id.card_secondary_text).text = "Perdido en tal lado"

        // solo si hay imagen usar holder.itemView.image
        Picasso.get().load(Uri.parse(mascotas[itemIndex].foto_mascota)).into(holder.itemView.findViewById<ImageView>(R.id.card_imagen_mascota))
        //Picasso.get().load(Uri.parse(mascotas[itemIndex].message)).into(holder.itemView.findViewById<ImageView>(R.id.card_imagen_mascota))
    }

    override fun getItemCount(): Int = mascotas.size + 1  // el primer item es el encabezado

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}