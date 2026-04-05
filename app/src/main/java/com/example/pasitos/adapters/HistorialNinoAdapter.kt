package com.example.pasitos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.Fragments.HistorialFragment.NinoConPago
import com.example.pasitos.R

class HistorialNinoAdapter(
    private val lista: MutableList<NinoConPago>
) : RecyclerView.Adapter<HistorialNinoAdapter.NinoViewHolder>() {

    class NinoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreNinoHistorial)
        val tvFecha: TextView = view.findViewById(R.id.tvFechaPagoHistorial)
        val tvTotal: TextView = view.findViewById(R.id.tvTotalPagadoHistorial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NinoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historial_nino, parent, false)
        return NinoViewHolder(view)
    }

    override fun onBindViewHolder(holder: NinoViewHolder, position: Int) {
        val ninoConPago = lista[position]
        val pagosPagados = ninoConPago.pagos.filter { it.estado == 1 }
        val meses = arrayOf("Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic")

        holder.tvNombre.text = ninoConPago.nino.nombre

        val pagoMasReciente = pagosPagados.maxByOrNull { it.anio * 100 + it.mes }
        holder.tvFecha.text = pagoMasReciente?.let {
            "${meses[it.mes - 1]}/${it.anio.toString().takeLast(2)}"
        } ?: "-"

        val totalPagado = pagosPagados.sumOf { it.pago }
        holder.tvTotal.text = "$${"%.2f".format(totalPagado)}"
    }

    override fun getItemCount(): Int = lista.size
}