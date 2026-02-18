package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pasitos.R
import com.example.pasitos.schemas.Maestro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

// DTO respuesta
data class EditarMaestroResponse(
    val status: String,
    val mensaje: String
)

// API solo para este dialog
interface ApiServiceEditar {
    @PUT("maestros/{id}")
    suspend fun editarMaestro(
        @Path("id") id: Int,
        @Body maestro: Maestro
    ): EditarMaestroResponse
}

// Retrofit local
object RetrofitClientEditar {

    private const val BASE_URL = "https://backend-pasitos.onrender.com/"
    private const val API_KEY = "m802334711-5085abf5ad7f25fcb144e440"

    val instance: ApiServiceEditar by lazy {

        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-api-key", API_KEY)
                    .build()
                chain.proceed(request)
            }).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiServiceEditar::class.java)
    }
}

class EditarMaestroDialog(
    private val maestro: Maestro,
    private val onEditado: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_agregar_maestro, null)

        val txtUsuario = view.findViewById<EditText>(R.id.txtUsuario)
        val txtContrasena = view.findViewById<EditText>(R.id.txtContrasena)
        val txtNombre = view.findViewById<EditText>(R.id.txtNombre)
        val txtTelefono = view.findViewById<EditText>(R.id.txtTelefono)
        val spGuarderia = view.findViewById<Spinner>(R.id.spGuarderia)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        // Precargar datos
        txtUsuario.setText(maestro.username)
        txtContrasena.setText(maestro.password)
        txtNombre.setText(maestro.nombre)
        txtTelefono.setText(maestro.telefono)

        val guarderias = arrayOf("Seleccionar sucursal...", "Tulipanes", "Pinos")
        val adapterSpinner = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            guarderias)
        spGuarderia.adapter = adapterSpinner
        spGuarderia.setSelection(maestro.sucursal)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardar.setOnClickListener {

            val usuario = txtUsuario.text.toString().trim()
            val contrasena = txtContrasena.text.toString().trim()
            val nombre = txtNombre.text.toString().trim()
            val telefono = txtTelefono.text.toString().trim()
            val sucursal = spGuarderia.selectedItemPosition

            if (usuario.isBlank() || nombre.isBlank() || telefono.isBlank() || sucursal == 0) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val maestroEditado = maestro.copy(
                username = usuario,
                password = if (contrasena.isBlank()) maestro.password else contrasena,
                nombre = nombre,
                telefono = telefono,
                sucursal = sucursal
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {

                    val respuesta = RetrofitClientEditar.instance
                        .editarMaestro(maestro.id!!, maestroEditado)

                    withContext(Dispatchers.Main) {

                        if (respuesta.status == "ok") {

                            Toast.makeText(
                                requireContext(),
                                respuesta.mensaje,
                                Toast.LENGTH_SHORT
                            ).show()

                            onEditado()
                            dialog.dismiss()

                        } else {

                            Toast.makeText(
                                requireContext(),
                                respuesta.mensaje,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } catch (e: Exception) {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        return dialog
    }
}
