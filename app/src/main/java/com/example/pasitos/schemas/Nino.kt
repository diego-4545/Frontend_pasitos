package com.example.pasitos.schemas

data class Nino(
    val id: Int? = null,
    val nombre: String,
    val padre_id: Int,
    val sucursal: Int,
    val paquete: Int
)