package com.example.pasitos.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.Adapters.PagosDialog
import com.example.pasitos.R

import com.example.pasitos.schemas.Nino
import com.example.pasitos.schemas.Padre

class PagosNinoAdapter : RecyclerView.Adapter<PagosNinoAdapter.NinoViewHolder>() {

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
        // Texto genérico para previsualizar
        holder.tvNombre.text = "Nombre del Niño ${position + 1}"
        holder.tvFecha.text = "04/26"
        holder.tvCantidad.text = "Cantidad total: $500.00"

        holder.btnPago.setOnClickListener {
            val dialogo = PagosDialog()
            val activity = holder.itemView.context as? androidx.fragment.app.FragmentActivity
            activity?.let {
                dialogo.show(it.supportFragmentManager, "PagosDialog")
            }
        }
    }

    // Mostrará 2 niños por cada padre para la prueba visual
    override fun getItemCount(): Int = 2
}