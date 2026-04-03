package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.example.pasitos.R
import com.example.pasitos.adapters.PadreAdapter
import com.example.pasitos.dialogs.AgregarPadreDialog
import com.example.pasitos.dialogs.EliminarPadreDialog
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.Padre
import com.example.pasitos.schemas.Nino
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PadreFragment : Fragment(R.layout.fragment_padre) {

    private var listaNinos = mutableListOf<Nino>()
    private lateinit var recycler: RecyclerView
    private lateinit var txtSinPadres: TextView
    private lateinit var adapter: PadreAdapter
    private var listaPadres = mutableListOf<Padre>()
    private var listaPadresFiltrada = mutableListOf<Padre>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerPadres)
        txtSinPadres = view.findViewById(R.id.txtSinPadres)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<MaterialButton>(R.id.btnAgregarPadre).setOnClickListener {
            val dialog = AgregarPadreDialog { nombre, telefono ->
                crearPadre(nombre, telefono)
            }
            dialog.show(parentFragmentManager, "AgregarPadre")
        }

        view.findViewById<SearchView>(R.id.searchPadrePagos).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarPadres(newText)
                return true
            }
        })

        cargarNinos()
    }

    private fun actualizarMensaje() {
        if (listaPadresFiltrada.isEmpty()) {
            txtSinPadres.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        } else {
            txtSinPadres.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }
    }

    private fun cargarNinos() {
        RetrofitClient.instance.obtenerNinos().enqueue(object : Callback<List<Nino>> {
            override fun onResponse(call: Call<List<Nino>>, response: Response<List<Nino>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaNinos.clear()
                    listaNinos.addAll(response.body()!!)
                    cargarPadres()
                }
            }
            override fun onFailure(call: Call<List<Nino>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al cargar niños", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarPadres() {
        RetrofitClient.instance.obtenerPadres().enqueue(object : Callback<List<Padre>> {
            override fun onResponse(call: Call<List<Padre>>, response: Response<List<Padre>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaPadres.clear()
                    listaPadres.addAll(response.body()!!)
                    listaPadresFiltrada.clear()
                    listaPadresFiltrada.addAll(listaPadres)

                    adapter = PadreAdapter(
                        listaPadresFiltrada,
                        { padre ->
                            padre.id?.let { id ->
                                cargarNinos()
                                val tieneNinos = listaNinos.any { it.padre_id == id }
                                if (tieneNinos) {
                                    Toast.makeText(requireContext(),
                                        "No se puede eliminar a ${padre.nombre} porque tiene niños registrados",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    val dialog = EliminarPadreDialog(padre.nombre) {
                                        eliminarPadre(id)
                                    }
                                    dialog.show(parentFragmentManager, "EliminarPadre")
                                }
                            }
                        },
                        { padre, nombreNuevo, telefonoNuevo ->
                            padre.id?.let { id -> editarPadre(id, nombreNuevo, telefonoNuevo) }
                        }
                    )
                    recycler.adapter = adapter
                    actualizarMensaje()
                }
            }
            override fun onFailure(call: Call<List<Padre>>, t: Throwable) {}
        })
    }

    private fun filtrarPadres(texto: String?) {
        listaPadresFiltrada.clear()
        if (texto.isNullOrEmpty()) {
            listaPadresFiltrada.addAll(listaPadres)
        } else {
            val textoMinusculas = texto.lowercase()
            listaPadresFiltrada.addAll(
                listaPadres.filter {
                    it.nombre.lowercase().contains(textoMinusculas) ||
                            it.telefono.contains(texto)
                }
            )
        }
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
            actualizarMensaje()
        }
    }

    private fun crearPadre(nombre: String, telefono: String) {
        val padre = Padre(nombre = nombre, telefono = telefono)
        RetrofitClient.instance.crearPadre(padre).enqueue(object : Callback<Padre> {
            override fun onResponse(call: Call<Padre>, response: Response<Padre>) {
                if (response.isSuccessful) {
                    cargarNinos()
                    Toast.makeText(requireContext(), "Padre agregado correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Padre>, t: Throwable) {}
        })
    }

    private fun eliminarPadre(id: Int) {
        RetrofitClient.instance.eliminarPadre(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    cargarNinos()
                    Toast.makeText(requireContext(), "Padre eliminado correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
    }

    private fun editarPadre(id: Int, nombre: String, telefono: String) {
        val padreActualizado = Padre(id = id, nombre = nombre, telefono = telefono)
        RetrofitClient.instance.editarPadre(id, padreActualizado).enqueue(object : Callback<Padre> {
            override fun onResponse(call: Call<Padre>, response: Response<Padre>) {
                if (response.isSuccessful) {
                    cargarNinos()
                    Toast.makeText(requireContext(), "Padre editado correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Padre>, t: Throwable) {}
        })
    }
}