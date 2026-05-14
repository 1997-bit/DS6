package com.example.conversionmoneda

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sonidoConvertir: MediaPlayer
    private lateinit var sonidoLimpiar: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sonidoConvertir = MediaPlayer.create(this, R.raw.convertir)
        sonidoLimpiar = MediaPlayer.create(this, R.raw.eliminar)

        val spinnerOrigen = findViewById<Spinner>(R.id.selectorMonedaOrigen)
        val spinnerDestino = findViewById<Spinner>(R.id.selectorMonedaDestino)
        val campoMonto = findViewById<EditText>(R.id.campoMonto)
        val etiquetaResultado = findViewById<TextView>(R.id.etiquetaResultado)
        val botonConvertir = findViewById<Button>(R.id.botonConvertir)
        val botonLimpiar = findViewById<Button>(R.id.botonLimpiar)
        val botonIntercambiar = findViewById<Button>(R.id.botonIntercambiar)

        val monedas = arrayOf("USD", "EUR", "PAB", "COP", "CRC", "MXN")


        val tasasCambio = mapOf(
            "USD" to 1.0,
            "EUR" to 0.857,
            "PAB" to 1.0,
            "COP" to 3842.5,
            "CRC" to 512.3,
            "MXN" to 16.84
        )

        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, monedas)
        spinnerOrigen.adapter = adaptador
        spinnerDestino.adapter = adaptador

        botonConvertir.setOnClickListener {
            val monto = campoMonto.text.toString().toDoubleOrNull()

            if (monto == null || monto <= 0) {
                Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monedaOrigen = spinnerOrigen.selectedItem.toString()
            val monedaDestino = spinnerDestino.selectedItem.toString()

            val resultado = monto / tasasCambio[monedaOrigen]!! * tasasCambio[monedaDestino]!!
            etiquetaResultado.text = "%.2f %s".format(resultado, monedaDestino)

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
    }

    override fun onDestroy() {
        sonidoConvertir.release()
        sonidoLimpiar.release()
        super.onDestroy()
    }
}
/*
    TODO: El historial
    TODO: modo oscuro / claro
    TODO: faltan ImageView REQUERIDO
    TODO: SeekBar Slider REQUERIDO
    TODO: Video externo( youtube)
    TODO: SplashScreen
    TODO: Le falta PIFIA
 */