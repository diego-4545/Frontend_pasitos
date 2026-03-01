package com.example.pasitos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R
import com.example.pasitos.dialogs.SalidasDialog
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.FechaAbierta
import com.example.pasitos.schemas.FechaUpdate
import com.example.pasitos.schemas.Nino
import com.example.pasitos.schemas.PagoCreate
import com.example.pasitos.schemas.PagoResponse
import com.example.pasitos.schemas.PagoUpdate
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SalidasNinosAdapter(
    private val lista: MutableList<FechaAbierta>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<SalidasNinosAdapter.ViewHolder>() {

    private val preciosPaquete = mapOf(
        1 to 3150.0,
        2 to 3350.0,
        3 to 3550.0,
        4 to 3750.0,
        5 to 3950.0,
        6 to 4200.0,
        7 to 4750.0,
        8 to 5000.0,
        9 to 5300.0
    )

    private fun horasDePaquete(paquete: Int): Int = paquete + 3

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
            val dialog = SalidasDialog(fecha.nombre) {
                val pos = holder.adapterPosition
                if (pos == RecyclerView.NO_POSITION) return@SalidasDialog
                obtenerNinoYRegistrarSalida(holder, pos, fecha)
            }
            dialog.show(fragmentManager, "SalidasDialog")
        }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<FechaAbierta>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    private fun obtenerNinoYRegistrarSalida(holder: ViewHolder, position: Int, fecha: FechaAbierta) {
        val contexto = holder.itemView.context

        RetrofitClient.instance.obtenerNinos()
            .enqueue(object : Callback<List<Nino>> {
                override fun onResponse(call: Call<List<Nino>>, response: Response<List<Nino>>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(contexto, "Error al obtener datos del niño", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val nino = response.body()?.firstOrNull { it.id == fecha.nino_id }
                    if (nino == null) {
                        Toast.makeText(contexto, "No se encontró el niño", Toast.LENGTH_SHORT).show()
                        return
                    }

                    android.util.Log.d("DEBUG_SALIDA", "nino: ${nino.nombre} | paquete: ${nino.paquete}")
                    registrarSalida(holder, position, fecha, nino.paquete)
                }

                override fun onFailure(call: Call<List<Nino>>, t: Throwable) {
                    Toast.makeText(contexto, "Error de conexión al obtener niño", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun registrarSalida(holder: ViewHolder, position: Int, fecha: FechaAbierta, paquete: Int) {
        val contexto = holder.itemView.context

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val horaActual = sdf.format(Calendar.getInstance().time)
        val horasTotales = calcularHorasTotales(fecha.hora_inicio, horaActual)

        android.util.Log.d("DEBUG_SALIDA", "paquete: $paquete | hora_inicio: ${fecha.hora_inicio} | hora_fin: $horaActual | horasTotales: $horasTotales")

        val fechaUpdate = FechaUpdate(
            hora_fin = horaActual,
            tiempo_estancia = horasTotales
        )

        RetrofitClient.instance.actualizarFecha(fecha.fecha_id, fechaUpdate)
            .enqueue(object : Callback<FechaAbierta> {
                override fun onResponse(call: Call<FechaAbierta>, response: Response<FechaAbierta>) {
                    if (response.isSuccessful) {
                        lista.removeAt(position)
                        notifyItemRemoved(position)
                        actualizarPagos(fecha.nino_id, paquete, horasTotales, contexto)
                    } else {
                        Toast.makeText(contexto, "Error al registrar salida", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<FechaAbierta>, t: Throwable) {
                    Toast.makeText(contexto, "Error de conexión al registrar salida", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun actualizarPagos(ninoId: Int, paquete: Int, horasTotales: Int, contexto: Context) {
        val cal = Calendar.getInstance()
        val mesActual = cal.get(Calendar.MONTH) + 1
        val anioActual = cal.get(Calendar.YEAR)

        val precioPaquete = preciosPaquete[paquete] ?: 0.0
        val horasIncluidas = horasDePaquete(paquete)

        android.util.Log.d("DEBUG_PAGO", "paquete: $paquete | precioPaquete: $precioPaquete | horasIncluidas: $horasIncluidas | horasTotales: $horasTotales")

        RetrofitClient.instance.obtenerPagosPorNino(ninoId)
            .enqueue(object : Callback<List<PagoResponse>> {
                override fun onResponse(call: Call<List<PagoResponse>>, response: Response<List<PagoResponse>>) {
                    if (!response.isSuccessful) return

                    val pagos = response.body() ?: emptyList()
                    val pagoExistente = pagos.firstOrNull { it.mes == mesActual && it.anio == anioActual }

                    android.util.Log.d("DEBUG_PAGO", "pagoExistente: $pagoExistente")

                    if (pagoExistente == null) {
                        val deudaInicial = if (horasTotales > horasIncluidas) {
                            precioPaquete + 80.0
                        } else {
                            precioPaquete
                        }

                        val nuevoPago = PagoCreate(
                            nino_id = ninoId,
                            mes = mesActual,
                            anio = anioActual,
                            deuda = deudaInicial
                        )

                        android.util.Log.d("DEBUG_PAGO", "Creando pago con deuda: $deudaInicial")

                        RetrofitClient.instance.crearPago(nuevoPago)
                            .enqueue(object : Callback<PagoResponse> {
                                override fun onResponse(call: Call<PagoResponse>, response: Response<PagoResponse>) {
                                    if (response.isSuccessful) {
                                        android.util.Log.d("DEBUG_PAGO", "Pago creado: ${response.body()}")
                                        Toast.makeText(contexto, "Salida registrada y pago creado", Toast.LENGTH_SHORT).show()
                                    } else {
                                        android.util.Log.e("DEBUG_PAGO", "Error al crear pago: ${response.code()} - ${response.errorBody()?.string()}")
                                        Toast.makeText(contexto, "Error al crear pago", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onFailure(call: Call<PagoResponse>, t: Throwable) {}
                            })
                    } else {
                        val deudaActual = pagoExistente.deuda
                        val nuevaDeuda = if (horasTotales > horasIncluidas) {
                            deudaActual + 80.0
                        } else {
                            deudaActual
                        }

                        android.util.Log.d("DEBUG_PAGO", "deudaActual: $deudaActual | nuevaDeuda: $nuevaDeuda")

                        if (nuevaDeuda > deudaActual) {
                            val pagoUpdate = PagoUpdate(deuda = nuevaDeuda)
                            RetrofitClient.instance.actualizarPago(pagoExistente.id, pagoUpdate)
                                .enqueue(object : Callback<PagoResponse> {
                                    override fun onResponse(call: Call<PagoResponse>, response: Response<PagoResponse>) {
                                        if (response.isSuccessful) {
                                            android.util.Log.d("DEBUG_PAGO", "Pago actualizado: ${response.body()}")
                                            Toast.makeText(contexto, "Salida registrada, se agregaron $80 por hora extra", Toast.LENGTH_SHORT).show()
                                        } else {
                                            android.util.Log.e("DEBUG_PAGO", "Error al actualizar: ${response.code()} - ${response.errorBody()?.string()}")
                                            Toast.makeText(contexto, "Error al actualizar pago", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    override fun onFailure(call: Call<PagoResponse>, t: Throwable) {}
                                })
                        } else {
                            Toast.makeText(contexto, "Salida registrada correctamente", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<PagoResponse>>, t: Throwable) {
                    Toast.makeText(contexto, "Error de conexión al obtener pagos", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun calcularHorasTotales(horaInicio: String, horaFin: String): Int {
        val inicioLimpio = horaInicio.substring(0, 5)
        val finLimpio = horaFin.substring(0, 5)

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val inicio = sdf.parse(inicioLimpio) ?: return 0
        val fin = sdf.parse(finLimpio) ?: return 0

        val diferenciaMs = fin.time - inicio.time
        val minutosTotales = diferenciaMs / 1000 / 60

        val horas = minutosTotales / 60
        val minutosRestantes = minutosTotales % 60

        return if (minutosRestantes <= 20) horas.toInt() else (horas + 1).toInt()
    }
}