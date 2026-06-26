package com.example.proy2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DatosEstudiantes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_estudiantes)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCarrera = findViewById<EditText>(R.id.etCarrera)
        val etGrupo = findViewById<EditText>(R.id.etGrupo)
        val switchNotificaciones = findViewById<Switch>(R.id.switchNotificaciones)

        val btnGuardar =
            findViewById<Button>(R.id.btnGuardar)

        val btnLimpiar =
            findViewById<Button>(R.id.btnLimpiar)

        val btnRegresar =
            findViewById<Button>(R.id.btnRegresar)

        val prefs =
            getSharedPreferences(
                "datos_estudiantes",
                MODE_PRIVATE
            )

        etNombre.setText(
            prefs.getString("nombre", "")
        )

        etCarrera.setText(
            prefs.getString("carrera", "")
        )

        etGrupo.setText(
            prefs.getString("grupo", "")
        )

        switchNotificaciones.isChecked =
            prefs.getBoolean("notificaciones", false)

        btnGuardar.setOnClickListener {

            if (
                etNombre.text.isEmpty() ||
                etCarrera.text.isEmpty() ||
                etGrupo.text.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            prefs.edit()
                .putString("nombre", etNombre.text.toString())
                .putString("carrera", etCarrera.text.toString())
                .putString("grupo", etGrupo.text.toString())
                .putBoolean(
                    "notificaciones",
                    switchNotificaciones.isChecked
                )
                .apply()

            Toast.makeText(
                this,
                "Datos guardados",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnLimpiar.setOnClickListener {
            etNombre.setText("")
            etCarrera.setText("")
            etGrupo.setText("")
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