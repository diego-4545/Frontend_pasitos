package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class AgregarNinoDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity().layoutInflater
            .inflate(R.layout.dialog_agregar_nino, null)

        val txtNombre = view.findViewById<EditText>(R.id.txtNombre)
        val spGuarderia = view.findViewById<Spinner>(R.id.spGuarderia)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        // Opciones de guardería
        val guarderias = arrayOf(
            "Seleccionar guardería...",
            "Guarderia A",
            "Guarderia B"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            guarderias
        )

        spGuarderia.adapter = adapter

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardar.setOnClickListener {

            val nombre = txtNombre.text.toString()
            val guarderia = spGuarderia.selectedItem.toString()

            println("Guardar niño: $nombre - $guarderia")

            dialog.dismiss()
        }
        val spPadre = view.findViewById<Spinner>(R.id.spPadre)

        val padres = arrayOf(
            "Seleccionar padre...",
            "Carlos Perez",
            "Luis Lopez"
        )

        val adapterPadres = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            padres
        )

        spPadre.adapter = adapterPadres

        btnGuardar.setOnClickListener {

            val nombre = txtNombre.text.toString()
            val padre = spPadre.selectedItem.toString()

            println("Guardar niño: $nombre - Padre: $padre")

            dialog.dismiss()
        }


        return dialog
    }
}
