package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class InfoMaestroDialog(
    private val usuario: String,
    private val contrasena: String,
    private val nombre: String,
    private val telefono: String,
    private val guarderia: Int
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_info_maestro, null)

        val txtUsuario = view.findViewById<TextView>(R.id.txtUsuarioInfo)
        val txtContrasena = view.findViewById<TextView>(R.id.txtContrasena)
        val txtNombre = view.findViewById<TextView>(R.id.txtNombreInfo)
        val txtTelefono = view.findViewById<TextView>(R.id.txtTelefonoInfo)
        val txtGuarderia = view.findViewById<TextView>(R.id.txtGuarderiaInfo)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrar)

        txtUsuario.text = usuario
        txtContrasena.text = contrasena
        txtNombre.text = nombre
        txtTelefono.text = telefono

        txtGuarderia.text = when (guarderia) {
            1 -> "Tulipanes"
            2 -> "Pinos"
            else -> "Desconocido"
        }

        btnCerrar.setOnClickListener { dismiss() }

        return AlertDialog.Builder(requireContext())
            .setTitle("Información del Maestro")
            .setView(view)
            .create()
    }
}
