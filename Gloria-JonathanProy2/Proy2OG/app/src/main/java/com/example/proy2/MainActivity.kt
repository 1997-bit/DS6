package com.example.proy2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvNombre: TextView
    private lateinit var tvCarrera: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvNombre = findViewById(R.id.tvNombre)
        tvCarrera = findViewById(R.id.tvCarrera)

        val btnConfigurar = findViewById<Button>(R.id.btnConfigurar)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnHistorial = findViewById<Button>(R.id.btnHistorial)
        val btnSalir = findViewById<Button>(R.id.btnSalir)

        btnConfigurar.setOnClickListener {
            startActivity(Intent(this, DatosEstudiantes::class.java))
        }

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistroCalificaciones::class.java))
        }

        btnHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialCal::class.java))
        }

        btnSalir.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val prefs = getSharedPreferences("datos_estudiantes", MODE_PRIVATE)

        tvNombre.text =
            prefs.getString("nombre", "No configurado")

        tvCarrera.text =
            prefs.getString("carrera", "No configurada")
    }
}