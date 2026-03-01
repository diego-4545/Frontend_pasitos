package com.example.pasitos.schemas

data class FechaAbierta(
    val fecha_id: Int,
    val hora_inicio: String,
    val nino_id: Int,
    val nombre: String,
    val fecha: String,
    var hora_fin: String? = null,
    var tiempo_estancia: Int? = null,
    val paquete: Int
)