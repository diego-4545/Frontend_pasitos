package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R
import com.example.pasitos.schemas.Nino
import com.example.pasitos.schemas.Padre

class EditarNinoDialog(
    private val ninoActual: Nino,
    private val listaPadres: List<Padre>,
    private val onGuardarClick: (Nino) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_editar_nino, null)

        val editNombre = view.findViewById<EditText>(R.id.editNombre)
        val spinnerPadre = view.findViewById<Spinner>(R.id.spPadre)
        val spinnerGuarderia = view.findViewById<Spinner>(R.id.spinnerGuarderia)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCambios)

        // Spinner de padres
        val listaNombresPadres = listaPadres.map { it.nombre }
        val padresAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listaNombresPadres
        )
        spinnerPadre.adapter = padresAdapter

        // Spinner de guarderías
        val guarderias = arrayOf("Tulipanes", "Pinos")
        val guarderiasAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            guarderias
        )
        spinnerGuarderia.adapter = guarderiasAdapter

        // Valores actuales
        editNombre.setText(ninoActual.nombre)
        val nombrePadreActual = listaPadres.find { it.id == ninoActual.padre_id }?.nombre ?: ""
        spinnerPadre.setSelection(listaNombresPadres.indexOf(nombrePadreActual).coerceAtLeast(0))
        spinnerGuarderia.setSelection(guarderias.indexOf(ninoActual.sucursal.toString()).coerceAtLeast(0))

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardar.setOnClickListener {
            val nuevoNombre = editNombre.text.toString()
            val nuevoPadreNombre = spinnerPadre.selectedItem.toString()
            val nuevaGuarderiaStr = spinnerGuarderia.selectedItem.toString()

            if (nuevoNombre.isBlank()) {
                Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val padreId = listaPadres.find { it.nombre == nuevoPadreNombre }?.id
            if (padreId == null) {
                Toast.makeText(requireContext(), "Padre no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ninoActualizado = Nino(
                id = ninoActual.id,
                nombre = nuevoNombre,
                padre_id = padreId,
                sucursal = nuevaGuarderiaStr.toIntOrNull() ?: 0
            )

            onGuardarClick(ninoActualizado)
            dismiss()
        }

        return dialog
    }
}
