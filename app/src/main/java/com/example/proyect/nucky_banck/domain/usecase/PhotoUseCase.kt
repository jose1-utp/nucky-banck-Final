package com.example.proyect.nucky_banck.domain.usecase

import android.net.Uri
import com.example.proyect.nucky_banck.domain.repository.AuthRepository

class PhotoUseCase(private val repository: AuthRepository) {
    operator fun invoke(cedula: String, imagen: Uri, onResult: (Boolean, String?) -> Unit) {
        repository.uploadPhoto(cedula, imagen, onResult)
    }
}