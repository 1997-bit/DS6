package com.example.conversionmoneda

import android.animation.ValueAnimator
import androidx.core.animation.doOnEnd
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conversionmoneda.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sonidoConvertir: MediaPlayer
    private lateinit var sonidoLimpiar: MediaPlayer
    private val monedas = arrayOf("USD", "EUR", "PAB", "COP", "CRC", "MXN")
    private val conversiones = mutableListOf<Conversion>()
    private lateinit var historicalAdapter: Historial
    private var precisionDecimal = 2

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
        val seekBarPrecision = findViewById<SeekBar>(R.id.seekBarPrecision)
        val tvPrecisionValue = findViewById<TextView>(R.id.tvPrecisionValue)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarConversion)

        val tasasCambio = mapOf(
            "USD" to 1.0,
            "EUR" to 0.857,
            "PAB" to 1.0,
            "COP" to 3842.5,
            "CRC" to 512.3,
            "MXN" to 16.84
        )

        // Configurar RecyclerView para historial
        binding.listaHistorial.layoutManager = LinearLayoutManager(this)
        historicalAdapter = Historial(conversiones) // conversiones ya es MutableList
        binding.listaHistorial.adapter = historicalAdapter

        // Configurar SeekBar
        seekBarPrecision.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                precisionDecimal = progress
                tvPrecisionValue.text = "$progress decimales"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        botonConvertir.setOnClickListener {
            // Validar monto
            val monto = campoMonto.text.toString().trim()
            if (monto.isEmpty()) {
                mostrarError("El campo de monto no puede estar vacío")
                return@setOnClickListener
            }

            val montoDouble = monto.toDoubleOrNull()
            if (montoDouble == null || montoDouble <= 0) {
                mostrarError("Ingresa un monto válido mayor que cero")
                return@setOnClickListener
            }

            val monedaOrigen = spinnerOrigen.selectedItem.toString()
            val monedaDestino = spinnerDestino.selectedItem.toString()

            // Validar que no sean iguales
            if (monedaOrigen == monedaDestino) {
                mostrarError("Las monedas origen y destino deben ser diferentes")
                return@setOnClickListener
            }

            // Mostrar ProgressBar y animarlo
            progressBar.visibility = android.view.View.VISIBLE
            val animator = ValueAnimator.ofInt(0, 100)
            animator.duration = 1000L // 100 * 10ms = 1000ms, ajusta según prefieras
            animator.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                progressBar.progress = value
            }
            animator.doOnEnd {
                progressBar.visibility = android.view.View.GONE

                // Realizar conversión (ya calculaste resultado anteriormente o recalcula)
                val resultado = montoDouble / tasasCambio[monedaOrigen]!! * tasasCambio[monedaDestino]!!

                val formatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.US)
                formatter.maximumFractionDigits = precisionDecimal
                formatter.minimumFractionDigits = precisionDecimal

                val formatoResultado = "${formatter.format(resultado)} $monedaDestino"
                etiquetaResultado.text = formatoResultado

                // Agregar al historial usando el método del adaptador
                val fechaActual = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date())
                val nueva = Conversion(monedaOrigen, monedaDestino, montoDouble, resultado, fechaActual)

                historicalAdapter.addConversionAtTop(nueva)
                binding.listaHistorial.scrollToPosition(0)

                // debug: confirmar tamaño real de la lista y del adaptador
                android.util.Log.d("Historial", "conversiones.size = ${conversiones.size}, adapterCount = ${binding.listaHistorial.adapter?.itemCount}")
                Toast.makeText(this, "Historial tamaño = ${conversiones.size}", Toast.LENGTH_SHORT).show()

                sonidoConvertir.start()

                com.google.android.material.snackbar.Snackbar.make(
                    binding.root,
                    "Conversión exitosa: $formatoResultado",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
            }
            animator.start()
        }

        botonLimpiar.setOnClickListener {
            android.app.AlertDialog.Builder(this)
                .setTitle("Limpiar formulario")
                .setMessage("¿Deseas limpiar todos los campos?")
                .setPositiveButton("Sí") { _, _ ->
                    campoMonto.text.clear()
                    etiquetaResultado.text = "—"
                    spinnerOrigen.setSelection(0)
                    spinnerDestino.setSelection(0)
                    progressBar.progress = 0
                    sonidoLimpiar.start()
                    Toast.makeText(this, "Formulario limpiado", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
        }

        botonIntercambiar.setOnClickListener {
            val temporal = spinnerOrigen.selectedItemPosition
            spinnerOrigen.setSelection(spinnerDestino.selectedItemPosition)
            spinnerDestino.setSelection(temporal)
            Toast.makeText(this, "Monedas intercambiadas", Toast.LENGTH_SHORT).show()
        }

        //MODO CLARO DEFAULT
        aplicarTemaClaro()
        configurarEventos()
        val videoView = findViewById<VideoView>(R.id.videoLocal)

        val uri = Uri.parse("android.resource://$packageName/${R.raw.video}")
        videoView.setVideoURI(uri)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            videoView.start()
        }
    }

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

    private fun aplicarTemaClaro() {
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
        binding.selectorMonedaOrigen.adapter = crearAdaptadorSpinner(android.graphics.Color.parseColor("#FF8888"))
        binding.selectorMonedaDestino.adapter = crearAdaptadorSpinner(android.graphics.Color.parseColor("#06D6A0"))
    }

    private fun configurarEventos() {
        binding.switchModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // MODO OSCURO
                binding.selectorMonedaOrigen.adapter = crearAdaptadorSpinner(android.graphics.Color.parseColor("#6C63FF"))
                binding.selectorMonedaDestino.adapter = crearAdaptadorSpinner(android.graphics.Color.parseColor("#06D6A0"))
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
                aplicarTemaClaro()
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Error de validación")
            .setMessage(mensaje)
            .setPositiveButton("OK", null)
            .show()
    }
}
/*
    TODO: El historial
    TODO: modo oscuro / claro *Listo
    TODO: faltan ImageView REQUERIDO *Listo
    TODO: SeekBar Slider REQUERIDO
    TODO: Video externo( youtube)
    TODO: SplashScreen
    TODO: Le falta PIFIA *Listo/2
 */