package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class EditarNinoDialog(
    private val nombreActual: String,
    private val padreActual: String,
    private val guarderiaActual: String
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_editar_nino, null)

        val editNombre = view.findViewById<EditText>(R.id.editNombre)
        val editPadre = view.findViewById<EditText>(R.id.editPadre)
        val spinnerGuarderia = view.findViewById<Spinner>(R.id.spinnerGuarderia)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCambios)

        val guarderias = arrayOf("Guardería A", "Guardería B")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            guarderias
        )

        spinnerGuarderia.adapter = adapter

        // valores actuales
        editNombre.setText(nombreActual)
        editPadre.setText(padreActual)

        val posicion = guarderias.indexOf(guarderiaActual)
        if (posicion >= 0) spinnerGuarderia.setSelection(posicion)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardar.setOnClickListener {

            val nuevoNombre = editNombre.text.toString()
            val nuevoPadre = editPadre.text.toString()
            val nuevaGuarderia = spinnerGuarderia.selectedItem.toString()

            println("Editar:")
            println(nuevoNombre)
            println(nuevoPadre)
            println(nuevaGuarderia)

            dialog.dismiss()
        }

        return dialog
    }
}
