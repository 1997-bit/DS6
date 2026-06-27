package com.example.proy2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegistroCalificaciones : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_registro_calificaciones
        )

        val etAsignatura =
            findViewById<EditText>(R.id.etAsignatura)

        val etNota1 =
            findViewById<EditText>(R.id.etNota1)

        val etNota2 =
            findViewById<EditText>(R.id.etNota2)

        val etNota3 =
            findViewById<EditText>(R.id.etNota3)

        val etNota4 =
            findViewById<EditText>(R.id.etNota4)

        val tvPromedio =
            findViewById<TextView>(R.id.tvPromedio)

        val tvCondicion =
            findViewById<TextView>(R.id.tvCondicion)

        val btnGuardar =
            findViewById<Button>(
                R.id.btnGuardarCalificacion
            )

        val btnRegresar =
            findViewById<Button>(
                R.id.btnRegresarMenu
            )

        btnGuardar.setOnClickListener {

            val n1 =
                etNota1.text.toString().toDoubleOrNull()

            val n2 =
                etNota2.text.toString().toDoubleOrNull()

            val n3 =
                etNota3.text.toString().toDoubleOrNull()

            val n4 =
                etNota4.text.toString().toDoubleOrNull()

            if (
                etAsignatura.text.isEmpty() ||
                n1 == null ||
                n2 == null ||
                n3 == null ||
                n4 == null
            ) {
                Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val promedio =
                (n1 + n2 + n3 + n4) / 4

            val condicion =
                when {
                    promedio >= 91 -> "Excelente"
                    promedio >= 81 -> "Bueno"
                    promedio >= 71 -> "Regular"
                    promedio >= 61 -> "Mínimo aprobado"
                    else -> "Reprobado"
                }

            tvPromedio.text =
                String.format("%.2f", promedio)

            tvCondicion.text =
                condicion

            val prefs =
                getSharedPreferences(
                    "datos_estudiantes",
                    MODE_PRIVATE
                )

            val registro =
                """
Estudiante: ${prefs.getString("nombre","")}
Carrera: ${prefs.getString("carrera","")}
Grupo: ${prefs.getString("grupo","")}
Asignatura: ${etAsignatura.text}
Nota1: $n1
Nota2: $n2
Nota3: $n3
Nota4: $n4
Promedio: $promedio
Condición: $condicion
-----------------------------------
""".trimIndent()

            openFileOutput(
                "historial_calificaciones.txt",
                MODE_APPEND
            ).use {
                it.write(
                    (registro + "\n")
                        .toByteArray()
                )
            }

            Toast.makeText(
                this,
                "Calificación guardada",
                Toast.LENGTH_SHORT
            ).show()
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