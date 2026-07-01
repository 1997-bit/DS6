package com.example.gestorclienteproducto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class itemProductosOrden(
    private val context: Context,
    private val lista: MutableList<Map<String, String>>
) : BaseAdapter() {

    override fun getCount() = lista.size
    override fun getItem(position: Int) = lista[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_detalle_orden, parent, false)

        val producto = lista[position]

        val tvNombre = view.findViewById<TextView>(R.id.tvDetalleNombreProducto)
        val tvCantidad = view.findViewById<TextView>(R.id.tvDetalleCantidad)
        val tvPrecio = view.findViewById<TextView>(R.id.tvDetallePrecio)
        val tvSubtotal = view.findViewById<TextView>(R.id.tvDetalleSubtotal)

        tvNombre.text = producto["nombreProducto"] ?: ""
        tvCantidad.text = "x${producto["cantidad"]}"
        tvPrecio.text = "$${producto["precio"]}"
        tvSubtotal.text = "$${"%.2f".format(producto["subtotal"]!!.toDouble())}"

        return view
    }
}