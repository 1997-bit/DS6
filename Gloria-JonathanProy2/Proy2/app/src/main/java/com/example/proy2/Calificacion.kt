package com.example.proy2

data class Calificacion(
    val id: Int,
    val estudianteId: Int,
    val asignatura: String,
    val nota1: Double,
    val nota2: Double,
    val nota3: Double,
    val nota4: Double,
    val promedio: Double,
    val condicion: String,
    val fecha: String
)