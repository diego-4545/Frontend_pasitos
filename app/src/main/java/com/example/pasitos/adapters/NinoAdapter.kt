package com.example.pasitos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.dialogs.InfoNinoDialog
import com.example.pasitos.schemas.Nino
import com.example.pasitos.dialogs.EditarNinoDialog
import com.example.pasitos.dialogs.EliminarNinoDialog


class NinoAdapter(private val lista: MutableList<Nino>) :
    RecyclerView.Adapter<NinoAdapter.ViewHolder>()
 {

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
        holder.txtPadre.text = nino.padre

        holder.btnInfo.setOnClickListener {

            val activity = holder.itemView.context as FragmentActivity

            val dialog = InfoNinoDialog(
                nino.nombre,
                nino.padre,
                nino.guarderia
            )

            dialog.show(activity.supportFragmentManager, "InfoNino")
        }


        holder.btnEditar.setOnClickListener {

            val activity = holder.itemView.context as FragmentActivity

            val dialog = EditarNinoDialog(
                nino.nombre,
                nino.padre,
                nino.guarderia
            )

            dialog.show(activity.supportFragmentManager, "EditarNino")
        }

        holder.btnEliminar.setOnClickListener {

            val activity = holder.itemView.context as FragmentActivity

            val dialog = EliminarNinoDialog(nino.nombre) {

                lista.removeAt(holder.adapterPosition)

                notifyItemRemoved(holder.adapterPosition)

            }

            dialog.show(activity.supportFragmentManager, "EliminarNino")
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}
