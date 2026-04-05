package com.example.pasitos.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.Fragments.PagosFragment.NinoConPago
import com.example.pasitos.R
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.PagoResponse
import com.example.pasitos.schemas.PagoUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagosNinoAdapter(
    private var lista: MutableList<NinoConPago>,
    private val onRefresh: () -> Unit
) : RecyclerView.Adapter<PagosNinoAdapter.NinoViewHolder>() {

    class NinoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreNino)
        val tvFecha: TextView = view.findViewById(R.id.tvFechaPago)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidadPagar)
        val btnPago: Button = view.findViewById(R.id.btnAgregarPago)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NinoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pagos_nino, parent, false)
        return NinoViewHolder(view)
    }

    override fun onBindViewHolder(holder: NinoViewHolder, position: Int) {
        val ninoConPago = lista[position]
        val nino = ninoConPago.nino

        // ✅ Solo mostrar pagos incompletos (estado != 1)
        val pagosIncompletos = ninoConPago.pagos.filter { it.estado != 1 }

        holder.tvNombre.text = nino.nombre

        if (pagosIncompletos.isEmpty()) {
            holder.tvFecha.text = "-"
            holder.tvCantidad.text = "Sin deuda pendiente"
            holder.btnPago.visibility = View.GONE
            return
        }

        // ✅ Mostrar el pago más reciente
        val pago = pagosIncompletos.maxByOrNull { it.anio * 100 + it.mes }!!
        val meses = arrayOf("Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic")
        holder.tvFecha.text = "${meses[pago.mes - 1]}/${pago.anio.toString().takeLast(2)}"

        val restante = pago.deuda - pago.pago
        holder.tvCantidad.text = "Restante: $${"%.2f".format(restante)}"
        holder.btnPago.visibility = View.VISIBLE

        holder.btnPago.setOnClickListener {
            mostrarDialogoPago(holder, pago, nino.nombre)
        }
    }

    private fun mostrarDialogoPago(holder: NinoViewHolder, pago: PagoResponse, nombreNino: String) {
        val context = holder.itemView.context
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_agregar_pago, null)
        val edtMonto = view.findViewById<EditText>(R.id.txtPagos)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmar)
        val btnCancelar = view.findViewById<Button>(R.id.button)

        val restante = pago.deuda - pago.pago

        val dialog = AlertDialog.Builder(context)
            .setTitle("Agregar pago - $nombreNino")
            .setView(view)
            .create()

        btnConfirmar.setOnClickListener {
            val montoStr = edtMonto.text.toString()
            if (montoStr.isBlank()) {
                Toast.makeText(context, "Ingresa un monto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val monto = montoStr.toDoubleOrNull()
            if (monto == null || monto <= 0) {
                Toast.makeText(context, "Monto inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (monto > restante) {
                Toast.makeText(context, "El monto no puede ser mayor a la deuda restante $${"%.2f".format(restante)}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoTotal = pago.pago + monto
            val update = PagoUpdate(pago = nuevoTotal)

            RetrofitClient.instance.actualizarPago(pago.id, update).enqueue(object : Callback<PagoResponse> {
                override fun onResponse(call: Call<PagoResponse>, response: Response<PagoResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Pago registrado", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        onRefresh()
                    } else {
                        Toast.makeText(context, "Error al registrar pago", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<PagoResponse>, t: Throwable) {
                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnCancelar.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun getItemCount(): Int = lista.size
}