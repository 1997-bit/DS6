package com.example.conversionmoneda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.conversionmoneda.databinding.ItemConversionHistoryBinding

class Historial(private val conversiones: MutableList<Conversion>) :
    RecyclerView.Adapter<Historial.ConversionViewHolder>() {

    inner class ConversionViewHolder(private val binding: ItemConversionHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(conversion: Conversion) {
            binding.tvDescrip.text = "${conversion.monedaOrigen} → ${conversion.monedaDestino}"
            binding.tvMonto.text = "%.2f → %.2f".format(conversion.montoOrigen, conversion.montoDestino)
            binding.tvFecha.text = conversion.fecha
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionViewHolder {
        val binding = ItemConversionHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ConversionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversionViewHolder, position: Int) {
        holder.bind(conversiones[position])
    }

    override fun getItemCount() = conversiones.size

    // Método para agregar una conversión al inicio y notificar correctamente
    fun addConversionAtTop(conversion: Conversion) {
        conversiones.add(0, conversion)
        notifyItemInserted(0)
    }

    // Método para limpiar historial (opcional)
    fun clearAll() {
        val size = conversiones.size
        conversiones.clear()
        if (size > 0) notifyItemRangeRemoved(0, size)
    }
}