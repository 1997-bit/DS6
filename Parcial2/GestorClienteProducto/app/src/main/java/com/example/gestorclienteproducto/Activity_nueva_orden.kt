package com.example.gestorclienteproducto

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class Activity_nueva_orden : AppCompatActivity() {

    private lateinit var db: ActivityBD
    private var fechaSeleccionada: String = ""
    private var clienteIdSeleccionado: Int = -1
    private val productosAgregados = mutableListOf<Map<String, String>>()
    private lateinit var adapterProductosOrden: itemProductosOrden

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_orden)

        db = ActivityBD(this)

        val btnRegresar = findViewById<LinearLayout>(R.id.btnRegresar)
        val btnGuardarOrden = findViewById<LinearLayout>(R.id.btnGuardarOrden)
        val btnAgregarProducto = findViewById<LinearLayout>(R.id.btnAgregarProducto)
        val spinnerCliente = findViewById<Spinner>(R.id.spinnerCliente)
        val tvFecha = findViewById<TextView>(R.id.tvFecha)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val layoutFecha = findViewById<LinearLayout>(R.id.layoutFecha)
        val listProductosOrden = findViewById<ListView>(R.id.listProductosOrden)

        // Cargar clientes en spinner
        val clientes = db.obtenerClientesParaSpinner()
        val nombresClientes = clientes.map { it["nombre"] ?: "" }
        val spinnerAdapter = ArrayAdapter(this, R.layout.item_spinner, nombresClientes)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner)
        spinnerCliente.adapter = spinnerAdapter

        spinnerCliente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                clienteIdSeleccionado = clientes[position]["idCliente"]!!.toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Fecha actual por defecto
        val calendar = Calendar.getInstance()
        fechaSeleccionada = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
        tvFecha.text = fechaSeleccionada

        // Fecha
        layoutFecha.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                fechaSeleccionada = "$day/${month + 1}/$year"
                tvFecha.text = fechaSeleccionada
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Adapter lista productos orden
        adapterProductosOrden = itemProductosOrden(this, productosAgregados)
        listProductosOrden.adapter = adapterProductosOrden

        btnRegresar.setOnClickListener { finish() }

        btnAgregarProducto.setOnClickListener {
            val intent = Intent(this, Activity_agregar_producto_orden::class.java)
            startActivityForResult(intent, 100)
        }

        btnGuardarOrden.setOnClickListener {
            if (clienteIdSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un cliente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (productosAgregados.isEmpty()) {
                Toast.makeText(this, "Agrega al menos un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = productosAgregados.sumOf { it["subtotal"]!!.toDouble() }
            val idOrden = db.insertarOrden(clienteIdSeleccionado, fechaSeleccionada, total)

            if (idOrden > 0) {
                productosAgregados.forEach { producto ->
                    db.insertarDetalle(
                        idOrden,
                        producto["idProducto"]!!.toInt(),
                        producto["cantidad"]!!.toInt(),
                        producto["subtotal"]!!.toDouble()
                    )
                }
                Toast.makeText(this, "Orden guardada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al guardar la orden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val producto = mapOf(
                "idProducto" to data.getStringExtra("idProducto")!!,
                "nombreProducto" to data.getStringExtra("nombreProducto")!!,
                "precio" to data.getStringExtra("precio")!!,
                "cantidad" to data.getStringExtra("cantidad")!!,
                "subtotal" to data.getStringExtra("subtotal")!!
            )
            productosAgregados.add(producto)
            adapterProductosOrden.notifyDataSetChanged()

            val total = productosAgregados.sumOf { it["subtotal"]!!.toDouble() }
            findViewById<TextView>(R.id.tvTotal).text = "$${"%.2f".format(total)}"
        }
    }
}