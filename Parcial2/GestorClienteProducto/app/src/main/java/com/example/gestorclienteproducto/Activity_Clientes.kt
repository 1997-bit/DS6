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

class Activity_Clientes : AppCompatActivity() {

    private lateinit var db: ActivityBD
    private lateinit var adapter: itemCliente
    private var listaCompleta: List<Map<String, String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        db = ActivityBD(this)

        val btnRegresar = findViewById<LinearLayout>(R.id.btnRegresar) //retroceder en la app
        val btnAgregar = findViewById<LinearLayout>(R.id.btnAgregar) //agregar Cliente
        val listClientes = findViewById<ListView>(R.id.listClientes) //aun sin uso
        val etBuscar = findViewById<EditText>(R.id.etBuscar) //Buscar Cliente

        btnRegresar.setOnClickListener { finish() }

        btnAgregar.setOnClickListener {
            val intent = Intent(this, Activity_form_Cliente::class.java)
            startActivity(intent)
        }

        // Búsqueda en tiempo real
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarClientes(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        cargarClientes()
    }
        //select para los clientes
    private fun cargarClientes() {
        listaCompleta = db.obtenerClientes()
        mostrarClientes(listaCompleta)
    }

    private fun filtrarClientes(texto: String) {
        val filtrada = if (texto.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter {
                it["nombre"]!!.contains(texto, ignoreCase = true) ||
                        it["correo"]!!.contains(texto, ignoreCase = true)
            }
        }
        mostrarClientes(filtrada)
    }

    private fun mostrarClientes(lista: List<Map<String, String>>) {
        val listView = findViewById<ListView>(R.id.listClientes)
        adapter = itemCliente(
            context = this,
            lista = lista,
            onEditar = { cliente ->
                val intent = Intent(this, Activity_form_Cliente::class.java)
                intent.putExtra("idCliente", cliente["idCliente"]!!.toInt())
                intent.putExtra("nombre", cliente["nombre"])
                intent.putExtra("correo", cliente["correo"])
                intent.putExtra("telefono", cliente["telefono"])
                startActivity(intent)
            },
            onEliminar = { cliente ->
                AlertDialog.Builder(this)
                    .setTitle("Eliminar cliente")
                    .setMessage("¿Estás seguro de eliminar a ${cliente["nombre"]}?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        db.eliminarCliente(cliente["idCliente"]!!.toInt())
                        Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show()
                        cargarClientes()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )
        listView.adapter = adapter
    }
}