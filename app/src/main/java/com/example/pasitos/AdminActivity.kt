package com.example.pasitos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdminActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_admin)

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val salida = findViewById<ImageButton>(R.id.salida)
        val pagos = findViewById<ImageButton>(R.id.pagos)
        val historial = findViewById<ImageButton>(R.id.historial)
        val registros = findViewById<ImageButton>(R.id.registros)
        val citas = findViewById<ImageButton>(R.id.citas)



        salida.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        pagos.setOnClickListener {
            startActivity(Intent(this, PagosActivity::class.java))
        }

        historial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }

        registros.setOnClickListener {
            startActivity(Intent(this, CRUDSActivity::class.java))
        }

        citas.setOnClickListener {
            startActivity(Intent(this, CitasActivity::class.java))
        }


    }
}
