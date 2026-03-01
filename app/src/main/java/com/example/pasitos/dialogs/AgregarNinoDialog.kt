package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R

class AgregarNinoDialog(
    private val listaPadres: List<String>,
    private val onAgregar: (nombre: String, padre: String, sucursal: Int, paquete: Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_agregar_nino, null)

        val etNombre = view.findViewById<EditText>(R.id.txtNombre)
        val spinnerPadre = view.findViewById<Spinner>(R.id.spPadre)
        val spinnerSucursal = view.findViewById<Spinner>(R.id.spGuarderia)
        val spinnerPaquete = view.findViewById<Spinner>(R.id.spPaquete)
        val btnAgregar = view.findViewById<Button>(R.id.btnGuardar)

        val adapterPadres = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaPadres
        )
        adapterPadres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPadre.adapter = adapterPadres

        val sucursales = arrayOf("Seleccionar sucursal...", "Tulipanes", "Pinos")
        val adapterSuc = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sucursales
        )
        adapterSuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSucursal.adapter = adapterSuc

        val paquetes = arrayOf(
            "Seleccionar paquete...",
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

        val adapterPaquete = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            paquetes
        )
        adapterPaquete.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPaquete.adapter = adapterPaquete


        btnAgregar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val padre = spinnerPadre.selectedItem.toString()
            val sucursalPos = spinnerSucursal.selectedItemPosition
            val paquetePos = spinnerPaquete.selectedItemPosition

            if (nombre.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese el nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (padre == "Seleccionar padre...") {
                Toast.makeText(requireContext(), "Seleccione un padre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (sucursalPos == 0) {
                Toast.makeText(requireContext(), "Seleccione una sucursal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (paquetePos == 0) {
                Toast.makeText(requireContext(), "Seleccione un paquete", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val paqueteReal = paquetePos

            onAgregar(nombre, padre, sucursalPos, paqueteReal)
            dismiss()
        }

        return android.app.AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}