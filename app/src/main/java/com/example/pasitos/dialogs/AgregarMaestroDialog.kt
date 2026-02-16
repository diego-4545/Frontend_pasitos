package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class AgregarMaestroDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_agregar_maestro, null)

        val txtUsuario = view.findViewById<EditText>(R.id.txtUsuario)
        val txtContrasena = view.findViewById<EditText>(R.id.txtContrasena)
        val txtNombre = view.findViewById<EditText>(R.id.txtNombre)
        val txtTelefono = view.findViewById<EditText>(R.id.txtTelefono)
        val txtGuarderia = view.findViewById<Spinner>(R.id.spGuarderia)

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
        txtGuarderia.adapter = adapter


        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardar.setOnClickListener {

            println(txtUsuario.text.toString())
            println(txtContrasena.text.toString())
            println(txtNombre.text.toString())
            println(txtTelefono.text.toString())
            println(txtGuarderia.selectedItem.toString())

            dialog.dismiss()

        }

        return dialog
    }
}
