package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R
import com.example.pasitos.MaestroAdapter
import com.example.pasitos.schemas.Maestro
import com.example.pasitos.dialogs.AgregarMaestroDialog
import com.google.android.material.button.MaterialButton

class MaestrosFragment : Fragment(R.layout.fragment_maestro) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerMaestros)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        val lista = mutableListOf(
            Maestro("user1", "1234", "Juan Perez", "8123456789", "Guarderia 1"),
            Maestro("user2", "1234", "Maria Lopez", "8112345678", "Guarderia 2"),
            Maestro("user3", "1234", "Pedro Ramirez", "8187654321", "Guarderia 3")
        )

        recycler.adapter = MaestroAdapter(lista)

        val btnAgregar = view.findViewById<MaterialButton>(R.id.btnAgregarMaestro)

        btnAgregar.setOnClickListener {

            val dialog = AgregarMaestroDialog()

            dialog.show(parentFragmentManager, "AgregarMaestro")

        }

    }

}
