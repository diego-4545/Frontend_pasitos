package com.example.pasitos

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pasitos.adapters.PagosAdapter

class PagosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_pagos)

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

        // 1. Configurar padding para Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPagos)
        val pagosAdapter = PagosAdapter(this) // Creamos el adaptador

        viewPager.adapter = pagosAdapter // ¡ESTA LÍNEA ES VITAL PARA QUE SE MUESTRE!
        viewPager.isUserInputEnabled = false // Desactiva deslizamiento para priorizar el scroll del Recycler

        // 3. Botones de navegación
        val casa = findViewById<ImageButton>(R.id.casa)
        val salida = findViewById<ImageButton>(R.id.salida)

        salida?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        casa?.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
    }
}