package com.example.pasitos.network

import com.example.pasitos.schemas.Maestro
import com.example.pasitos.schemas.Padre
import com.example.pasitos.schemas.Nino
import com.example.pasitos.schemas.FechaCreate
import com.example.pasitos.schemas.FechaAbierta



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

    @GET("maestros")
    fun obtenerMaestros(): Call<List<Maestro>>

    @POST("maestros")
    fun crearMaestro(@Body maestro: Maestro): Call<Maestro>

    @PUT("maestros/{id}")
    fun editarMaestro(@Path("id") id: Int, @Body maestro: Maestro): Call<Maestro>

    @DELETE("maestros/{id}")
    fun eliminarMaestro(@Path("id") id: Int): Call<Void>

    @GET("fechas/disponibles/{sucursal_id}")
    fun obtenerNinosDisponibles(
        @Path("sucursal_id") sucursalId: Int
    ): Call<List<Nino>>

    @POST("fechas/")
    fun crearFecha(
        @Body fecha: FechaCreate
    ): Call<Any>

    @GET("fechas/abiertas/{sucursal_id}")
    fun obtenerFechasAbiertas(
        @Path("sucursal_id") sucursalId: Int
    ): Call<List<FechaAbierta>>
}
