package com.utn.MascotApp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface IDogsApi {
    @GET
    suspend fun get_dogs_image_by_breed(@Url url:String): Response<MascotasResponse>
}