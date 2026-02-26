package com.example.pasitos

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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

        val btnAdmin = findViewById<Button>(R.id.btnAdmin)
        val btnMaestros = findViewById<Button>(R.id.btnMaestros)

        btnAdmin.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

        btnMaestros.setOnClickListener {
            startActivity(Intent(this, MaestroActivity::class.java))
        }
    }
}
