package com.example.pasitos.schemas

data class PagoResponse(
    val id: Int,
    val nino_id: Int,
    val mes: Int,
    val anio: Int,
    val deuda: Double,
    val pago: Double,
    val estado: Int
)