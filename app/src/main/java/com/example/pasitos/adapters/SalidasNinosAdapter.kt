package com.example.pasitos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R
import com.example.pasitos.schemas.FechaAbierta
import com.google.android.material.button.MaterialButton

class SalidasNinosAdapter(
    private val lista: MutableList<FechaAbierta>
) : RecyclerView.Adapter<SalidasNinosAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val btnSalida: MaterialButton = itemView.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_salidas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val fecha = lista[position]

        holder.txtNombre.text = fecha.nombre

        holder.btnSalida.setOnClickListener {
        }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<FechaAbierta>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}