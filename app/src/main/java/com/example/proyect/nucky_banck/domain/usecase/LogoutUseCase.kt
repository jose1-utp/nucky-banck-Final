package com.example.proyect.nucky_banck.domain.usecase

import com.example.proyect.nucky_banck.domain.repository.AuthRepository

class LogoutUseCase(private  val repository: AuthRepository) {
    operator fun invoke() {
        repository.logout()
    }
}