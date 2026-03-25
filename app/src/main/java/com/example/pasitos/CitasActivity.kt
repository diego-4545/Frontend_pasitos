package com.example.pasitos

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.Cita
import com.example.pasitos.schemas.CitaRequest
import com.example.pasitos.schemas.CitaResponse
import com.example.pasitos.schemas.Padre
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CitasActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CitasAdapter
    private lateinit var gridDias: GridLayout
    private lateinit var txtMesAnio: TextView
    private lateinit var txtSinCitas: TextView

    private val listaCitas = mutableListOf<Cita>()
    private val listaCitasFiltrada = mutableListOf<Cita>()
    private var listaPadres = listOf<Padre>()

    private var mesActual = Calendar.getInstance().get(Calendar.MONTH)
    private var anioActual = Calendar.getInstance().get(Calendar.YEAR)

    private val nombresMeses = arrayOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_citas)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val density = resources.displayMetrics.density
        val maxWidth = (500 * density).toInt()
        val maxHeight = (725 * density).toInt()
        val finalWidth = if (displayMetrics.widthPixels > maxWidth) maxWidth else displayMetrics.widthPixels
        val finalHeight = if (displayMetrics.heightPixels > maxHeight) maxHeight else displayMetrics.heightPixels
        window.setLayout(finalWidth, finalHeight)

        findViewById<ImageButton>(R.id.salida).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageButton>(R.id.casa).setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

        txtSinCitas = findViewById(R.id.txtSinCitas)
        recycler = findViewById(R.id.recyclerCitas)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = CitasAdapter(
            listaCitasFiltrada,
            onInfo = { mostrarModalInfo(it) },
            onEditar = { mostrarModalEditar(it) },
            onEliminar = { mostrarModalEliminar(it) }
        )
        recycler.adapter = adapter

        gridDias = findViewById(R.id.gridDias)
        txtMesAnio = findViewById(R.id.txtMesAnio)

        findViewById<Button>(R.id.btnMesAnterior).setOnClickListener {
            mesActual--
            if (mesActual < 0) { mesActual = 11; anioActual-- }
            renderizarCalendario()
        }
        findViewById<Button>(R.id.btnMesSiguiente).setOnClickListener {
            mesActual++
            if (mesActual > 11) { mesActual = 0; anioActual++ }
            renderizarCalendario()
        }

        findViewById<MaterialButton>(R.id.btnVerTodas).setOnClickListener {
            listaCitasFiltrada.clear()
            listaCitasFiltrada.addAll(listaCitas)
            adapter.notifyDataSetChanged()
            actualizarMensajeSinCitas()
        }

        findViewById<MaterialButton>(R.id.btnAgregarCita).setOnClickListener {
            mostrarModalAgregar()
        }

        cargarPadres()
    }


    private fun cargarPadres() {
        RetrofitClient.instance.obtenerPadres().enqueue(object : Callback<List<Padre>> {
            override fun onResponse(call: Call<List<Padre>>, response: Response<List<Padre>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaPadres = response.body()!!
                }
                cargarCitas()
            }
            override fun onFailure(call: Call<List<Padre>>, t: Throwable) {
                cargarCitas()
            }
        })
    }

    private fun cargarCitas() {
        RetrofitClient.instance.obtenerCitas().enqueue(object : Callback<List<CitaResponse>> {
            override fun onResponse(call: Call<List<CitaResponse>>, response: Response<List<CitaResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaCitas.clear()
                    listaCitas.addAll(response.body()!!.map { citaResponseToCita(it) })

                    val calHoy = Calendar.getInstance()
                    val fechaHoy = String.format(
                        "%04d-%02d-%02d",
                        calHoy.get(Calendar.YEAR),
                        calHoy.get(Calendar.MONTH) + 1,
                        calHoy.get(Calendar.DAY_OF_MONTH)
                    )
                    listaCitasFiltrada.clear()
                    listaCitasFiltrada.addAll(listaCitas.filter { it.fecha == fechaHoy })
                    adapter.notifyDataSetChanged()
                    actualizarMensajeSinCitas()
                    renderizarCalendario()
                } else {
                    Toast.makeText(this@CitasActivity, "Error al cargar citas", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<CitaResponse>>, t: Throwable) {
                Toast.makeText(this@CitasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun citaResponseToCita(r: CitaResponse): Cita {
        val nombrePadre = listaPadres.find { it.id == r.padre_id }?.nombre ?: "Padre desconocido"
        return Cita(
            id = r.id,
            fecha = r.fecha,
            hora = r.hora.substring(0, 5),
            descripcion = r.descripcion,
            padreNombre = nombrePadre
        )
    }


    private fun fechaHoraEsValida(fecha: String, hora: String): Boolean {
        return try {
            val ahora = Calendar.getInstance()
            val partes = fecha.split("-")
            val horaPartes = hora.split(":")

            val citaCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, partes[0].toInt())
                set(Calendar.MONTH, partes[1].toInt() - 1)
                set(Calendar.DAY_OF_MONTH, partes[2].toInt())
                set(Calendar.HOUR_OF_DAY, horaPartes[0].toInt())
                set(Calendar.MINUTE, horaPartes[1].toInt())
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            citaCal.after(ahora)
        } catch (e: Exception) {
            false
        }
    }


    private fun actualizarMensajeSinCitas() {
        if (listaCitasFiltrada.isEmpty()) {
            txtSinCitas.visibility = TextView.VISIBLE
            recycler.visibility = RecyclerView.GONE
        } else {
            txtSinCitas.visibility = TextView.GONE
            recycler.visibility = RecyclerView.VISIBLE
        }
    }

    private fun renderizarCalendario() {
        gridDias.removeAllViews()
        txtMesAnio.text = "${nombresMeses[mesActual]} $anioActual"

        val calHoy = Calendar.getInstance()
        val diaHoy = calHoy.get(Calendar.DAY_OF_MONTH)
        val mesHoy = calHoy.get(Calendar.MONTH)
        val anioHoy = calHoy.get(Calendar.YEAR)

        val cal = Calendar.getInstance()
        cal.set(anioActual, mesActual, 1)
        val primerDia = cal.get(Calendar.DAY_OF_WEEK) - 1
        val diasEnMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val diasConCitas = listaCitas
            .filter {
                val partes = it.fecha.split("-")
                partes[0].toInt() == anioActual && partes[1].toInt() == mesActual + 1
            }
            .map { it.fecha.split("-")[2].toInt() }
            .toSet()

        for (i in 0 until primerDia) {
            val vacio = TextView(this)
            vacio.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            gridDias.addView(vacio)
        }

        for (dia in 1..diasEnMes) {
            val tieneCita = diasConCitas.contains(dia)
            val esHoy = dia == diaHoy && mesActual == mesHoy && anioActual == anioHoy

            val container = FrameLayout(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(4, 4, 4, 4)
                }
            }

            val circulo = TextView(this).apply {
                text = dia.toString()
                gravity = Gravity.CENTER
                textSize = 12f
                setPadding(0, 16, 0, 16)
                background = android.graphics.drawable.GradientDrawable().apply {
                    shape = android.graphics.drawable.GradientDrawable.OVAL
                    when {
                        esHoy && tieneCita -> setColor(Color.parseColor("#81C784"))
                        esHoy -> setColor(Color.parseColor("#4FC3F7"))
                        tieneCita -> setColor(Color.parseColor("#599B3D"))
                        else -> setColor(Color.TRANSPARENT)
                    }
                }
                if (esHoy || tieneCita) {
                    setTextColor(Color.WHITE)
                    setTypeface(null, Typeface.BOLD)
                } else {
                    setTextColor(Color.parseColor("#333333"))
                }
                setOnClickListener {
                    val fecha = String.format("%04d-%02d-%02d", anioActual, mesActual + 1, dia)
                    val citasDelDia = listaCitas.filter { it.fecha == fecha }
                    listaCitasFiltrada.clear()
                    listaCitasFiltrada.addAll(if (citasDelDia.isEmpty()) listaCitas else citasDelDia)
                    adapter.notifyDataSetChanged()
                    actualizarMensajeSinCitas()
                }
            }

            circulo.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.CENTER }

            container.addView(circulo)
            gridDias.addView(container)
        }
    }

    private fun mostrarDatePicker(boton: Button) {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            boton.text = String.format("%04d-%02d-%02d", year, month + 1, day)
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun mostrarTimePicker(boton: Button) {
        val cal = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            boton.text = String.format("%02d:%02d", hour, minute)
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun configurarSpinnerPadres(spinner: Spinner, seleccionado: String? = null) {
        val opciones = mutableListOf("Seleccionar padre...").apply {
            addAll(listaPadres.map { it.nombre })
        }
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner
        seleccionado?.let {
            val index = opciones.indexOf(it)
            if (index >= 0) spinner.setSelection(index)
        }
    }


    private fun mostrarModalInfo(cita: Cita) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_info_cita, null)
        view.findViewById<TextView>(R.id.txtFechaInfo).text = cita.fecha
        view.findViewById<TextView>(R.id.txtHoraInfo).text = cita.hora
        view.findViewById<TextView>(R.id.txtDescripcionInfo).text = cita.descripcion
        view.findViewById<TextView>(R.id.txtPadreInfo).text = cita.padreNombre
        val dialog = AlertDialog.Builder(this).setView(view).create()
        view.findViewById<Button>(R.id.btnCerrarInfo).setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun mostrarModalAgregar() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_cita, null)
        val btnFecha = view.findViewById<Button>(R.id.btnSeleccionarFecha)
        val btnHora = view.findViewById<Button>(R.id.btnSeleccionarHora)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionCita)
        val spPadre = view.findViewById<Spinner>(R.id.spPadreCita)

        btnFecha.setOnClickListener { mostrarDatePicker(btnFecha) }
        btnHora.setOnClickListener { mostrarTimePicker(btnHora) }
        configurarSpinnerPadres(spPadre)

        val dialog = AlertDialog.Builder(this).setView(view).create()
        view.findViewById<Button>(R.id.btnGuardarCita).setOnClickListener {
            val fecha = btnFecha.text.toString()
            val hora = btnHora.text.toString()
            val descripcion = etDescripcion.text.toString()
            val nombrePadre = spPadre.selectedItem.toString()

            if (fecha == "Seleccionar fecha" || hora == "Seleccionar hora" ||
                descripcion.isBlank() || nombrePadre == "Seleccionar padre...") {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!fechaHoraEsValida(fecha, hora)) {
                Toast.makeText(this, "La fecha y hora deben ser posteriores al momento actual", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val padreId = listaPadres.find { it.nombre == nombrePadre }?.id
            if (padreId == null) {
                Toast.makeText(this, "Padre no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = CitaRequest(
                padre_id = padreId,
                fecha = fecha,
                hora = "$hora:00",
                descripcion = descripcion
            )

            RetrofitClient.instance.crearCita(request).enqueue(object : Callback<CitaResponse> {
                override fun onResponse(call: Call<CitaResponse>, response: Response<CitaResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CitasActivity, "Cita agregada", Toast.LENGTH_SHORT).show()
                        cargarCitas()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@CitasActivity, "Error al agregar cita", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<CitaResponse>, t: Throwable) {
                    Toast.makeText(this@CitasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
        dialog.show()
    }

    private fun mostrarModalEditar(cita: Cita) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_editar_cita, null)
        val btnFecha = view.findViewById<Button>(R.id.btnSeleccionarFechaEditar)
        val btnHora = view.findViewById<Button>(R.id.btnSeleccionarHoraEditar)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcionCitaEditar)
        val spPadre = view.findViewById<Spinner>(R.id.spPadreCitaEditar)

        btnFecha.text = cita.fecha
        btnHora.text = cita.hora
        etDescripcion.setText(cita.descripcion)
        configurarSpinnerPadres(spPadre, cita.padreNombre)

        btnFecha.setOnClickListener { mostrarDatePicker(btnFecha) }
        btnHora.setOnClickListener { mostrarTimePicker(btnHora) }

        val dialog = AlertDialog.Builder(this).setView(view).create()
        view.findViewById<Button>(R.id.btnGuardarCitaEditar).setOnClickListener {
            val fecha = btnFecha.text.toString()
            val hora = btnHora.text.toString()
            val descripcion = etDescripcion.text.toString()
            val nombrePadre = spPadre.selectedItem.toString()

            if (descripcion.isBlank() || nombrePadre == "Seleccionar padre...") {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!fechaHoraEsValida(fecha, hora)) {
                Toast.makeText(this, "La fecha y hora deben ser posteriores al momento actual", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val padreId = listaPadres.find { it.nombre == nombrePadre }?.id
            if (padreId == null) {
                Toast.makeText(this, "Padre no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = CitaRequest(
                padre_id = padreId,
                fecha = fecha,
                hora = if (hora.length == 5) "$hora:00" else hora,
                descripcion = descripcion
            )

            RetrofitClient.instance.editarCita(cita.id, request).enqueue(object : Callback<CitaResponse> {
                override fun onResponse(call: Call<CitaResponse>, response: Response<CitaResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CitasActivity, "Cita actualizada", Toast.LENGTH_SHORT).show()
                        cargarCitas()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@CitasActivity, "Error al editar cita", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<CitaResponse>, t: Throwable) {
                    Toast.makeText(this@CitasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
        dialog.show()
    }

    private fun mostrarModalEliminar(cita: Cita) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_eliminar_cita, null)
        view.findViewById<TextView>(R.id.txtMensajeCita).text =
            "¿Eliminar cita del ${cita.fecha} a las ${cita.hora}?"

        val dialog = AlertDialog.Builder(this).setView(view).create()
        view.findViewById<Button>(R.id.btnCancelarCita).setOnClickListener { dialog.dismiss() }
        view.findViewById<Button>(R.id.btnConfirmarEliminarCita).setOnClickListener {
            RetrofitClient.instance.eliminarCita(cita.id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CitasActivity, "Cita eliminada", Toast.LENGTH_SHORT).show()
                        cargarCitas()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@CitasActivity, "Error al eliminar cita", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@CitasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
        dialog.show()
    }
}