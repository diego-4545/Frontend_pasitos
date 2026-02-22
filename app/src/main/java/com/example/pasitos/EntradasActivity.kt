package com.example.pasitos

import android.content.Intent
import android.os.Bundle
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


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