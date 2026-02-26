package com.example.pasitos.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class EntradasDialog(
    private val nombreNino: String,
    private val alConfirmar: () -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_agregar_entrada, container, false)

        // 1. Referenciar componentes
        val txtTitulo = view.findViewById<TextView>(R.id.txtPregunta)
        val btnCancelar = view.findViewById<Button>(R.id.button)
        val btnConfirmar = view.findViewById<Button>(R.id.button2)

        // 2. Personalizar el mensaje con el nombre del niño
        txtTitulo.text = "¿Quieres confirmar la entrada de $nombreNino?"

        // 3. Configurar botones
        btnCancelar.setOnClickListener {
            dismiss() // Cierra el diálogo
        }

        btnConfirmar.setOnClickListener {
            alConfirmar() // Ejecuta la función de Retrofit
            dismiss()
        }

        return view
    }
}