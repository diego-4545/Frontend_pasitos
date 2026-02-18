package com.example.pasitos.network

import com.example.pasitos.schemas.Maestro
import com.example.pasitos.schemas.Padre
import com.example.pasitos.schemas.Nino

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("padres/")
    fun obtenerPadres(): Call<List<Padre>>

    @POST("padres/")
    fun crearPadre(@Body padre: Padre): Call<Padre>

    @PUT("padres/{id}")
    fun editarPadre(
        @Path("id") id: Int,
        @Body padre: Padre
    ): Call<Padre>

    @DELETE("padres/{id}")
    fun eliminarPadre(
        @Path("id") id: Int
    ): Call<Void>

    @GET("padres/{id}/ninos")
    fun obtenerNinosDePadre(
        @Path("id") id: Int
    ): Call<List<Nino>>

    @GET("ninos")
    fun obtenerNinos(): Call<List<Nino>>

    @POST("ninos")
    fun crearNino(@Body nino: Nino): Call<Nino>

    @DELETE("ninos/{id}")
    fun eliminarNino(@Path("id") id: Int): Call<Void>

    @PUT("ninos/{id}")
    fun editarNino(@Path("id") id: Int, @Body nino: Nino): Call<Nino>

    // Obtener todos los maestros
    @GET("maestros")
    fun obtenerMaestros(): Call<List<Maestro>>

    // Crear maestro
    @POST("maestros")
    fun crearMaestro(@Body maestro: Maestro): Call<Maestro>

    // Editar maestro
    @PUT("maestros/{id}")
    fun editarMaestro(@Path("id") id: Int, @Body maestro: Maestro): Call<Maestro>

    @DELETE("maestros/{id}")
    fun eliminarMaestro(@Path("id") id: Int): Call<Void>


}
