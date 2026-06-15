package com.example.conversionmoneda

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.conversionmoneda.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sonidoConvertir: MediaPlayer
    private lateinit var sonidoLimpiar: MediaPlayer
    private val monedas = arrayOf("USD", "EUR", "PAB", "COP", "CRC", "MXN")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sonidoConvertir = MediaPlayer.create(this, R.raw.convertir)
        sonidoLimpiar = MediaPlayer.create(this, R.raw.eliminar)

        val spinnerOrigen = findViewById<Spinner>(R.id.selectorMonedaOrigen)
        val spinnerDestino = findViewById<Spinner>(R.id.selectorMonedaDestino)
        val campoMonto = findViewById<EditText>(R.id.campoMonto)
        val etiquetaResultado = findViewById<TextView>(R.id.etiquetaResultado)
        val botonConvertir = findViewById<Button>(R.id.botonConvertir)
        val botonLimpiar = findViewById<Button>(R.id.botonLimpiar)
        val botonIntercambiar = findViewById<Button>(R.id.botonIntercambiar)
        val seekBarTexto = findViewById<SeekBar>(R.id.seekBarTexto)

        val tasasCambio = mapOf(
            "USD" to 1.0,
            "EUR" to 0.857,
            "PAB" to 1.0,
            "COP" to 3842.5,
            "CRC" to 512.3,
            "MXN" to 16.84
        )


        botonConvertir.setOnClickListener {
            val monto = campoMonto.text.toString().toDoubleOrNull()
            if (monto == null || monto <= 0) {
                Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val monedaOrigen = spinnerOrigen.selectedItem.toString()
            val monedaDestino = spinnerDestino.selectedItem.toString()
            if (monedaOrigen == monedaDestino) {
                etiquetaResultado.text = "⚠ Seleccione monedas diferentes"
                etiquetaResultado.setTextColor(
                    android.graphics.Color.parseColor("#FF0000")
                )

                return@setOnClickListener
            }
            val resultado = monto / tasasCambio[monedaOrigen]!! * tasasCambio[monedaDestino]!!
            etiquetaResultado.text = "%.2f %s".format(resultado, monedaDestino)
            etiquetaResultado.setTextColor(
                android.graphics.Color.parseColor("#06D6A0")
            )
            sonidoConvertir.start()
        }

        botonLimpiar.setOnClickListener {
            campoMonto.text.clear()
            etiquetaResultado.text = "—"
            spinnerOrigen.setSelection(0)
            spinnerDestino.setSelection(0)
            sonidoLimpiar.start()
        }

        botonIntercambiar.setOnClickListener {
            val temporal = spinnerOrigen.selectedItemPosition
            spinnerOrigen.setSelection(spinnerDestino.selectedItemPosition)
            spinnerDestino.setSelection(temporal)
        }

        seekBarTexto.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {

                val tamaño = if (progress < 12) 12 else progress

                etiquetaResultado.textSize = tamaño.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //MODO CLARO DEFAULT
        binding.pantallaPrincipal.setBackgroundColor(android.graphics.Color.parseColor("#B0BBF8"))
        binding.layoutPrincipal.setBackgroundColor(android.graphics.Color.parseColor("#B0BBF8"))
        binding.tvTitulo.setTextColor(android.graphics.Color.parseColor("#6C63FF"))
        binding.LinearL.setBackgroundColor(android.graphics.Color.parseColor("#FFD6D6"))
        binding.Monedat.setTextColor(android.graphics.Color.parseColor("#333333"))
        binding.campoMonto.setTextColor(android.graphics.Color.parseColor("#FF8888"))
        binding.campoMonto.setHintTextColor(android.graphics.Color.parseColor("#AAAAAA"))
        binding.LinearLDestino.setBackgroundColor(android.graphics.Color.parseColor("#B7E4C7"))
        binding.tvMonedaDestino.setTextColor(android.graphics.Color.parseColor("#888888"))
        binding.etiquetaResultado.setTextColor(android.graphics.Color.parseColor("#06D6A0"))
        binding.tvModoOscuro.setTextColor(android.graphics.Color.parseColor("#888888"))
        binding.tvHistorial.setTextColor(android.graphics.Color.parseColor("#333333"))
        binding.botonConvertir.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2E7D32"))
        binding.botonConvertir.setTextColor(android.graphics.Color.parseColor("#FFFFFF"))
        binding.botonLimpiar.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#C62828"))
        binding.botonLimpiar.setTextColor(android.graphics.Color.parseColor("#FFFFFF"))
        binding.botonIntercambiar.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#6C63FF"))
        binding.selectorMonedaOrigen.adapter = crearAdaptadorSpinner( android.graphics.Color.parseColor("#FF8888"))
        binding.selectorMonedaDestino.adapter = crearAdaptadorSpinner( android.graphics.Color.parseColor("#06D6A0"))

        configurarEventos() // SE LLAMA AL FINAL DE onCreate

    } // CIERRA onCreate

    override fun onDestroy() {
        sonidoConvertir.release()
        sonidoLimpiar.release()
        super.onDestroy()
    }

    private fun crearAdaptadorSpinner(colorTexto: Int): ArrayAdapter<String> {

        val banderas = mapOf(
            "USD" to R.drawable.bandera_usd,
            "EUR" to R.drawable.bandera_eur,
            "PAB" to R.drawable.bandera_pab,
            "COP" to R.drawable.bandera_cop,
            "CRC" to R.drawable.bandera_crc,
            "MXN" to R.drawable.bandera_mxn
        )
        //Pifia a los spiners
        val adaptador = object : ArrayAdapter<String>(this, R.layout.item_spinner_moneda, monedas) {

            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val vista = layoutInflater.inflate(R.layout.item_spinner_moneda, parent, false)
                val imagen = vista.findViewById<ImageView>(R.id.imagenBandera)
                val texto = vista.findViewById<TextView>(R.id.textoMoneda)
                texto.text = monedas[position]
                texto.setTextColor(colorTexto)
                imagen.setImageResource(banderas[monedas[position]] ?: 0)
                return vista
            }

            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val vista = layoutInflater.inflate(R.layout.item_spinner_moneda, parent, false)
                val imagen = vista.findViewById<ImageView>(R.id.imagenBandera)
                val texto = vista.findViewById<TextView>(R.id.textoMoneda)
                texto.text = monedas[position]
                texto.setTextColor(colorTexto)
                imagen.setImageResource(banderas[monedas[position]] ?: 0)
                return vista
            }
        }
        return adaptador
    }

    private fun configurarEventos() {
        binding.switchModoOscuro.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                // MODO OSCURO
                binding.selectorMonedaOrigen.adapter = crearAdaptadorSpinner( android.graphics.Color.parseColor("#6C63FF"))
                binding.selectorMonedaDestino.adapter = crearAdaptadorSpinner( android.graphics.Color.parseColor("#06D6A0"))
                binding.pantallaPrincipal.setBackgroundColor(android.graphics.Color.parseColor("#0D1117"))
                binding.layoutPrincipal.setBackgroundColor(android.graphics.Color.parseColor("#0D1117"))
                binding.tvTitulo.setTextColor(android.graphics.Color.parseColor("#A78BFA"))
                binding.LinearL.setBackgroundColor(android.graphics.Color.parseColor("#1A1F2E"))
                binding.Monedat.setTextColor(android.graphics.Color.parseColor("#7C83FD"))
                binding.campoMonto.setTextColor(android.graphics.Color.parseColor("#6C63FF"))
                binding.campoMonto.setHintTextColor(android.graphics.Color.parseColor("#555555"))
                binding.LinearLDestino.setBackgroundColor(android.graphics.Color.parseColor("#1A2E1F"))
                binding.tvMonedaDestino.setTextColor(android.graphics.Color.parseColor("#4ADE80"))
                binding.etiquetaResultado.setTextColor(android.graphics.Color.parseColor("#06D6A0"))
                binding.tvModoOscuro.setTextColor(android.graphics.Color.parseColor("#7C83FD"))
                binding.tvHistorial.setTextColor(android.graphics.Color.parseColor("#A78BFA"))
                binding.botonConvertir.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#166534"))
                binding.botonConvertir.setTextColor(android.graphics.Color.parseColor("#4ADE80"))
                binding.botonLimpiar.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#7F1D1D"))
                binding.botonLimpiar.setTextColor(android.graphics.Color.parseColor("#FCA5A5"))
                binding.botonIntercambiar.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#3730A3"))

            } else {
                // MODO CLARO
                binding.selectorMonedaOrigen.adapter = crearAdaptadorSpinner( android.graphics.Color.parseColor("#FF8888"))
                binding.selectorMonedaDestino.adapter = crearAdaptadorSpinner( android.graphics.Color.parseColor("#06D6A0"))
                binding.pantallaPrincipal.setBackgroundColor(android.graphics.Color.parseColor("#B0BBF8"))
                binding.layoutPrincipal.setBackgroundColor(android.graphics.Color.parseColor("#B0BBF8"))
                binding.tvTitulo.setTextColor(android.graphics.Color.parseColor("#6C63FF"))
                binding.LinearL.setBackgroundColor(android.graphics.Color.parseColor("#FFD6D6"))
                binding.Monedat.setTextColor(android.graphics.Color.parseColor("#333333"))
                binding.campoMonto.setTextColor(android.graphics.Color.parseColor("#FF8888"))
                binding.campoMonto.setHintTextColor(android.graphics.Color.parseColor("#AAAAAA"))
                binding.LinearLDestino.setBackgroundColor(android.graphics.Color.parseColor("#B7E4C7"))
                binding.tvMonedaDestino.setTextColor(android.graphics.Color.parseColor("#888888"))
                binding.etiquetaResultado.setTextColor(android.graphics.Color.parseColor("#06D6A0"))
                binding.tvModoOscuro.setTextColor(android.graphics.Color.parseColor("#888888"))
                binding.tvHistorial.setTextColor(android.graphics.Color.parseColor("#333333"))
                binding.botonConvertir.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2E7D32"))
                binding.botonConvertir.setTextColor(android.graphics.Color.parseColor("#FFFFFF"))
                binding.botonLimpiar.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#C62828"))
                binding.botonLimpiar.setTextColor(android.graphics.Color.parseColor("#FFFFFF"))
                binding.botonIntercambiar.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#6C63FF"))
            }
        }
    }

}
/*
    TODO: El historial
    TODO: modo oscuro / claro *Listo
    TODO: faltan ImageView REQUERIDO *Listo
    TODO: SeekBar Slider REQUERIDO *Listo
    TODO: Video externo( youtube)
    TODO: SplashScreen *Listo
    TODO: Le falta PIFIA *Listo/2
 */