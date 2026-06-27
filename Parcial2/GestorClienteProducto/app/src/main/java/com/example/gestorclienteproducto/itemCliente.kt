package com.example.gestorclienteproducto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

class itemCliente(
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
            .inflate(R.layout.item_cliente, parent, false)

        val cliente = lista[position]

        val tvIniciales = view.findViewById<TextView>(R.id.tvIniciales)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombre)
        val tvCorreo = view.findViewById<TextView>(R.id.tvCorreo)
        val tvTelefono = view.findViewById<TextView>(R.id.tvTelefono)
        val btnEditar = view.findViewById<LinearLayout>(R.id.btnEditar)
        val btnEliminar = view.findViewById<LinearLayout>(R.id.btnEliminar)

        val nombre = cliente["nombre"] ?: ""
        tvNombre.text = nombre
        tvCorreo.text = cliente["correo"] ?: ""
        tvTelefono.text = cliente["telefono"] ?: ""

        // Iniciales del nombre
        val partes = nombre.trim().split(" ")
        tvIniciales.text = if (partes.size >= 2) {
            "${partes[0].first().uppercaseChar()}${partes[1].first().uppercaseChar()}"
        } else {
            nombre.take(2).uppercase()
        }

        btnEditar.setOnClickListener { onEditar(cliente) }
        btnEliminar.setOnClickListener { onEliminar(cliente) }

        return view
    }
}