package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pasitos.R
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.adapters.EntradasNinosAdapter
import com.example.pasitos.schemas.Nino

class EntradasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entradas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Creamos datos de prueba
        val listaEjemplo = mutableListOf(
            // Estructura: Nino(id, nombre, padre_id, sucursal)
            Nino(1, "Ana Sofía Martínez", 101, 1),
            Nino(2, "Juan Pablo Tamez", 102, 1),
            Nino(3, "Ximena Garza", 103, 2),
            Nino(4, "Mateo Alexander", 104, 1),
            Nino(5, "Victoria Cavazos", 105, 2)
        )
        // 2. Referenciamos el RecyclerView del XML
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerEntradas)

        // 3. Le decimos cómo acomodarse (en lista vertical)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 4. Conectamos el Adapter con los datos de ejemplo
        // Como tu adapter pide una función para "refrescar",
        // por ahora le mandamos un bloque vacío { }
        val adapter = EntradasNinosAdapter(listaEjemplo) {
            // Aquí no hacemos nada porque son datos de prueba
            Toast.makeText(context, "Lista actualizada (Simulado)", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
    }
}