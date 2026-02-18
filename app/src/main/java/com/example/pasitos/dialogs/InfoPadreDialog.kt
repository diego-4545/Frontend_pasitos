package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class InfoPadreDialog(
    private val nombre: String,
    private val telefono: String,
    private val hijos: List<String>
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_info_padre, null)

        val txtNombre = view.findViewById<TextView>(R.id.txtNombrePadreInfo)
        val txtTelefono = view.findViewById<TextView>(R.id.txtTelefonoPadreInfo)
        val txtHijos = view.findViewById<TextView>(R.id.txtHijosPadreInfo)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrarPadre)

        txtNombre.text = nombre
        txtTelefono.text = telefono

        txtHijos.text = hijos.joinToString("\n")

        btnCerrar.setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Información del Padre")
            .setView(view)
            .create()
    }
}
