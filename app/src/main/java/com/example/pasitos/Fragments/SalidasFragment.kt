package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
    private var listaCompleta: List<FechaAbierta> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_salidas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerSalidas)
        val searchView = view.findViewById<SearchView>(R.id.searchSalidas)

        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = SalidasNinosAdapter(mutableListOf())
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarLista(newText)
                return true
            }
        })

        cargarFechasAbiertas()
    }

    private fun cargarFechasAbiertas() {

        val sucursalId = 1

        RetrofitClient.instance.obtenerFechasAbiertas(sucursalId)
            .enqueue(object : Callback<List<FechaAbierta>> {

                override fun onResponse(
                    call: Call<List<FechaAbierta>>,
                    response: Response<List<FechaAbierta>>
                ) {
                    if (response.isSuccessful) {

                        val lista = response.body() ?: emptyList()
                        listaCompleta = lista
                        adapter.actualizarLista(lista)

                    } else {
                        Toast.makeText(
                            context,
                            "Error servidor: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<FechaAbierta>>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Error al cargar niños",
                        Toast.LENGTH_LONG
                    ).show()
                }            })
    }

    private fun filtrarLista(texto: String?) {

        if (texto.isNullOrEmpty()) {
            adapter.actualizarLista(listaCompleta)
            return
        }

        val textoNormalizado = texto.trim()

        val listaFiltrada = listaCompleta.filter {
            it.nombre.contains(textoNormalizado, ignoreCase = true)
        }

        adapter.actualizarLista(listaFiltrada)
    }
}