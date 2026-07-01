package com.example.gestorclienteproducto

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Activity_agregar_producto_orden : AppCompatActivity() {

    private lateinit var db: ActivityBD
    private var productoIdSeleccionado: Int = -1
    private var precioSeleccionado: Double = 0.0
    private var productos: List<Map<String, String>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto_orden)

        db = ActivityBD(this)

        val btnRegresar = findViewById<LinearLayout>(R.id.btnRegresar)
        val btnConfirmar = findViewById<LinearLayout>(R.id.btnConfirmar)
        val spinnerProducto = findViewById<Spinner>(R.id.spinnerProducto)
        val tvPrecioUnitario = findViewById<TextView>(R.id.tvPrecioUnitario)
        val tvSubtotal = findViewById<TextView>(R.id.tvSubtotal)
        val etCantidad = findViewById<EditText>(R.id.etCantidad)

        // Cargar productos en spinner
        productos = db.obtenerProductosParaSpinner()
        val nombresProductos = productos.map { "${it["nombreProducto"]} - $${it["precio"]}" }
        val spinnerAdapter = ArrayAdapter(this, R.layout.item_spinner, nombresProductos)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner)
        spinnerProducto.adapter = spinnerAdapter

        spinnerProducto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                productoIdSeleccionado = productos[position]["idProducto"]!!.toInt()
                precioSeleccionado = productos[position]["precio"]!!.toDouble()
                tvPrecioUnitario.text = "$${"%.2f".format(precioSeleccionado)}"
                calcularSubtotal(etCantidad.text.toString(), tvSubtotal)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Calcular subtotal en tiempo real
        etCantidad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calcularSubtotal(s.toString(), tvSubtotal)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnRegresar.setOnClickListener { finish() }

        btnConfirmar.setOnClickListener {
            if (productoIdSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidadStr = etCantidad.text.toString().trim()
            if (cantidadStr.isEmpty()) {
                Toast.makeText(this, "Ingresa la cantidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadStr.toIntOrNull()
            if (cantidad == null || cantidad <= 0) {
                Toast.makeText(this, "La cantidad debe ser mayor a cero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val subtotal = precioSeleccionado * cantidad
            val productoSeleccionado = productos.find { it["idProducto"]!!.toInt() == productoIdSeleccionado }

            val intent = Intent()
            intent.putExtra("idProducto", productoIdSeleccionado.toString())
            intent.putExtra("nombreProducto", productoSeleccionado?.get("nombreProducto") ?: "")
            intent.putExtra("precio", precioSeleccionado.toString())
            intent.putExtra("cantidad", cantidad.toString())
            intent.putExtra("subtotal", subtotal.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun calcularSubtotal(cantidadStr: String, tvSubtotal: TextView) {
        val cantidad = cantidadStr.toIntOrNull() ?: 0
        val subtotal = precioSeleccionado * cantidad
        tvSubtotal.text = "$${"%.2f".format(subtotal)}"
    }
}