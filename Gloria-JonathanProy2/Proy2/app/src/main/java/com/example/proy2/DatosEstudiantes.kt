package com.example.proy2

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proy2.databinding.ActivityDatosEstudiantesBinding

class DatosEstudiantes : AppCompatActivity() {

    private lateinit var binding: ActivityDatosEstudiantesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDatosEstudiantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosExistentes()

        binding.btnGuardar.setOnClickListener {
            guardarEstudiante()
        }

        binding.btnLimpiar.setOnClickListener {
            limpiarCampos()
        }

        binding.btnRegresar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun cargarDatosExistentes() {

        val admin = AdministradorBD(this)
        val db = admin.readableDatabase

        val cursor = db.rawQuery(
            "SELECT nombre, carrera, grupo, notificaciones FROM Estudiantes LIMIT 1",
            null
        )

        if (cursor.moveToFirst()) {
            binding.etNombre.setText(cursor.getString(0))
            binding.etCarrera.setText(cursor.getString(1))
            binding.etGrupo.setText(cursor.getString(2))
            binding.switchNotificaciones.isChecked = cursor.getInt(3) == 1
        }

        cursor.close()
        db.close()
    }

    private fun guardarEstudiante() {

        val nombre = binding.etNombre.text.toString().trim()
        val carrera = binding.etCarrera.text.toString().trim()
        val grupo = binding.etGrupo.text.toString().trim()
        val notificaciones = if (binding.switchNotificaciones.isChecked) 1 else 0

        if (nombre.isEmpty() || carrera.isEmpty() || grupo.isEmpty()) {
            Toast.makeText(
                this,
                "Complete todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val admin = AdministradorBD(this)
        val db = admin.writableDatabase
        //hacer insert por cada estudiante
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("carrera", carrera)
            put("grupo", grupo)
            put("notificaciones", notificaciones)
        }

        val resultado = db.insert("Estudiantes", null, valores)


        db.close()

        if (resultado != -1L) {
            Toast.makeText(
                this,
                "Estudiante guardado correctamente",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "Error al guardar estudiante",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun limpiarCampos() {
        binding.etNombre.setText("")
        binding.etCarrera.setText("")
        binding.etGrupo.setText("")
        binding.switchNotificaciones.isChecked = false
        binding.etNombre.requestFocus()
    }
}