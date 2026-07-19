package com.example.conversionmoneda

data class Conversion(
    val monedaOrigen: String,
    val monedaDestino: String,
    val montoOrigen: Double,
    val montoDestino: Double,
    val fecha: String
)