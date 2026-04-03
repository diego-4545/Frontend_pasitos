package com.example.pasitos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R

class PagosPadreAdapter : RecyclerView.Adapter<PagosPadreAdapter.PadreViewHolder>() {

    class PadreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Conexión con los IDs de item_pagos_padres.xml
        val tvNombre: TextView = view.findViewById(R.id.tvNombrePadre)
        val tvDeuda: TextView = view.findViewById(R.id.tvDeudaPadre)
        val recyclerHijos: RecyclerView = view.findViewById(R.id.recyclerHijos)
        val layoutHeader: View = view.findViewById(R.id.layoutHeaderPadre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PadreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pagos_padres, parent, false)
        return PadreViewHolder(view)
    }

    override fun onBindViewHolder(holder: PadreViewHolder, position: Int) {
        holder.tvNombre.text = "Padre de Familia ${position + 1}"
        holder.tvDeuda.text = "$1,000.00"

        if (holder.recyclerHijos.layoutManager == null) {
            holder.recyclerHijos.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.recyclerHijos.setHasFixedSize(false)
            holder.recyclerHijos.isNestedScrollingEnabled = false
        }

        if (holder.recyclerHijos.adapter == null) {
            holder.recyclerHijos.adapter = PagosNinoAdapter()
        }

        // Expandir / colapsar
        holder.layoutHeader.setOnClickListener {
            val estaVisible = holder.recyclerHijos.visibility == View.VISIBLE
            holder.recyclerHijos.visibility = if (estaVisible) View.GONE else View.VISIBLE
        }
    }

    // Mostrará 10 padres para que puedas probar el scroll
    override fun getItemCount(): Int = 10
}