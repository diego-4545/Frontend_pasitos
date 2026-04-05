package com.example.pasitos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.Fragments.HistorialFragment.NinoConPago
import com.example.pasitos.Fragments.HistorialFragment.PadreConNinos
import com.example.pasitos.R

class HistorialPadreAdapter(
    private var lista: MutableList<PadreConNinos>
) : RecyclerView.Adapter<HistorialPadreAdapter.PadreViewHolder>() {

    class PadreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombrePadre)
        val tvDeuda: TextView = view.findViewById(R.id.tvDeudaPadre)
        val recyclerHijos: RecyclerView = view.findViewById(R.id.recyclerHijos)
        val layoutHeader: View = view.findViewById(R.id.layoutHeaderPadre)
        val ivExpandir: ImageView = view.findViewById(R.id.ivExpandir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PadreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pagos_padres, parent, false)
        return PadreViewHolder(view)
    }

    override fun onBindViewHolder(holder: PadreViewHolder, position: Int) {
        val padreConNinos = lista[position]
        holder.tvNombre.text = padreConNinos.padre.nombre

        val totalPagado = padreConNinos.ninos.sumOf { ncp ->
            ncp.pagos.filter { it.estado == 1 }.sumOf { it.pago }
        }
        holder.tvDeuda.text = "$${"%.2f".format(totalPagado)}"

        if (holder.recyclerHijos.layoutManager == null) {
            holder.recyclerHijos.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.recyclerHijos.isNestedScrollingEnabled = false
        }

        holder.recyclerHijos.adapter = HistorialNinoAdapter(
            padreConNinos.ninos.toMutableList()
        )

        holder.layoutHeader.setOnClickListener {
            val estaVisible = holder.recyclerHijos.visibility == View.VISIBLE
            holder.recyclerHijos.visibility = if (estaVisible) View.GONE else View.VISIBLE
            holder.ivExpandir.setImageResource(
                if (estaVisible) android.R.drawable.arrow_down_float
                else android.R.drawable.arrow_up_float
            )
        }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nueva: MutableList<PadreConNinos>) {
        lista = nueva
        notifyDataSetChanged()
    }
}