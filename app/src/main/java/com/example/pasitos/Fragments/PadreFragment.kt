package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.adapters.PadreAdapter
import com.example.pasitos.R
import com.example.pasitos.schemas.Padre
import com.example.pasitos.dialogs.AgregarPadreDialog
import com.google.android.material.button.MaterialButton

class PadreFragment : Fragment(R.layout.fragment_padre) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerPadres)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        val lista = mutableListOf(
            Padre("Carlos Perez", "8123456789"),
            Padre("Luis Lopez", "8112345678"),
            Padre("Pedro Diaz", "8187654321")
        )

        recycler.adapter = PadreAdapter(lista)

        val btnAgregar = view.findViewById<MaterialButton>(R.id.btnAgregarPadre)

        btnAgregar.setOnClickListener {

            val dialog = AgregarPadreDialog()

            dialog.show(parentFragmentManager, "AgregarPadre")

        }

    }

}
