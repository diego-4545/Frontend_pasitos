package com.example.pasitos

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EntradasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entradas)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val density = resources.displayMetrics.density
        val maxWidth = (600 * density).toInt()
        val maxHeight = (725 * density).toInt()

        val finalWidth = if (screenWidth > maxWidth) maxWidth else screenWidth
        val finalHeight = if (screenHeight > maxHeight) maxHeight else screenHeight

        window.setLayout(finalWidth, finalHeight)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewEntradas)
        val adapter = com.example.pasitos.adapters.EntradasAdapter(this)
        viewPager.adapter = adapter

        //Botones de menu superior
        val casa = findViewById<ImageButton>(R.id.casa)
        val salida = findViewById<ImageButton>(R.id.salida)

        salida.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        casa.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
    }
}