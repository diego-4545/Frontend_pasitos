package com.example.pasitos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R
import com.example.pasitos.dialogs.EntradasDialog
import com.example.pasitos.schemas.Nino // Asegúrate de que tu modelo se llame así
import com.example.pasitos.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntradasNinosAdapter(
    private val lista: MutableList<Nino>,
    private val refrescar: () -> Unit
) : RecyclerView.Adapter<EntradasNinosAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // IDs exactos de tu item_entradas.xml
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val btnAgregar: MaterialButton = itemView.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entradas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nino = lista[position]
        holder.txtNombre.text = nino.nombre

        // 1. IMPORTANTE: Necesitamos el contexto como FragmentActivity
        val activity = holder.itemView.context as? FragmentActivity

        holder.btnAgregar.setOnClickListener {
            // 2. Verificamos que la activity no sea nula
            activity?.let {
                val dialog = EntradasDialog(nino.nombre) {
                    // Aquí va lo que pasará al confirmar (por ahora vacío o un Toast)
                    println("Confirmado para ${nino.nombre}")
                }
                // 3. Mostramos el diálogo usando el manager de la actividad
                dialog.show(it.supportFragmentManager, "EntradasDialog")
            }
        }
    }
    override fun getItemCount(): Int = lista.size
}