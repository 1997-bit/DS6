package com.example.gestorclienteproducto

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Activity_Productos : AppCompatActivity() {

    private lateinit var db: ActivityBD
    private lateinit var adapter: itemProducto
    private var listaCompleta: List<Map<String, String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        db = ActivityBD(this)

        val btnRegresar = findViewById<LinearLayout>(R.id.btnRegresar)
        val btnAgregar = findViewById<LinearLayout>(R.id.btnAgregar)
        val etBuscar = findViewById<EditText>(R.id.etBuscar)

        btnRegresar.setOnClickListener { finish() }

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, Activity_FormProductos::class.java))
        }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarProductos(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        listaCompleta = db.obtenerProductos()
        mostrarProductos(listaCompleta)
    }

    private fun filtrarProductos(texto: String) {
        val filtrada = if (texto.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter {
                it["nombreProducto"]!!.contains(texto, ignoreCase = true)
            }
        }
        mostrarProductos(filtrada)
    }

    private fun mostrarProductos(lista: List<Map<String, String>>) {
        val listView = findViewById<ListView>(R.id.listProductos)
        adapter = itemProducto(
            context = this,
            lista = lista,
            onEditar = { producto ->
                val intent = Intent(this, Activity_FormProductos::class.java)
                intent.putExtra("idProducto", producto["idProducto"]!!.toInt())
                intent.putExtra("nombreProducto", producto["nombreProducto"])
                intent.putExtra("precio", producto["precio"])
                intent.putExtra("descripcion", producto["descripcion"])
                startActivity(intent)
            },
            onEliminar = { producto ->
                AlertDialog.Builder(this)
                    .setTitle("Eliminar producto")
                    .setMessage("¿Estás seguro de eliminar ${producto["nombreProducto"]}?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        db.eliminarProducto(producto["idProducto"]!!.toInt())
                        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
                        cargarProductos()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )
        listView.adapter = adapter
    }
}