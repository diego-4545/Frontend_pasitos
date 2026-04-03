package com.example.pasitos.Adapters

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class PagosDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Inflamos el XML del diálogo
        val view = inflater.inflate(R.layout.dialog_agregar_pago, container, false)

        // 2. Conectamos los componentes del XML mediante sus IDs
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmar)
        val btnCancelar = view.findViewById<Button>(R.id.button) // El ID de tu XML para cancelar
        val edtMonto = view.findViewById<EditText>(R.id.txtPagos)

        // 3. Lógica del botón Confirmar
        btnConfirmar.setOnClickListener {
            val monto = edtMonto.text.toString()
            if (monto.isNotEmpty()) {
                // Aquí iría la lógica para guardar el pago
                dismiss() // Cierra el diálogo
            }
        }

        // 4. Lógica del botón Cancelar
        btnCancelar.setOnClickListener {
            dismiss() // Simplemente cierra el cuadrito
        }

        return view
    }

    // Opcional: Para que el diálogo no ocupe toda la pantalla y se vea como un "Pop-up"
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}