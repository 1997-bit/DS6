package com.example.proy2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException

class HistorialCal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_historial_cal)

        val tvHistorial =
            findViewById<TextView>(R.id.tvHistorial)

        val tvSinHistorial =
            findViewById<TextView>(R.id.tvSinHistorial)

        val btnRegresar =
            findViewById<Button>(
                    R.id.btnRegresarMenuHistorial
            )

        try {

            val contenido =
                openFileInput(
                    "historial_calificaciones.txt"
                ).bufferedReader()
                    .use { it.readText() }

            tvHistorial.text = contenido

        } catch (e: FileNotFoundException) {

            tvSinHistorial.visibility =
                android.view.View.VISIBLE

            tvHistorial.text = ""
        }

        btnRegresar.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()
        }
    }
}