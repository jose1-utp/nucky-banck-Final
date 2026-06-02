package com.example.proyect.nucky_banck.domain.usecase

import com.example.proyect.nucky_banck.domain.repository.AuthRepository
import com.example.proyect.nucky_banck.domain.model.User

class HomeUseCase(private val repository: AuthRepository) {
    operator fun invoke(cedula: String, onResult: (User?) -> Unit) {
        repository.getUser(cedula, onResult)
    }
}