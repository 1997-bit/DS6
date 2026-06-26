package com.example.proy2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proy2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfigurar.setOnClickListener {
            startActivity(
                Intent(this, DatosEstudiantes::class.java)
            )
        }

        binding.btnRegistrar.setOnClickListener {
            startActivity(
                Intent(this, RegistroCalificaciones::class.java)
            )
        }

        binding.btnHistorial.setOnClickListener {
            startActivity(
                Intent(this, HistorialCal::class.java)
            )
        }

        binding.btnSalir.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val admin = AdministradorBD(this)
        val db = admin.readableDatabase

        val cursor = db.rawQuery(
            "SELECT nombre, carrera FROM Estudiantes LIMIT 1",
            null
        )

        if (cursor.moveToFirst()) {
            binding.tvNombre.text = cursor.getString(0)
            binding.tvCarrera.text = cursor.getString(1)
        } else {
            binding.tvNombre.text = "No configurado"
            binding.tvCarrera.text = "No configurada"
        }

        cursor.close()
        db.close()
    }
}