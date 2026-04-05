package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.R
import com.example.pasitos.adapters.SalidasNinosAdapter
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.FechaAbierta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SalidasFragment : Fragment() {

    private lateinit var adapter: SalidasNinosAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var txtSinSalidas: TextView
    private var listaCompleta: List<FechaAbierta> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_salidas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerSalidas)
        txtSinSalidas = view.findViewById(R.id.txtSinSalidas)
        val searchView = view.findViewById<SearchView>(R.id.searchSalidas)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SalidasNinosAdapter(mutableListOf(), parentFragmentManager)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarLista(newText)
                return true
            }
        })

        cargarFechasAbiertas()
    }

    private fun actualizarMensaje(lista: List<FechaAbierta>) {
        if (lista.isEmpty()) {
            txtSinSalidas.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            txtSinSalidas.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun cargarFechasAbiertas() {
        val prefs = requireContext().getSharedPreferences("pasitos_prefs", android.content.Context.MODE_PRIVATE)
        val sucursalId = prefs.getInt("sucursal_id", 1)
        RetrofitClient.instance.obtenerFechasAbiertas(sucursalId)
            .enqueue(object : Callback<List<FechaAbierta>> {
                override fun onResponse(call: Call<List<FechaAbierta>>, response: Response<List<FechaAbierta>>) {
                    if (response.isSuccessful) {
                        val lista = response.body() ?: emptyList()
                        listaCompleta = lista
                        adapter.actualizarLista(lista)
                        actualizarMensaje(lista)
                    } else {
                        Toast.makeText(requireContext(), "Error servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<List<FechaAbierta>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error al cargar niños", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun filtrarLista(texto: String?) {
        if (texto.isNullOrEmpty()) {
            adapter.actualizarLista(listaCompleta)
            actualizarMensaje(listaCompleta)
            return
        }
        val listaFiltrada = listaCompleta.filter {
            it.nombre.contains(texto.trim(), ignoreCase = true)
        }
        adapter.actualizarLista(listaFiltrada)
        actualizarMensaje(listaFiltrada)
    }
}