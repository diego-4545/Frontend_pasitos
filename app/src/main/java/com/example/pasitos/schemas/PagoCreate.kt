package com.example.pasitos.schemas

data class PagoCreate(
    val nino_id: Int,
    val mes: Int,
    val anio: Int,
    val deuda: Double,
    val pago: Double = 0.0,
    val estado: Int = 0
)