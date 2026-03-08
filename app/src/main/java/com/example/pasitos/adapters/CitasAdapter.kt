package com.example.pasitos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CitasAdapter(
    private val lista: MutableList<Cita>,
    private val onInfo: (Cita) -> Unit,
    private val onEditar: (Cita) -> Unit,
    private val onEliminar: (Cita) -> Unit
) : RecyclerView.Adapter<CitasAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFecha: TextView = itemView.findViewById(R.id.txtFechaCita)
        val txtHora: TextView = itemView.findViewById(R.id.txtHoraCita)
        val btnInfo: MaterialButton = itemView.findViewById(R.id.btnInfoCita)
        val btnEditar: MaterialButton = itemView.findViewById(R.id.btnEditarCita)
        val btnEliminar: MaterialButton = itemView.findViewById(R.id.btnEliminarCita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cita, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cita = lista[position]
        holder.txtFecha.text = cita.fecha
        holder.txtHora.text = cita.hora
        holder.btnInfo.setOnClickListener { onInfo(cita) }
        holder.btnEditar.setOnClickListener { onEditar(cita) }
        holder.btnEliminar.setOnClickListener { onEliminar(cita) }
    }

    override fun getItemCount(): Int = lista.size
}