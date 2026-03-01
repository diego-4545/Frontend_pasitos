package com.example.pasitos.schemas

data class PagoUpdate(
    val deuda: Double? = null,
    val pago: Double? = null,
    val estado: Int? = null
)