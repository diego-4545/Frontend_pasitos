package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle

import android.view.LayoutInflater
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class AgregarPadreDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = android.app.AlertDialog.Builder(requireContext())

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_agregar_padre, null)

        builder.setView(view)

        val dialog = builder.create()

        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarPadre)

        btnGuardar.setOnClickListener {

            dialog.dismiss()

        }

        return dialog

    }

}
