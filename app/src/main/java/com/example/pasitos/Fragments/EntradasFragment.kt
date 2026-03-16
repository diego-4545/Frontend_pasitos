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
import com.example.pasitos.adapters.EntradasNinosAdapter
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.Nino
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntradasFragment : Fragment() {

    private lateinit var adapter: EntradasNinosAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var txtSinEntradas: TextView
    private var listaCompleta: List<Nino> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entradas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerEntradas)
        txtSinEntradas = view.findViewById(R.id.txtSinEntradas)
        val searchView = view.findViewById<SearchView>(R.id.searchEntradas)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = EntradasNinosAdapter(mutableListOf()) {
            cargarNinosDisponibles()
        }
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarLista(newText)
                return true
            }
        })

        cargarNinosDisponibles()
    }

    private fun actualizarMensaje(lista: List<Nino>) {
        if (lista.isEmpty()) {
            txtSinEntradas.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            txtSinEntradas.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun cargarNinosDisponibles() {
        val sucursalId = 1
        RetrofitClient.instance.obtenerNinosDisponibles(sucursalId)
            .enqueue(object : Callback<List<Nino>> {
                override fun onResponse(call: Call<List<Nino>>, response: Response<List<Nino>>) {
                    if (response.isSuccessful) {
                        val lista = response.body() ?: emptyList()
                        listaCompleta = lista
                        adapter.actualizarLista(lista)
                        actualizarMensaje(lista)
                    } else {
                        Toast.makeText(context, "Error servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<List<Nino>>, t: Throwable) {
                    Toast.makeText(context, "Error al cargar niños", Toast.LENGTH_LONG).show()
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
            it.nombre.trim().contains(texto.trim(), ignoreCase = true)
        }
        adapter.actualizarLista(listaFiltrada)
        actualizarMensaje(listaFiltrada)
    }
}