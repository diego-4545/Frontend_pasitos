package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.NinoAdapter
import com.example.pasitos.R
import com.example.pasitos.schemas.Nino   // ← IMPORT CORRECTO
import com.example.pasitos.dialogs.AgregarNinoDialog
import com.google.android.material.button.MaterialButton

class NinoFragment : Fragment(R.layout.fragment_nino) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerNinos)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        val lista = mutableListOf(
            Nino("Juan Perez", "Carlos Perez", "Guarderia A"),
            Nino("Maria Lopez", "Luis Lopez", "Guarderia B"),
            Nino("Maria  Maria Lopez Maria Lopez", "Luis Lopez", "Guarderia B"),
            Nino("Maria Lopez", "Luis Lopez", "Guarderia B"),
            Nino("Carlos Diaz", "Pedro Diaz", "Guarderia C")
        )

        recycler.adapter = NinoAdapter(lista)

        recycler.adapter = NinoAdapter(lista)

        val btnAgregar = view.findViewById<MaterialButton>(R.id.btnAgregar)

        btnAgregar.setOnClickListener {
            val dialog = AgregarNinoDialog()
            dialog.show(parentFragmentManager, "AgregarNino")
        }

    }

}
