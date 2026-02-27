package com.example.pasitos.schemas

data class FechaCreate(
    val fecha: String,
    val hora_inicio: String,
    val nino_id: Int,
    val hora_fin: String? = null,
    val tiempo_estancia: Int? = null
)