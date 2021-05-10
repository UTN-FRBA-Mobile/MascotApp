package com.utn.MascotApp

import retrofit2.Call
import retrofit2.http.GET

interface IDogsApi {
    @GET("random")
    fun get_dog_image(): Call<Mascotas>
}