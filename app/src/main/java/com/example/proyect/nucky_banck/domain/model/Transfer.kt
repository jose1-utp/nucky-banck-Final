package com.example.proyect.nucky_banck.domain.model

data class Transfer(
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
