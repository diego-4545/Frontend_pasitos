package com.example.pasitos.Fragments

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.NinoAdapter
import com.example.pasitos.R
import com.example.pasitos.dialogs.AgregarNinoDialog
import com.example.pasitos.dialogs.InfoNinoDialog
import com.example.pasitos.dialogs.EliminarNinoDialog
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.Nino
import com.example.pasitos.schemas.Padre
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NinoFragment : Fragment(R.layout.fragment_nino) {

    private lateinit var recycler: RecyclerView
    private lateinit var txtSinNinos: TextView
    private lateinit var adapter: NinoAdapter
    private var listaNinos = mutableListOf<Nino>()
    private var listaPadres = listOf<Padre>()
    private var listaNinosFiltrada = mutableListOf<Nino>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerNino)
        txtSinNinos = view.findViewById(R.id.txtSinNinos)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        cargarPadres()

        view.findViewById<MaterialButton>(R.id.btnAgregar).setOnClickListener {
            RetrofitClient.instance.obtenerPadres().enqueue(object : Callback<List<Padre>> {
                override fun onResponse(call: Call<List<Padre>>, response: Response<List<Padre>>) {
                    if (response.isSuccessful && response.body() != null) {
                        listaPadres = response.body()!!

                        if (listaPadres.isEmpty()) {
                            Toast.makeText(requireContext(), "No hay padres disponibles", Toast.LENGTH_SHORT).show()
                            return
                        }

                        val nombresPadres = mutableListOf("Seleccionar padre...").apply {
                            addAll(listaPadres.map { it.nombre })
                        }

                        val dialog = AgregarNinoDialog(nombresPadres) { nombreNino, nombrePadreSeleccionado, sucursalInt, paqueteHoras ->
                            if (nombrePadreSeleccionado == "Seleccionar padre...") {
                                Toast.makeText(requireContext(), "Debes seleccionar un padre", Toast.LENGTH_SHORT).show()
                                return@AgregarNinoDialog
                            }

                            val padreId = listaPadres.find { it.nombre == nombrePadreSeleccionado }?.id
                            if (padreId == null) {
                                Toast.makeText(requireContext(), "Error: padre no encontrado", Toast.LENGTH_SHORT).show()
                                return@AgregarNinoDialog
                            }

                            val nuevoNino = Nino(
                                nombre = nombreNino,
                                padre_id = padreId,
                                sucursal = sucursalInt,
                                paquete = paqueteHoras
                            )

                            crearNino(nuevoNino) { cargarPadres() }
                        }

                        dialog.show(parentFragmentManager, "AgregarNino")
                    }
                }

                override fun onFailure(call: Call<List<Padre>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error al cargar padres", Toast.LENGTH_SHORT).show()
                }
            })
        }

        view.findViewById<SearchView>(R.id.searchNino).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarNinos(newText)
                return true
            }
        })
    }

    private fun actualizarMensaje() {
        if (listaNinosFiltrada.isEmpty()) {
            txtSinNinos.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        } else {
            txtSinNinos.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }
    }

    private fun cargarPadres() {
        RetrofitClient.instance.obtenerPadres().enqueue(object : Callback<List<Padre>> {
            override fun onResponse(call: Call<List<Padre>>, response: Response<List<Padre>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaPadres = response.body()!!
                    cargarNinos()
                }
            }
            override fun onFailure(call: Call<List<Padre>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al cargar padres", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarNinos() {
        listaNinosFiltrada.clear()

        RetrofitClient.instance.obtenerNinos().enqueue(object : Callback<List<Nino>> {
            override fun onResponse(call: Call<List<Nino>>, response: Response<List<Nino>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaNinos.clear()
                    listaNinos.addAll(response.body()!!)
                    listaNinosFiltrada.addAll(listaNinos)

                    adapter = NinoAdapter(
                        listaNinosFiltrada,
                        listaPadres,
                        onEliminar = { nino ->
                            val dialog = EliminarNinoDialog(nino.nombre) { eliminarNino(nino) }
                            dialog.show(parentFragmentManager, "EliminarNino")
                        },
                        onEditar = { nino -> editarNino(nino) },
                        onInfo = { nino ->
                            val nombrePadre = listaPadres.find { it.id == nino.padre_id }?.nombre ?: "Desconocido"
                            val dialog = InfoNinoDialog(
                                nombre = nino.nombre,
                                padre = nombrePadre,
                                guarderia = nino.sucursal.toString(),
                                paquete = nino.paquete
                            )
                            dialog.show(
                                (recycler.context as androidx.fragment.app.FragmentActivity).supportFragmentManager,
                                "InfoNino"
                            )
                        }
                    )

                    recycler.adapter = adapter
                    adapter.notifyDataSetChanged()
                    actualizarMensaje()
                }
            }
            override fun onFailure(call: Call<List<Nino>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al cargar niños", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filtrarNinos(texto: String?) {
        listaNinosFiltrada.clear()
        if (texto.isNullOrEmpty()) {
            listaNinosFiltrada.addAll(listaNinos)
        } else {
            val textoMinusculas = texto.lowercase()
            listaNinosFiltrada.addAll(
                listaNinos.filter { nino ->
                    val nombrePadre = listaPadres.find { it.id == nino.padre_id }?.nombre ?: ""
                    nino.nombre.lowercase().contains(textoMinusculas) ||
                            nombrePadre.lowercase().contains(textoMinusculas)
                }
            )
        }
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
            actualizarMensaje()
        }
    }

    private fun editarNino(ninoActualizado: Nino) {
        RetrofitClient.instance.editarNino(ninoActualizado.id!!, ninoActualizado)
            .enqueue(object : Callback<Nino> {
                override fun onResponse(call: Call<Nino>, response: Response<Nino>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Niño editado correctamente", Toast.LENGTH_SHORT).show()
                        cargarNinos()
                    } else {
                        Toast.makeText(requireContext(), "Error al editar niño", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Nino>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun crearNino(nuevoNino: Nino, onSuccess: (() -> Unit)? = null) {
        RetrofitClient.instance.crearNino(nuevoNino).enqueue(object : Callback<Nino> {
            override fun onResponse(call: Call<Nino>, response: Response<Nino>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(requireContext(), "Niño agregado correctamente", Toast.LENGTH_SHORT).show()
                    onSuccess?.invoke()
                } else {
                    Toast.makeText(requireContext(), "Error al agregar niño", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Nino>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarNino(nino: Nino) {
        RetrofitClient.instance.eliminarNino(nino.id ?: return)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Niño eliminado correctamente", Toast.LENGTH_SHORT).show()
                        cargarNinos()
                    } else {
                        Toast.makeText(requireContext(), "Error al eliminar niño", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error de conexión al eliminar niño", Toast.LENGTH_SHORT).show()
                }
            })
    }
}