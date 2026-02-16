package com.example.pasitos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.schemas.Maestro
import com.example.pasitos.dialogs.InfoMaestroDialog
import com.example.pasitos.dialogs.EditarMaestroDialog
import com.example.pasitos.dialogs.EliminarMaestroDialog

class MaestroAdapter(private val lista: MutableList<Maestro>) :
    RecyclerView.Adapter<MaestroAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreMaestro)
        val txtTelefono: TextView = itemView.findViewById(R.id.txtTelefonoMaestro)

        val btnInfo: Button = itemView.findViewById(R.id.btnInfoMaestro)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditarMaestro)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarMaestro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_maestro, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val maestro = lista[position]

        holder.txtNombre.text = maestro.nombre
        holder.txtTelefono.text = maestro.telefono

        val activity = holder.itemView.context as FragmentActivity

        holder.btnInfo.setOnClickListener {

            val dialog = InfoMaestroDialog(
                maestro.usuario,
                maestro.contrasena,
                maestro.nombre,
                maestro.telefono,
                maestro.guarderia
            )

            dialog.show(activity.supportFragmentManager, "InfoMaestro")
        }

        holder.btnEditar.setOnClickListener {

            val dialog = EditarMaestroDialog(
                maestro.usuario,
                maestro.contrasena,
                maestro.nombre,
                maestro.telefono,
                maestro.guarderia
            )

            dialog.show(activity.supportFragmentManager, "EditarMaestro")
        }

        holder.btnEliminar.setOnClickListener {

            val pos = holder.adapterPosition

            if (pos != RecyclerView.NO_POSITION) {

                val dialog = EliminarMaestroDialog(maestro.nombre) {

                    lista.removeAt(pos)
                    notifyItemRemoved(pos)
                }

                dialog.show(activity.supportFragmentManager, "EliminarMaestro")
            }
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}
