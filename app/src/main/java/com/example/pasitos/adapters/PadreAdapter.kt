package com.example.pasitos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R
import com.example.pasitos.schemas.Padre
import com.example.pasitos.dialogs.EditarPadreDialog
import com.example.pasitos.dialogs.InfoPadreDialog
import com.example.pasitos.dialogs.EliminarPadreDialog


class PadreAdapter(
    private val lista: MutableList<Padre>
) : RecyclerView.Adapter<PadreAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtNombre: TextView =
            itemView.findViewById(R.id.txtNombrePadre)

        val txtTelefono: TextView =
            itemView.findViewById(R.id.txtTelefonoPadre)

        val btnInfo: Button =
            itemView.findViewById(R.id.btnInfoPadre)

        val btnEditar: Button =
            itemView.findViewById(R.id.btnEditarPadre)

        val btnEliminar: Button =
            itemView.findViewById(R.id.btnEliminarPadre)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_padre, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val padre = lista[position]

        holder.txtNombre.text = padre.nombre
        holder.txtTelefono.text = padre.telefono

        holder.btnInfo.setOnClickListener {

            val dialog = InfoPadreDialog(
                padre.nombre,
                padre.telefono,
                listOf(
                    "Juan Perez",
                    "Maria Perez",
                    "Carlos Perez"
                )
            )

            dialog.show(
                (holder.itemView.context as androidx.fragment.app.FragmentActivity)
                    .supportFragmentManager,
                "InfoPadre"
            )
        }


        holder.btnEditar.setOnClickListener {

            val dialog = EditarPadreDialog(
                padre.nombre,
                padre.telefono
            )

            dialog.show(
                (holder.itemView.context as androidx.fragment.app.FragmentActivity)
                    .supportFragmentManager,
                "EditarPadre"
            )
        }


        holder.btnEliminar.setOnClickListener {

            val activity = holder.itemView.context as FragmentActivity

            val dialog = EliminarPadreDialog(padre.nombre) {

                lista.removeAt(holder.adapterPosition)

                notifyItemRemoved(holder.adapterPosition)

            }

            dialog.show(activity.supportFragmentManager, "EliminarPadre")
        }

    }

    override fun getItemCount(): Int {

        return lista.size
    }
}
