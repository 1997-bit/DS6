package com.example.gestorclienteproducto

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // iniciar sonidos
        SonidoManager.inicializar(this)

        val btnClientes = findViewById<LinearLayout>(R.id.btnClientes)
        val btnProductos = findViewById<LinearLayout>(R.id.btnProductos)
        val btnOrdenes = findViewById<LinearLayout>(R.id.btnOrdenes)
        val btnSalir = findViewById<LinearLayout>(R.id.btnSalir)

        // pantalla cliente
        btnClientes.setOnClickListener {
            SonidoManager.reproducirClick()
            startActivity(Intent(this, Activity_Clientes::class.java))
        }
        // fin pantalla cliente

        // pantalla Productos
        btnProductos.setOnClickListener {
            SonidoManager.reproducirClick()
            startActivity(Intent(this, Activity_Productos::class.java))
        }
        // fin pantalla Productos

        // pantalla Ordenes
        btnOrdenes.setOnClickListener {
            SonidoManager.reproducirClick()
            startActivity(Intent(this, Activity_ordenes::class.java))
        }
        // fin pantalla Ordenes

        // pantalla salir
        btnSalir.setOnClickListener {
            SonidoManager.reproducirClick()
            finish()
        }
        // fin pantalla salir
    }

    override fun onDestroy() {
        SonidoManager.liberar()
        super.onDestroy()
    }
}