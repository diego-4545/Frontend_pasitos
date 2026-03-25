package com.example.pasitos.schemas

data class Cita(
    val id: Int,
    val fecha: String,
    val hora: String,
    val descripcion: String,
    val padreNombre: String
)

data class CitaRequest(
    val padre_id: Int,
    val fecha: String,
    val hora: String,
    val descripcion: String
)

data class CitaResponse(
    val id: Int,
    val padre_id: Int,
    val fecha: String,
    val hora: String,
    val descripcion: String
)