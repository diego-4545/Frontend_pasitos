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
import com.google.android.material.button.MaterialButton
import java.util.Calendar

class CitasActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CitasAdapter
    private lateinit var gridDias: GridLayout
    private lateinit var txtMesAnio: TextView
    private val listaCitas = mutableListOf<Cita>()
    private val listaCitasFiltrada = mutableListOf<Cita>()
    private val listaPadres = listOf("Pedro Pérez", "Ana López", "Carlos García", "María Torres", "Juan Martínez")

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
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val density = resources.displayMetrics.density
        val maxWidth = (500 * density).toInt()
        val maxHeight = (725 * density).toInt()
        val finalWidth = if (screenWidth > maxWidth) maxWidth else screenWidth
        val finalHeight = if (screenHeight > maxHeight) maxHeight else screenHeight
        window.setLayout(finalWidth, finalHeight)

        findViewById<ImageButton>(R.id.salida).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageButton>(R.id.casa).setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

        listaCitas.addAll(listOf(
            Cita(1, "2026-03-07", "09:00", "Consulta general", "Pedro Pérez"),
            Cita(2, "2026-03-07", "10:30", "Revisión mensual", "Ana López"),
            Cita(3, "2026-03-08", "11:00", "Primera visita", "Carlos García"),
            Cita(4, "2026-04-08", "12:00", "Seguimiento", "María Torres"),
            Cita(5, "2026-05-10", "09:30", "Evaluación", "Juan Martínez"),
        ))
        listaCitasFiltrada.addAll(listaCitas)

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

        renderizarCalendario()

        // ✅ Ver todas las citas
        findViewById<MaterialButton>(R.id.btnVerTodas).setOnClickListener {
            listaCitasFiltrada.clear()
            listaCitasFiltrada.addAll(listaCitas)
            adapter.notifyDataSetChanged()
        }

        findViewById<MaterialButton>(R.id.btnAgregarCita).setOnClickListener {
            mostrarModalAgregar()
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
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            vacio.layoutParams = params
            gridDias.addView(vacio)
        }

        for (dia in 1..diasEnMes) {
            val tieneCita = diasConCitas.contains(dia)
            val esHoy = dia == diaHoy && mesActual == mesHoy && anioActual == anioHoy

            val container = FrameLayout(this).apply {
                val params = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(4, 4, 4, 4)
                }
                layoutParams = params
            }

            val circulo = TextView(this).apply {
                text = dia.toString()
                gravity = Gravity.CENTER
                textSize = 12f
                setPadding(0, 16, 0, 16)

                val drawable = android.graphics.drawable.GradientDrawable().apply {
                    shape = android.graphics.drawable.GradientDrawable.OVAL
                    when {
                        esHoy && tieneCita -> {
                            // ✅ Hoy con citas -> verde claro
                            setColor(Color.parseColor("#81C784"))
                        }
                        esHoy -> {
                            // ✅ Hoy sin citas -> azul claro
                            setColor(Color.parseColor("#4FC3F7"))
                        }
                        tieneCita -> {
                            // ✅ Con citas -> verde
                            setColor(Color.parseColor("#599B3D"))
                        }
                        else -> setColor(Color.TRANSPARENT)
                    }
                }
                background = drawable

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
                    if (citasDelDia.isEmpty()) {
                        listaCitasFiltrada.addAll(listaCitas)
                    } else {
                        // ✅ Solo filtrar, nunca abrir modal automáticamente
                        listaCitasFiltrada.addAll(citasDelDia)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            val circleParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.CENTER }
            circulo.layoutParams = circleParams
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
        val opciones = mutableListOf("Seleccionar padre...").apply { addAll(listaPadres) }
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
            val padre = spPadre.selectedItem.toString()

            if (fecha == "Seleccionar fecha" || hora == "Seleccionar hora" ||
                descripcion.isBlank() || padre == "Seleccionar padre...") {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevaCita = Cita(
                id = listaCitas.size + 1,
                fecha = fecha, hora = hora,
                descripcion = descripcion, padreNombre = padre
            )
            listaCitas.add(nuevaCita)
            listaCitasFiltrada.add(nuevaCita)
            adapter.notifyDataSetChanged()
            renderizarCalendario()
            Toast.makeText(this, "Cita agregada", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
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
            val padre = spPadre.selectedItem.toString()

            if (descripcion.isBlank() || padre == "Seleccionar padre...") {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val citaEditada = cita.copy(
                fecha = fecha, hora = hora,
                descripcion = descripcion, padreNombre = padre
            )
            val index = listaCitas.indexOfFirst { it.id == cita.id }
            val indexFiltrada = listaCitasFiltrada.indexOfFirst { it.id == cita.id }
            if (index != -1) listaCitas[index] = citaEditada
            if (indexFiltrada != -1) listaCitasFiltrada[indexFiltrada] = citaEditada

            adapter.notifyDataSetChanged()
            renderizarCalendario()
            Toast.makeText(this, "Cita actualizada", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
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
            listaCitas.removeAll { it.id == cita.id }
            listaCitasFiltrada.removeAll { it.id == cita.id }
            adapter.notifyDataSetChanged()
            renderizarCalendario()
            Toast.makeText(this, "Cita eliminada", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }
}