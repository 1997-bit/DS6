package com.example.conversionpeso

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var input: EditText
    lateinit var spinnerDe: Spinner
    lateinit var spinnerA: Spinner
    lateinit var btnConvertir: Button
    lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 Vincular componentes
        input = findViewById(R.id.input)
        spinnerDe = findViewById(R.id.spinnerDe)
        spinnerA = findViewById(R.id.spinner2)
        btnConvertir = findViewById(R.id.btnConvertir)
        result = findViewById(R.id.result)

        // 🔹 Opciones del spinner
        val unidades = arrayOf("Kilogramos", "Libras", "Gramos")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, unidades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerDe.adapter = adapter
        spinnerA.adapter = adapter

        //  BOTÓN
        btnConvertir.setOnClickListener {
            convertir()
        }
    }

    // FUNCIÓN DE CONVERSIÓN
    private fun convertir() {

        val valorStr = input.text.toString()

        if (valorStr.isEmpty()) {
            result.text = "Ingresa un valor"
            return
        }

        val valor = valorStr.toDouble()

        val de = spinnerDe.selectedItem.toString()
        val a = spinnerA.selectedItem.toString()

        var enKg = 0.0
        var resultado = 0.0

        // 🔹 Convertir a KG
        when (de) {
            "Kilogramos" -> enKg = valor
            "Libras" -> enKg = valor * 0.453592
            "Gramos" -> enKg = valor / 1000
        }

        // 🔹 Convertir de KG a destino
        when (a) {
            "Kilogramos" -> resultado = enKg
            "Libras" -> resultado = enKg * 2.20462
            "Gramos" -> resultado = enKg * 1000
        }

        result.text = String.format("%.2f", resultado)
    }
}