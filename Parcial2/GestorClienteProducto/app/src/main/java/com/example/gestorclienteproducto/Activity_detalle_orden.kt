package com.example.gestorclienteproducto

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Activity_detalle_orden : AppCompatActivity() {

    private lateinit var db: ActivityBD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_orden)

        db = ActivityBD(this)

        val btnRegresar = findViewById<LinearLayout>(R.id.btnRegresar)
        val tvNumOrden = findViewById<TextView>(R.id.tvDetalleNumOrden)
        val tvCliente = findViewById<TextView>(R.id.tvDetalleCliente)
        val tvFecha = findViewById<TextView>(R.id.tvDetalleFecha)
        val tvTotal = findViewById<TextView>(R.id.tvDetalleTotal)
        val listDetalle = findViewById<ListView>(R.id.listDetalleProductos)

        val idOrden = intent.getIntExtra("idOrden", -1)
        val cliente = intent.getStringExtra("cliente") ?: ""
        val fecha = intent.getStringExtra("fecha") ?: ""
        val total = intent.getStringExtra("total") ?: "0.00"

        tvNumOrden.text = "Orden #$idOrden"
        tvCliente.text = cliente
        tvFecha.text = fecha
        tvTotal.text = "$${"%.2f".format(total.toDouble())}"

        btnRegresar.setOnClickListener { finish() }

        if (idOrden != -1) {
            val detalles = db.obtenerDetalleOrden(idOrden)
            val adapter = itemDetalleOrden(this, detalles)
            listDetalle.adapter = adapter
        }
    }
}