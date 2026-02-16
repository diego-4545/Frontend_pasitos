package com.example.pasitos.schemas

data class Padre(
    var nombre: String,
    var telefono: String,
    var hijos: MutableList<String> = mutableListOf()
)
