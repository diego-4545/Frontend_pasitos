package com.example.pasitos.schemas

data class Padre(
    val id: Int? = null,
    var nombre: String,
    var telefono: String,
    var hijos: MutableList<String> = mutableListOf()
)
