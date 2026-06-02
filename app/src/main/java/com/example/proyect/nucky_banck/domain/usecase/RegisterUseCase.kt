package com.example.proyect.nucky_banck.domain.usecase

import com.example.proyect.nucky_banck.domain.repository.AuthRepository
import com.example.proyect.nucky_banck.domain.model.User

class RegisterUseCase(private val repository: AuthRepository) {
    operator fun invoke(user: User, onResult: (Boolean, Int) -> Unit) {
        repository.register(user, onResult)
    }
}