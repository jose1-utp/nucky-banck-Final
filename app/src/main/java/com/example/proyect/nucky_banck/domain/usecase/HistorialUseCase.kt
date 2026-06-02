package com.example.proyect.nucky_banck.domain.usecase

import com.example.proyect.nucky_banck.domain.model.Movimiento
import com.example.proyect.nucky_banck.domain.repository.AuthRepository

class HistorialUseCase(
    private val repository: AuthRepository
) {

    operator fun invoke(cedula: String, onResult: (List<Movimiento>) -> Unit) {
        repository.getMovimientos(cedula, onResult)
    }
}
