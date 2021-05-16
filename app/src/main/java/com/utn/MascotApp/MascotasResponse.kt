package com.utn.MascotApp

import java.util.*
import com.google.gson.annotations.SerializedName

data class Mascota(@SerializedName("status") var status: String,
                   @SerializedName("images") var message: List<String>){
}

/*
data class Mascota(val foto_mascota: String,
                   val tipo_mascota: String?,
                   val descripcion_busqueda: String?,
                   val direccion: String?,
                   val sexo: String?,
                   val colores: Array<String>?,
                   val fecha_publicacion: Date?,
                   val apariencia_edad: String?,
                   val nombre_mascota: String?,
                   val raza_mascota: String?) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mascota

        if (!colores.contentEquals(other.colores)) return false

        return true
    }

    override fun hashCode(): Int {
        return colores.contentHashCode()
    }
}
 */