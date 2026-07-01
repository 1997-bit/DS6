package com.example.gestorclienteproducto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class itemOrden(
    private val context: Context,
    private val lista: List<Map<String, String>>,
    private val onVerDetalle: (Map<String, String>) -> Unit
) : BaseAdapter() {

    override fun getCount() = lista.size
    override fun getItem(position: Int) = lista[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_orden, parent, false)

        val orden = lista[position]

        val tvNumeroOrden = view.findViewById<TextView>(R.id.tvNumeroOrden)
        val tvClienteOrden = view.findViewById<TextView>(R.id.tvClienteOrden)
        val tvFechaOrden = view.findViewById<TextView>(R.id.tvFechaOrden)
        val tvTotalOrden = view.findViewById<TextView>(R.id.tvTotalOrden)

        tvNumeroOrden.text = "Orden #${orden["idOrden"]}"
        tvClienteOrden.text = "Cliente: ${orden["cliente"]}"
        tvFechaOrden.text = orden["fecha"]
        tvTotalOrden.text = "Total: $${orden["total"]}"

        view.setOnClickListener { onVerDetalle(orden) }

        return view
    }
}