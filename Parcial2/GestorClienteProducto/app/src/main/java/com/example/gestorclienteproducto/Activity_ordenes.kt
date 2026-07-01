package com.example.gestorclienteproducto

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class Activity_ordenes : AppCompatActivity() {

    private lateinit var db: ActivityBD
    private lateinit var adapter: itemOrden
    private var listaCompleta: List<Map<String, String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordenes)

        db = ActivityBD(this)

        val btnRegresar = findViewById<LinearLayout>(R.id.btnRegresar)
        val btnNuevaOrden = findViewById<LinearLayout>(R.id.btnNuevaOrden)
        val etBuscar = findViewById<EditText>(R.id.etBuscar)

        btnRegresar.setOnClickListener { finish() }

        btnNuevaOrden.setOnClickListener {
            startActivity(Intent(this, Activity_nueva_orden::class.java))
        }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarOrdenes(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        cargarOrdenes()
    }

    private fun cargarOrdenes() {
        listaCompleta = db.obtenerOrdenes()
        mostrarOrdenes(listaCompleta)
    }

    private fun filtrarOrdenes(texto: String) {
        val filtrada = if (texto.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter {
                it["cliente"]!!.contains(texto, ignoreCase = true) ||
                        it["idOrden"]!!.contains(texto, ignoreCase = true)
            }
        }
        mostrarOrdenes(filtrada)
    }

    private fun mostrarOrdenes(lista: List<Map<String, String>>) {
        val listView = findViewById<ListView>(R.id.listOrdenes)
        adapter = itemOrden(
            context = this,
            lista = lista,
            onVerDetalle = { orden ->
                val intent = Intent(this, Activity_detalle_orden::class.java)
                intent.putExtra("idOrden", orden["idOrden"]!!.toInt())
                intent.putExtra("cliente", orden["cliente"])
                intent.putExtra("fecha", orden["fecha"])
                intent.putExtra("total", orden["total"])
                startActivity(intent)
            }
        )
        listView.adapter = adapter
    }
}