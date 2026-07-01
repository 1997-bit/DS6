package com.example.gestorclienteproducto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

class itemProducto(
    private val context: Context,
    private val lista: List<Map<String, String>>,
    private val onEditar: (Map<String, String>) -> Unit,
    private val onEliminar: (Map<String, String>) -> Unit
) : BaseAdapter() {

    override fun getCount() = lista.size
    override fun getItem(position: Int) = lista[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_producto, parent, false)

        val producto = lista[position]

        val tvNombreProducto = view.findViewById<TextView>(R.id.tvNombreProducto)
        val tvPrecio = view.findViewById<TextView>(R.id.tvPrecio)
        val tvDescripcion = view.findViewById<TextView>(R.id.tvDescripcion)
        val btnEditar = view.findViewById<LinearLayout>(R.id.btnEditar)
        val btnEliminar = view.findViewById<LinearLayout>(R.id.btnEliminar)

        tvNombreProducto.text = producto["nombreProducto"] ?: ""
        tvPrecio.text = "$${producto["precio"] ?: "0.00"}"
        tvDescripcion.text = producto["descripcion"] ?: ""

        btnEditar.setOnClickListener { onEditar(producto) }
        btnEliminar.setOnClickListener { onEliminar(producto) }

        return view
    }
}