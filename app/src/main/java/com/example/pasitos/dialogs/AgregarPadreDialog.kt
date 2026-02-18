package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class AgregarPadreDialog(
    private val onPadreAgregado: ((String, String) -> Unit)? = null
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = android.app.AlertDialog.Builder(requireContext())

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_agregar_padre, null)

        builder.setView(view)

        val dialog = builder.create()

        val txtNombre = view.findViewById<EditText>(R.id.txtNombrePadre)
        val txtTelefono = view.findViewById<EditText>(R.id.txtTelefonoPadre)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarPadre)

        btnGuardar.setOnClickListener {

            val nombre = txtNombre.text.toString().trim()
            val telefono = txtTelefono.text.toString().trim()

            // VALIDACIONES
            if (nombre.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese el nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (telefono.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese el teléfono", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (telefono.length != 10) {
                Toast.makeText(requireContext(), "El teléfono debe tener 10 dígitos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ENVÍA LOS DATOS AL FRAGMENT
            onPadreAgregado?.invoke(nombre, telefono)

            dialog.dismiss()
        }

        return dialog
    }
}
