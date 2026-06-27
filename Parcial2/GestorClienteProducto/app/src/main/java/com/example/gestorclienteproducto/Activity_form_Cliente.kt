package com.example.gestorclienteproducto

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Activity_form_Cliente : AppCompatActivity() {
    private lateinit var db: ActivityBD
    private var clienteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cliente)

        db = ActivityBD(this)

        val btnRegresar = findViewById<LinearLayout>(R.id.btnRegresar)
        val btnGuardar = findViewById<LinearLayout>(R.id.btnGuardar)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)

        // Si viene con datos es modo edición
        clienteId = intent.getIntExtra("idCliente", -1)
        if (clienteId != -1) {
            etNombre.setText(intent.getStringExtra("nombre"))
            etCorreo.setText(intent.getStringExtra("correo"))
            etTelefono.setText(intent.getStringExtra("telefono"))
        }

        btnRegresar.setOnClickListener {
            finish()
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()

            // Validaciones
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (correo.isEmpty()) {
                Toast.makeText(this, "El correo es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (telefono.isEmpty()) {
                Toast.makeText(this, "El teléfono es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (clienteId == -1) {
                // Modo agregar
                val resultado = db.insertarCliente(nombre, correo, telefono)
                if (resultado > 0) {
                    Toast.makeText(this, "Cliente guardado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar el cliente", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Modo editar
                val resultado = db.actualizarCliente(clienteId, nombre, correo, telefono)
                if (resultado > 0) {
                    Toast.makeText(this, "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar el cliente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
