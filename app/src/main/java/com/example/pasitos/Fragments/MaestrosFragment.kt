package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.MaestroAdapter
import com.example.pasitos.R
import com.example.pasitos.dialogs.AgregarMaestroDialog
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.Maestro
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MaestrosFragment : Fragment(R.layout.fragment_maestro) {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: MaestroAdapter
    private var listaMaestros = mutableListOf<Maestro>()
    private var listaMaestrosFiltrada = mutableListOf<Maestro>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerMaestros)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = MaestroAdapter(listaMaestrosFiltrada) { refrescarLista() }
        recycler.adapter = adapter

        cargarMaestros()

        val btnAgregar = view.findViewById<MaterialButton>(R.id.btnAgregarMaestro)
        btnAgregar.setOnClickListener {
            val dialog = AgregarMaestroDialog { nuevoMaestro ->
                crearMaestro(nuevoMaestro)
            }
            dialog.show(parentFragmentManager, "AgregarMaestro")
        }

        val searchView = view.findViewById<SearchView>(R.id.searchMaestro)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarMaestros(newText)
                return true
            }
        })
    }

    private fun cargarMaestros() {
        RetrofitClient.instance.obtenerMaestros().enqueue(object : Callback<List<Maestro>> {
            override fun onResponse(call: Call<List<Maestro>>, response: Response<List<Maestro>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaMaestros.clear()
                    listaMaestros.addAll(response.body()!!)
                    listaMaestrosFiltrada.clear()
                    listaMaestrosFiltrada.addAll(listaMaestros)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Maestro>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al cargar maestros", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun crearMaestro(maestro: Maestro) {
        RetrofitClient.instance.crearMaestro(maestro).enqueue(object : Callback<Maestro> {
            override fun onResponse(call: Call<Maestro>, response: Response<Maestro>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Maestro agregado", Toast.LENGTH_SHORT).show()
                    cargarMaestros()
                } else {
                    Toast.makeText(requireContext(), "Error al agregar maestro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Maestro>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filtrarMaestros(texto: String?) {
        listaMaestrosFiltrada.clear()
        if (texto.isNullOrEmpty()) {
            listaMaestrosFiltrada.addAll(listaMaestros)
        } else {
            val txt = texto.lowercase()
            listaMaestrosFiltrada.addAll(
                listaMaestros.filter { it.nombre.lowercase().contains(txt) || it.telefono.contains(txt) }
            )
        }
        adapter.notifyDataSetChanged()
    }

    private fun refrescarLista() {
        cargarMaestros()
    }
}
