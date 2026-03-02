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
    private val guarderia: String,
    private val paquete: Int
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_info_nino, null)

        val txtNombre = view.findViewById<TextView>(R.id.txtNombreInfo)
        val txtPadre = view.findViewById<TextView>(R.id.txtPadreInfo)
        val txtGuarderia = view.findViewById<TextView>(R.id.txtGuarderiaInfo)
        val txtPaquete = view.findViewById<TextView>(R.id.txtPaqueteInfo)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrar)

        txtNombre.text = nombre
        txtPadre.text = padre

        val guarderiaTexto = when (guarderia.toIntOrNull()) {
            1 -> "Tulipanes"
            2 -> "Pinos"
            else -> "Desconocido"
        }
        txtGuarderia.text = guarderiaTexto

        val paqueteTexto = when (paquete) {
            1 -> "4 horas - $3150"
            2 -> "5 horas - $3350"
            3 -> "6 horas - $3550"
            4 -> "7 horas - $3750"
            5 -> "8 horas - $3950"
            6 -> "9 horas - $4200"
            7 -> "10 horas - $4750"
            8 -> "11 horas - $5000"
            9 -> "12 horas - $5300"
            else -> "Desconocido"
        }

        txtPaquete.text = paqueteTexto

        btnCerrar.setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Información del Niño")
            .setView(view)
            .create()
    }
}