package com.example.proyect.nucky_banck.domain.model

// Modelo principal de la aplicación.
// Representa los datos del usuario y también los estados de la UI (errores, loading, éxitos).
data class User(

    // Datos del usuario
    val cedula: String = "",
    val password: String = "",
    val fullName: String = "",
    val saldo: Double = 100000.0,   // Saldo inicial por defecto: $100,000

    // Campos del formulario de registro
    val confirmPassword: String = "",

    // Estados de carga y resultado
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,

    // Mensajes de error por campo
    val cedulaError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val photoUrl: String = ""

)
