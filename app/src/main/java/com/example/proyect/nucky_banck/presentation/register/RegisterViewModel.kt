package com.example.proyect.nucky_banck.presentation.register

import androidx.lifecycle.ViewModel
import com.example.proyect.nucky_banck.R
import com.example.proyect.nucky_banck.data.repository.FirebaseAuthRepositoryImpl
import com.example.proyect.nucky_banck.domain.model.User
import com.example.proyect.nucky_banck.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
class RegisterViewModel(private val registerUseCase: RegisterUseCase = RegisterUseCase(FirebaseAuthRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(User())
    val uiState: StateFlow<User> = _uiState.asStateFlow()

    fun onFullNameChange(fullName: String) {
        _uiState.update {
            it.copy(fullName = fullName)
        }
    }

    fun onCedulaChange(cedula: String) {
        _uiState.update {

            it.copy(
                cedula = cedula
            )
        }
    }

    fun onPasswordChange(password: String) {

        _uiState.update {

            it.copy(
                password = password
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {

        _uiState.update {

            it.copy(
                confirmPassword = confirmPassword
            )
        }
    }

    fun onRegisterClicked(onSuccess: () -> Unit, onError: (Int) -> Unit) {

        registerUseCase(_uiState.value) { success, message ->

            if (success) {
                onSuccess()
            } else {
                onError(message)
            }
        }
    }
}