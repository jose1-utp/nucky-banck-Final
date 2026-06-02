package com.example.proyect.nucky_banck.domain.usecase

import com.example.proyect.nucky_banck.domain.repository.AuthRepository

// UseCase de transferencia.
// Su único trabajo es llamar al repositorio para realizar la transferencia.
class TransferUseCase(private val repository: AuthRepository) {

    operator fun invoke(cedulaOrigen: String, cedulaDestino: String, monto: Double, onResult: (Boolean, String) -> Unit) {
        repository.transferir(cedulaOrigen = cedulaOrigen, cedulaDestino = cedulaDestino, monto = monto, onResult = onResult)
    }
}
