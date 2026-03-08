package com.example.pasitos

data class Cita(
    val id: Int,
    val fecha: String,
    val hora: String,
    val descripcion: String,
    val padreNombre: String
)