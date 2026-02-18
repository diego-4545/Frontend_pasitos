package com.example.pasitos.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class EliminarNinoDialog(
    private val nombre: String,
    private val onEliminarConfirmado: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_eliminar_padre, null)
        builder.setView(view)
        val dialog = builder.create()

        val txtMensaje = view.findViewById<TextView>(R.id.txtMensaje)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarPadre)
        val btnEliminar = view.findViewById<Button>(R.id.btnConfirmarEliminarPadre)

        txtMensaje.text = "¿Eliminar a $nombre?"

        btnCancelar.setOnClickListener { dialog.dismiss() }
        btnEliminar.setOnClickListener {
            onEliminarConfirmado()
            dialog.dismiss()
        }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
