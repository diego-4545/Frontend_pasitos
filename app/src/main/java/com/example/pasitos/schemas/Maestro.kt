package com.example.pasitos.schemas

data class Maestro(
    val id: Int? = null,
    val nombre: String,
    val telefono: String,
    val username: String,
    val password: String,
    val sucursal: Int

)
