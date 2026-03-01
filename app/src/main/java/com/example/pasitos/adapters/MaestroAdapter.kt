package com.example.pasitos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.dialogs.EditarMaestroDialog
import com.example.pasitos.dialogs.EliminarMaestroDialog
import com.example.pasitos.dialogs.InfoMaestroDialog
import com.example.pasitos.schemas.Maestro
import android.widget.Toast
import com.example.pasitos.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MaestroAdapter(
    private val lista: MutableList<Maestro>,
    private val refrescar: () -> Unit
) : RecyclerView.Adapter<MaestroAdapter.ViewHolder>() {

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
                usuario = maestro.username,
                contrasena = maestro.password,
                nombre = maestro.nombre,
                telefono = maestro.telefono,
                guarderia = maestro.sucursal ?: 0
            )
            dialog.show(activity.supportFragmentManager, "InfoMaestro")
        }

        holder.btnEditar.setOnClickListener {
            val dialog = EditarMaestroDialog(maestro) {
                refrescar()
            }
            dialog.show(activity.supportFragmentManager, "EditarMaestro")
        }

        holder.btnEliminar.setOnClickListener {

            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val maestro = lista[pos]

            val dialog = EliminarMaestroDialog(maestro.nombre) {

                RetrofitClient.instance.eliminarMaestro(maestro.id!!)
                    .enqueue(object : retrofit2.Callback<Void> {

                        override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {

                            if (response.isSuccessful) {

                                lista.removeAt(pos)
                                notifyItemRemoved(pos)

                                Toast.makeText(
                                    holder.itemView.context,
                                    "Maestro eliminado",
                                    Toast.LENGTH_SHORT
                                ).show()

                                refrescar()

                            } else {

                                Toast.makeText(
                                    holder.itemView.context,
                                    "Error al eliminar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {

                            Toast.makeText(
                                holder.itemView.context,
                                "Error de conexión",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }

            dialog.show(activity.supportFragmentManager, "EliminarMaestro")
        }
    }

    override fun getItemCount(): Int = lista.size
}
