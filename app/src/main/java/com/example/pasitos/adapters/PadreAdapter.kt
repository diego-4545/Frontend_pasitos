package com.example.pasitos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R
import com.example.pasitos.dialogs.EditarPadreDialog
import com.example.pasitos.dialogs.InfoPadreDialog
import com.example.pasitos.schemas.Padre
import com.example.pasitos.schemas.Nino
import com.example.pasitos.network.RetrofitClient


class PadreAdapter(
    private val lista: MutableList<Padre>,
    private val onEliminarClick: (Padre) -> Unit,
    private val onEditarClick: (Padre, String, String) -> Unit
) : RecyclerView.Adapter<PadreAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtNombre: TextView = view.findViewById(R.id.txtNombrePadre)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefonoPadre)

        val btnEliminar: Button = view.findViewById(R.id.btnEliminarPadre)
        val btnEditar: Button = view.findViewById(R.id.btnEditarPadre)
        val btnInfo: Button = view.findViewById(R.id.btnInfoPadre)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_padre, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val padre = lista[position]

        holder.txtNombre.text = padre.nombre
        holder.txtTelefono.text = padre.telefono

        holder.btnInfo.setOnClickListener {

            padre.id?.let { id ->

                RetrofitClient.instance.obtenerNinosDePadre(id)
                    .enqueue(object : retrofit2.Callback<List<Nino>> {

                        override fun onResponse(
                            call: retrofit2.Call<List<Nino>>,
                            response: retrofit2.Response<List<Nino>>
                        ) {

                            if (response.isSuccessful) {

                                val listaNinos = response.body() ?: emptyList()

                                val nombres = listaNinos.map { it.nombre }

                                val dialog = InfoPadreDialog(
                                    padre.nombre,
                                    padre.telefono,
                                    nombres
                                )

                                dialog.show(
                                    (holder.itemView.context as androidx.fragment.app.FragmentActivity)
                                        .supportFragmentManager,
                                    "InfoPadre"
                                )

                            }

                        }

                        override fun onFailure(
                            call: retrofit2.Call<List<Nino>>,
                            t: Throwable
                        ) {}

                    })

            }

        }

        holder.btnEliminar.setOnClickListener {

            onEliminarClick(padre)

        }

        holder.btnEditar.setOnClickListener {

            padre.id?.let { id ->

                val dialog = EditarPadreDialog(
                    id,
                    padre.nombre,
                    padre.telefono
                ) { nombreNuevo, telefonoNuevo ->

                    onEditarClick(padre, nombreNuevo, telefonoNuevo)

                }

                val activity = holder.itemView.context as FragmentActivity

                dialog.show(activity.supportFragmentManager, "EditarPadre")

            }

        }

    }

    override fun getItemCount(): Int = lista.size
}
