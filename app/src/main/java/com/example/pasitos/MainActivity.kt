package com.example.pasitos

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pasitos.network.RetrofitClient
import com.example.pasitos.schemas.Maestro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val etUsuario = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.User)
        val etPassword = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.Password)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (usuario.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (usuario == "admin" && password == "admin") {
                val prefs = getSharedPreferences("pasitos_prefs", MODE_PRIVATE)
                prefs.edit().putInt("sucursal_id", 1).apply()
                startActivity(Intent(this, AdminActivity::class.java))
                return@setOnClickListener
            }

            RetrofitClient.instance.loginMaestro(usuario, password)
                .enqueue(object : Callback<Maestro> {
                    override fun onResponse(call: Call<Maestro>, response: Response<Maestro>) {
                        if (response.isSuccessful && response.body() != null) {
                            val maestro = response.body()!!

                            val prefs = getSharedPreferences("pasitos_prefs", MODE_PRIVATE)
                            prefs.edit().putInt("sucursal_id", maestro.sucursal).apply()

                            Toast.makeText(
                                this@MainActivity,
                                "Bienvenido ${maestro.nombre}",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@MainActivity, MaestroActivity::class.java))
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Usuario o contraseña incorrectos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    override fun onFailure(call: Call<Maestro>, t: Throwable) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error de conexión",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}