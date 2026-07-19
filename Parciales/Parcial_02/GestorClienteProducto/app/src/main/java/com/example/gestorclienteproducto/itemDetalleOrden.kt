package com.example.gestorclienteproducto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class itemDetalleOrden(
    private val context: Context,
    private val lista: List<Map<String, String>>
) : BaseAdapter() {

    override fun getCount() = lista.size
    override fun getItem(position: Int) = lista[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_detalle_orden, parent, false)

        val detalle = lista[position]

        val tvNombre = view.findViewById<TextView>(R.id.tvDetalleNombreProducto)
        val tvCantidad = view.findViewById<TextView>(R.id.tvDetalleCantidad)
        val tvPrecio = view.findViewById<TextView>(R.id.tvDetallePrecio)
        val tvSubtotal = view.findViewById<TextView>(R.id.tvDetalleSubtotal)

        tvNombre.text = detalle["nombreProducto"] ?: ""
        tvCantidad.text = "x${detalle["cantidad"]}"
        tvPrecio.text = "$${detalle["precio"]}"
        tvSubtotal.text = "$${"%.2f".format(detalle["subtotal"]!!.toDouble())}"

        return view
    }
}