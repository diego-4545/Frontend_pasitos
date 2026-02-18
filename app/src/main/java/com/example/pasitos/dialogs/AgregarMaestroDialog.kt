package com.example.pasitos.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
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
import retrofit2.http.POST

// DTO para respuesta del servidor
data class CrearMaestroResponse(val status: String, val mensaje: String)

// Retrofit API
interface ApiService {
    @POST("maestros/") // coincide con el prefix de tu router FastAPI
    suspend fun crearMaestro(@Body maestro: Maestro): CrearMaestroResponse
}

// Retrofit Client con API Key
object RetrofitClient {
    private const val BASE_URL = "https://backend-pasitos.onrender.com/" // tu URL
    private const val API_KEY = "m802334711-5085abf5ad7f25fcb144e440" // <-- coloca tu API Key aquí

    val instance: ApiService by lazy {
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

        retrofit.create(ApiService::class.java)
    }
}

class AgregarMaestroDialog(
    private val onGuardar: (Maestro) -> Unit
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

        // Spinner
        val guarderias = arrayOf("Seleccionar sucursal...", "Tulipanes", "Pinos")
        val adapterSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, guarderias)
        spGuarderia.adapter = adapterSpinner

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardar.setOnClickListener {

            val usuario = txtUsuario.text.toString().trim()
            val contrasena = txtContrasena.text.toString().trim()
            val nombre = txtNombre.text.toString().trim()
            val telefono = txtTelefono.text.toString().trim()
            val sucursal = spGuarderia.selectedItemPosition

            // Validaciones locales
            if (usuario.isBlank() || contrasena.isBlank() || nombre.isBlank() || telefono.isBlank() || sucursal == 0) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (usuario.length < 8) {
                Toast.makeText(requireContext(), "El usuario debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena.length < 8) {
                Toast.makeText(requireContext(), "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (telefono.length != 10 || !telefono.all { it.isDigit() }) {
                Toast.makeText(requireContext(), "El teléfono debe tener 10 dígitos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear objeto Maestro
            val maestro = Maestro(
                nombre = nombre,
                telefono = telefono,
                username = usuario,
                password = contrasena,
                sucursal = sucursal
            )

            // Llamada al servidor
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val respuesta = RetrofitClient.instance.crearMaestro(maestro)
                    withContext(Dispatchers.Main) {
                        if (respuesta.status == "ok") {
                            Toast.makeText(requireContext(), respuesta.mensaje, Toast.LENGTH_SHORT).show()
                            onGuardar(maestro)
                            dialog.dismiss()
                        } else {
                            Toast.makeText(requireContext(), respuesta.mensaje, Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return dialog
    }
}
