package com.example.proyect.nucky_banck.domain.usecase

import com.example.proyect.nucky_banck.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    //
    operator fun invoke(cedula: String, password: String, onResult: (Boolean, Int) -> Unit) {
        repository.login(cedula, password, onResult)
    }
}