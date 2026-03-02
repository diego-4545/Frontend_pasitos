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
        val spinnerPaquete = view.findViewById<Spinner>(R.id.spPaqueteEditar)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarCambios)

        val listaNombresPadres = listaPadres.map { it.nombre }
        val padresAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listaNombresPadres
        )
        spinnerPadre.adapter = padresAdapter

        val guarderias = arrayOf("Tulipanes", "Pinos")
        val guarderiasAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            guarderias
        )
        spinnerGuarderia.adapter = guarderiasAdapter

        val paquetes = arrayOf(
            "4 horas - $3150",
            "5 horas - $3350",
            "6 horas - $3550",
            "7 horas - $3750",
            "8 horas - $3950",
            "9 horas - $4200",
            "10 horas - $4750",
            "11 horas - $5000",
            "12 horas - $5300"
        )

        val paquetesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            paquetes
        )
        spinnerPaquete.adapter = paquetesAdapter

        editNombre.setText(ninoActual.nombre)

        val nombrePadreActual = listaPadres.find { it.id == ninoActual.padre_id }?.nombre ?: ""
        spinnerPadre.setSelection(listaNombresPadres.indexOf(nombrePadreActual).coerceAtLeast(0))

        spinnerGuarderia.setSelection(
            when (ninoActual.sucursal) {
                1 -> 0
                2 -> 1
                else -> 0
            }
        )

        spinnerPaquete.setSelection((ninoActual.paquete - 1).coerceAtLeast(0))

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardar.setOnClickListener {

            val nuevoNombre = editNombre.text.toString()

            if (nuevoNombre.isBlank()) {
                Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val padreId = listaPadres.find { it.nombre == spinnerPadre.selectedItem.toString() }?.id
            if (padreId == null) {
                Toast.makeText(requireContext(), "Padre no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevaSucursal = when (spinnerGuarderia.selectedItemPosition) {
                0 -> 1
                1 -> 2
                else -> 1
            }

            val nuevoPaquete = spinnerPaquete.selectedItemPosition + 1

            val ninoActualizado = Nino(
                id = ninoActual.id,
                nombre = nuevoNombre,
                padre_id = padreId,
                sucursal = nuevaSucursal,
                paquete = nuevoPaquete
            )

            onGuardarClick(ninoActualizado)
            dismiss()
        }

        return dialog
    }
}