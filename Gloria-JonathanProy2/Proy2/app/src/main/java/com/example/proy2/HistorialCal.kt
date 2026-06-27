package com.example.proy2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.proy2.databinding.ActivityHistorialCalBinding

class HistorialCal : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialCalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistorialCalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        consultarHistorial()

        binding.btnRegresarMenuHistorial.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun consultarHistorial() {

        val admin = AdministradorBD(this)
        val db = admin.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT asignatura, nota1, nota2, nota3, nota4,
                   promedio, condicion, fecha
            FROM Calificaciones
            ORDER BY id DESC
            """.trimIndent(),
            null
        )
        if (cursor.count == 0) {
            binding.tvSinHistorial.visibility = View.VISIBLE
            binding.tvHistorial.text = ""
        } else {
            binding.tvSinHistorial.visibility = View.GONE

            val sb = StringBuilder()

            while (cursor.moveToNext()) {
                sb.append("Asignatura: ${cursor.getString(0)}\n")
                sb.append("Nota 1: ${cursor.getDouble(1)}\n")
                sb.append("Nota 2: ${cursor.getDouble(2)}\n")
                sb.append("Nota 3: ${cursor.getDouble(3)}\n")
                sb.append("Nota 4: ${cursor.getDouble(4)}\n")
                sb.append("Promedio: ${"%.2f".format(cursor.getDouble(5))}\n")
                sb.append("Condición: ${cursor.getString(6)}\n")
                sb.append("Fecha: ${cursor.getString(7)}\n")
                sb.append("-----------------------------\n\n")
            }

            binding.tvHistorial.text = sb.toString()
        }

        cursor.close()
        db.close()
    }
}