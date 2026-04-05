package com.example.pasitos

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pasitos.Fragments.HistorialFragment

class HistorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial)

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

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentHistorial, HistorialFragment())
                .commit()
        }
    }
}