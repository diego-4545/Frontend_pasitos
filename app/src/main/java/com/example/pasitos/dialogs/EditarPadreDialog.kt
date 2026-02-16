package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class EditarPadreDialog(
    private val nombreActual: String,
    private val telefonoActual: String
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_editar_padre, null)

        val editNombre = view.findViewById<EditText>(R.id.editNombrePadre)
        val editTelefono = view.findViewById<EditText>(R.id.editTelefonoPadre)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCambiosPadre)

        // cargar datos actuales
        editNombre.setText(nombreActual)
        editTelefono.setText(telefonoActual)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardar.setOnClickListener {

            val nuevoNombre = editNombre.text.toString()
            val nuevoTelefono = editTelefono.text.toString()

            println("Editar Padre:")
            println(nuevoNombre)
            println(nuevoTelefono)

            dialog.dismiss()
        }

        return dialog
    }
}
