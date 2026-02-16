package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class EditarMaestroDialog(
    private val usuarioActual: String,
    private val contrasenaActual: String,
    private val nombreActual: String,
    private val telefonoActual: String,
    private val guarderiaActual: String
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_editar_maestro, null)

        val editUsuario = view.findViewById<EditText>(R.id.txtUsuario)
        val editContrasena = view.findViewById<EditText>(R.id.txtContrasena)
        val editNombre = view.findViewById<EditText>(R.id.txtNombre)
        val editTelefono = view.findViewById<EditText>(R.id.txtTelefono)
        val spinnerGuarderia = view.findViewById<Spinner>(R.id.spGuarderia)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        val guarderias = arrayOf("Guardería A", "Guardería B")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            guarderias
        )

        spinnerGuarderia.adapter = adapter

        editUsuario.setText(usuarioActual)
        editContrasena.setText(contrasenaActual)
        editNombre.setText(nombreActual)
        editTelefono.setText(telefonoActual)

        val posicion = guarderias.indexOf(guarderiaActual)
        if (posicion >= 0) spinnerGuarderia.setSelection(posicion)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardar.setOnClickListener {

            val nuevoUsuario = editUsuario.text.toString()
            val nuevaContrasena = editContrasena.text.toString()
            val nuevoNombre = editNombre.text.toString()
            val nuevoTelefono = editTelefono.text.toString()
            val nuevaGuarderia = spinnerGuarderia.selectedItem.toString()

            println("Editar Maestro:")
            println(nuevoUsuario)
            println(nuevaContrasena)
            println(nuevoNombre)
            println(nuevoTelefono)
            println(nuevaGuarderia)

            dialog.dismiss()
        }

        return dialog
    }
}
