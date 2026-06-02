package com.example.proyect.nucky_banck.domain.model

data class Movimiento(
    val tipo: String = "",
    val nombre: String = "",
    val cedula: String = "",
    val monto: Double = 0.0,
    val fecha: String = ""
)
