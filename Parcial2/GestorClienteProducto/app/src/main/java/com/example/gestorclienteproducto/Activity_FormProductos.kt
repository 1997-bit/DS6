package com.example.gestorclienteproducto

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Activity_FormProductos : AppCompatActivity() {

    private lateinit var db: ActivityBD
    private var productoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formproductos)

        db = ActivityBD(this)

        val btnRegresar = findViewById<LinearLayout>(R.id.btnRegresar)
        val btnGuardar = findViewById<LinearLayout>(R.id.btnGuardar)
        val etNombre = findViewById<EditText>(R.id.etNombreProducto)
        val etPrecio = findViewById<EditText>(R.id.etPrecio)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)

        // Si viene con datos es modo edición
        productoId = intent.getIntExtra("idProducto", -1)
        if (productoId != -1) {
            etNombre.setText(intent.getStringExtra("nombreProducto"))
            etPrecio.setText(intent.getStringExtra("precio"))
            etDescripcion.setText(intent.getStringExtra("descripcion"))
        }

        btnRegresar.setOnClickListener { finish() }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val precioStr = etPrecio.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()

            // Validaciones
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (precioStr.isEmpty()) {
                Toast.makeText(this, "El precio es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val precio = precioStr.toDoubleOrNull()
            if (precio == null || precio <= 0) {
                Toast.makeText(this, "El precio debe ser mayor a cero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (productoId == -1) {
                val resultado = db.insertarProducto(nombre, precio, descripcion)
                if (resultado > 0) {
                    Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar el producto", Toast.LENGTH_SHORT).show()
                }
            } else {
                val resultado = db.actualizarProducto(productoId, nombre, precio, descripcion)
                if (resultado > 0) {
                    Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}