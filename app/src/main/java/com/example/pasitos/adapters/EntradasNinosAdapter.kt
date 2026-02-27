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
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.FechaCreate
import com.example.pasitos.schemas.Nino
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EntradasNinosAdapter(
    private val lista: MutableList<Nino>,
    private val refrescar: () -> Unit
) : RecyclerView.Adapter<EntradasNinosAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        val activity = holder.itemView.context as? FragmentActivity

        holder.btnAgregar.setOnClickListener {

            activity?.let {

                val dialog = EntradasDialog(nino.nombre) {

                    val calendar = Calendar.getInstance()

                    val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formatoHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

                    val fechaActual = formatoFecha.format(calendar.time)
                    val horaInicio = formatoHora.format(calendar.time)

                    val ninoId = nino.id

                    if (ninoId == null) {
                        Toast.makeText(
                            holder.itemView.context,
                            "Error: niño sin ID",
                            Toast.LENGTH_LONG
                        ).show()
                        return@EntradasDialog
                    }

                    // 🔥 SOLO enviamos lo que el backend necesita
                    val nuevaFecha = FechaCreate(
                        fecha = fechaActual,
                        hora_inicio = horaInicio,
                        nino_id = ninoId
                    )

                    RetrofitClient.instance.crearFecha(nuevaFecha)
                        .enqueue(object : Callback<Any> {

                            override fun onResponse(
                                call: Call<Any>,
                                response: Response<Any>
                            ) {
                                if (response.isSuccessful) {

                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Entrada registrada",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    refrescar()

                                } else {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Error servidor ${response.code()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Any>, t: Throwable) {
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Error conexión: ${t.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                }

                dialog.show(it.supportFragmentManager, "EntradasDialog")
            }
        }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<Nino>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}