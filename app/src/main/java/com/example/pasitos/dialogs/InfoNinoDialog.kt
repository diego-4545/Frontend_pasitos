package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class InfoNinoDialog(
    private val nombre: String,
    private val padre: String,
    private val guarderia: String
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_info_nino, null)

        val txtNombre = view.findViewById<TextView>(R.id.txtNombreInfo)
        val txtPadre = view.findViewById<TextView>(R.id.txtPadreInfo)
        val txtGuarderia = view.findViewById<TextView>(R.id.txtGuarderiaInfo)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrar)

        txtNombre.text = nombre
        txtPadre.text = padre
        txtGuarderia.text = guarderia

        btnCerrar.setOnClickListener {
            dismiss()
        }


        return AlertDialog.Builder(requireContext())
            .setTitle("Información del Niño")
            .setView(view)
            .create()


    }

}
