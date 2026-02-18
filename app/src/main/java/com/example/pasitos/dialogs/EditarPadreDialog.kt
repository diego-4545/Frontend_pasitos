package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class EditarPadreDialog(
    private val padreId: Int,
    private val nombreActual: String,
    private val telefonoActual: String,
    private val onEditarConfirmado: (String, String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = android.app.AlertDialog.Builder(requireContext())

        val view = requireActivity().layoutInflater
            .inflate(R.layout.dialog_agregar_padre, null)

        builder.setView(view)

        val dialog = builder.create()

        val txtNombre = view.findViewById<EditText>(R.id.txtNombrePadre)
        val txtTelefono = view.findViewById<EditText>(R.id.txtTelefonoPadre)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarPadre)

        txtNombre.setText(nombreActual)
        txtTelefono.setText(telefonoActual)

        btnGuardar.setOnClickListener {

            val nombre = txtNombre.text.toString().trim()
            val telefono = txtTelefono.text.toString().trim()

            if (nombre.isEmpty() || telefono.isEmpty()) {

                Toast.makeText(context, "Campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (telefono.length != 10) {

                Toast.makeText(context, "Teléfono debe tener 10 dígitos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            onEditarConfirmado(nombre, telefono)

            dialog.dismiss()

        }

        return dialog

    }

}
