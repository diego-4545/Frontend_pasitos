package com.example.pasitos

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.dialogs.InfoNinoDialog
import com.example.pasitos.dialogs.EditarNinoDialog

import com.example.pasitos.schemas.Nino
import com.example.pasitos.schemas.Padre

class NinoAdapter(
    private val lista: MutableList<Nino>,
    private val listaPadres: List<Padre>,
    private val onEliminar: (Nino) -> Unit,
    private val onEditar: (Nino) -> Unit,
    private val onInfo: (Nino) -> Unit
) : RecyclerView.Adapter<NinoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val txtPadre: TextView = itemView.findViewById(R.id.txtPadre)
        val btnInfo: Button = itemView.findViewById(R.id.btnInfo)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nino, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nino = lista[position]

        holder.txtNombre.text = nino.nombre

        val nombrePadre = listaPadres.find { it.id == nino.padre_id }?.nombre ?: "Desconocido"
        holder.txtPadre.text = nombrePadre

        holder.btnInfo.setOnClickListener {
            val nombrePadre = listaPadres.find { it.id == nino.padre_id }?.nombre ?: "Desconocido"
            val dialog = InfoNinoDialog(
                nombre = nino.nombre,
                padre = nombrePadre,
                guarderia = nino.sucursal.toString(),
                paquete = nino.paquete
            )
            dialog.show(
                (holder.itemView.context as FragmentActivity).supportFragmentManager,
                "InfoNino"
            )
        }
        holder.btnEditar.setOnClickListener {
            val activity = holder.itemView.context as FragmentActivity
            val dialog = EditarNinoDialog(
                ninoActual = nino,
                listaPadres = listaPadres,
                onGuardarClick = { ninoActualizado ->
                    onEditar(ninoActualizado)
                }
            )
            dialog.show(activity.supportFragmentManager, "EditarNino")
        }
        holder.btnEliminar.setOnClickListener {
            onEliminar(nino)
        }

        Log.d("NINO_ADAPTER", "Mostrando niño: ${nino.nombre}, padre_id=$nombrePadre, sucursal=${nino.sucursal}")
    }


    override fun getItemCount(): Int = lista.size
}
