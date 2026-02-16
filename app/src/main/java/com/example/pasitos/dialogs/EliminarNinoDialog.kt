package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class EliminarNinoDialog(
    private val nombre: String,
    private val onEliminarConfirmado: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_eliminar_nino, null)

        val txtMensaje = view.findViewById<TextView>(R.id.txtMensaje)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelar)
        val btnEliminar = view.findViewById<Button>(R.id.btnConfirmarEliminar)

        txtMensaje.text = "¿Eliminar a $nombre?"

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnEliminar.setOnClickListener {

            onEliminarConfirmado()

            dialog.dismiss()
        }

        return dialog
    }
}
