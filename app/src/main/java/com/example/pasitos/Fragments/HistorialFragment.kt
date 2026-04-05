package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R
import com.example.pasitos.adapters.HistorialPadreAdapter
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.Nino
import com.example.pasitos.schemas.Padre
import com.example.pasitos.schemas.PagoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialFragment : Fragment() {

    private lateinit var adapter: HistorialPadreAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var txtSinHistorial: TextView

    private var listaPadres = listOf<Padre>()
    private var listaNinos = listOf<Nino>()
    private var listaPagos = listOf<PagoResponse>()

    data class PadreConNinos(val padre: Padre, val ninos: List<NinoConPago>)
    data class NinoConPago(val nino: Nino, val pagos: List<PagoResponse>)

    private var listaAgrupada = mutableListOf<PadreConNinos>()
    private var listaFiltrada = mutableListOf<PadreConNinos>()
    private var filtroOrden = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)

        recycler = view.findViewById(R.id.recyclerHistorial)
        txtSinHistorial = view.findViewById(R.id.txtSinHistorial)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.isNestedScrollingEnabled = false
        recycler.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        // ✅ Spinner orden
        val spOrden = view.findViewById<Spinner>(R.id.sp_orden_historial)
        val opciones = listOf("Sin ordenar", "Mayor pago", "Menor pago", "Más antiguo", "Más reciente")
        val adapterSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spOrden.adapter = adapterSpinner
        spOrden.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                filtroOrden = position
                aplicarFiltros(view.findViewById<SearchView>(R.id.searchHistorial).query?.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ✅ SearchView
        view.findViewById<SearchView>(R.id.searchHistorial)
            .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    aplicarFiltros(newText)
                    return true
                }
            })

        cargarDatos()
        return view
    }

    private fun cargarDatos() {
        RetrofitClient.instance.obtenerPadres().enqueue(object : Callback<List<Padre>> {
            override fun onResponse(call: Call<List<Padre>>, response: Response<List<Padre>>) {
                if (response.isSuccessful) listaPadres = response.body() ?: emptyList()
                cargarNinos()
            }
            override fun onFailure(call: Call<List<Padre>>, t: Throwable) { cargarNinos() }
        })
    }

    private fun cargarNinos() {
        RetrofitClient.instance.obtenerNinos().enqueue(object : Callback<List<Nino>> {
            override fun onResponse(call: Call<List<Nino>>, response: Response<List<Nino>>) {
                if (response.isSuccessful) listaNinos = response.body() ?: emptyList()
                cargarPagos()
            }
            override fun onFailure(call: Call<List<Nino>>, t: Throwable) { cargarPagos() }
        })
    }

    private fun cargarPagos() {
        RetrofitClient.instance.obtenerPagos().enqueue(object : Callback<List<PagoResponse>> {
            override fun onResponse(call: Call<List<PagoResponse>>, response: Response<List<PagoResponse>>) {
                if (response.isSuccessful) listaPagos = response.body() ?: emptyList()
                construirListaAgrupada()
            }
            override fun onFailure(call: Call<List<PagoResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al cargar historial", Toast.LENGTH_SHORT).show()
                construirListaAgrupada()
            }
        })
    }

    private fun construirListaAgrupada() {
        listaAgrupada.clear()
        for (padre in listaPadres) {
            val ninosDelPadre = listaNinos.filter { it.padre_id == padre.id }
            val ninosConPago = ninosDelPadre.map { nino ->
                NinoConPago(nino, listaPagos.filter { it.nino_id == nino.id && it.estado == 1 })
            }.filter { it.pagos.isNotEmpty() }

            if (ninosConPago.isNotEmpty()) {
                listaAgrupada.add(PadreConNinos(padre, ninosConPago))
            }
        }
        aplicarFiltros()
    }

    private fun aplicarFiltros(texto: String? = null) {
        var resultado = listaAgrupada.toMutableList()

        if (!texto.isNullOrBlank()) {
            resultado = resultado.filter {
                it.padre.nombre.contains(texto, ignoreCase = true)
            }.toMutableList()
        }

        resultado = when (filtroOrden) {
            1 -> resultado.sortedByDescending { pcn ->
                pcn.ninos.sumOf { ncp -> ncp.pagos.sumOf { it.pago } }
            }.toMutableList()
            2 -> resultado.sortedBy { pcn ->
                pcn.ninos.sumOf { ncp -> ncp.pagos.sumOf { it.pago } }
            }.toMutableList()
            3 -> resultado.sortedBy { pcn ->
                pcn.ninos.flatMap { it.pagos }.minOfOrNull { it.anio * 100 + it.mes } ?: 0
            }.toMutableList()
            4 -> resultado.sortedByDescending { pcn ->
                pcn.ninos.flatMap { it.pagos }.maxOfOrNull { it.anio * 100 + it.mes } ?: 0
            }.toMutableList()
            else -> resultado
        }

        listaFiltrada.clear()
        listaFiltrada.addAll(resultado)
        actualizarAdapter()
    }

    private fun actualizarAdapter() {
        if (!::adapter.isInitialized) {
            adapter = HistorialPadreAdapter(listaFiltrada)
            recycler.adapter = adapter
        } else {
            adapter.actualizarLista(listaFiltrada)
        }

        if (listaFiltrada.isEmpty()) {
            txtSinHistorial.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        } else {
            txtSinHistorial.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }
    }
}