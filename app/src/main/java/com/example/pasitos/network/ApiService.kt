package com.example.pasitos.network

import com.example.pasitos.schemas.Maestro
import com.example.pasitos.schemas.Padre
import com.example.pasitos.schemas.Nino
import com.example.pasitos.schemas.FechaCreate
import com.example.pasitos.schemas.FechaAbierta
import com.example.pasitos.schemas.FechaUpdate
import com.example.pasitos.schemas.PagoCreate
import com.example.pasitos.schemas.PagoResponse
import com.example.pasitos.schemas.PagoUpdate
import com.example.pasitos.schemas.Cita
import com.example.pasitos.schemas.CitaRequest
import com.example.pasitos.schemas.CitaResponse




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

    @PUT("fechas/{id}")
    fun actualizarFecha(
        @Path("id") fechaId: Int,
        @Body fechaUpdate: FechaUpdate
    ): Call<FechaAbierta>

    @POST("pagos/")
    fun crearPago(
        @Body pago: PagoCreate
    ): Call<PagoResponse>

    @GET("pagos/nino/{nino_id}")
    fun obtenerPagosPorNino(
        @Path("nino_id") ninoId: Int
    ): Call<List<PagoResponse>>

    @PUT("pagos/{pago_id}")
    fun actualizarPago(
        @Path("pago_id") pagoId: Int,
        @Body pagoUpdate: PagoUpdate
    ): Call<PagoResponse>

    @POST("pagos/registrar_salida/{nino_id}")
    fun registrarSalida(
        @Path("nino_id") ninoId: Int,
        @Query("paquete") paquete: Int,
        @Query("horas_totales") horasTotales: Int
    ): Call<PagoResponse>


    @GET("fechas/nino/{nino_id}/mes/{mes}/anio/{anio}")
    fun obtenerFechasPorNinoMes(
        @Path("nino_id") ninoId: Int,
        @Path("mes") mes: Int,
        @Path("anio") anio: Int
    ): Call<List<FechaAbierta>>

    @GET("agenda/")
    fun obtenerCitas(): Call<List<CitaResponse>>

    @POST("agenda/")
    fun crearCita(@Body cita: CitaRequest): Call<CitaResponse>

    @PUT("agenda/{cita_id}")
    fun editarCita(@Path("cita_id") citaId: Int, @Body cita: CitaRequest): Call<CitaResponse>

    @DELETE("agenda/{cita_id}")
    fun eliminarCita(@Path("cita_id") citaId: Int): Call<Void>

}
