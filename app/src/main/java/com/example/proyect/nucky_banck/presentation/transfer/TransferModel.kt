package com.example.proyect.nucky_banck.presentation.transfer

// Modelo de estado de la pantalla de transferencia.
// Guarda lo que el usuario escribe y el resultado de la operación.
data class TransferModel(

    // Lo que escribe el usuario
    val cedulaDestino: String = "",
    val monto: String = "",

    // Estado de carga
    val isLoading: Boolean = false,

    // Errores por campo
    val cedulaDestinoError: String? = null,
    val montoError: String? = null,
    val generalError: String? = null,

    // Resultado de la transferencia
    val transferSuccess: Boolean = false,
    val successMessage: String = ""
)
