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
        val error = when {
            cedula.isBlank() -> null
            !cedula.all { it.isDigit() } -> "Solo se permiten números"
            cedula.length < 8 -> "Mínimo 8 dígitos"
            cedula.length > 10 -> "Máximo 10 dígitos"
            else -> null   // null = válido (verde)
        }
        _uiState.update { it.copy(cedula = cedula, cedulaError = error) }
    }

    fun onPasswordChange(password: String) {

        val error = when {
            password.isBlank() -> null
            password.length < 6 -> "Mínimo 6 caracteres"
            !password.any { it.isUpperCase() } -> "Debe tener mayúscula"
            !password.any { !it.isLetterOrDigit() } -> "Debe tener carácters especial"
            else -> null
        }
        _uiState.update { it.copy(password = password, passwordError = error) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {

        // Valida en tiempo real si la contraseña y la confirmación coinciden
        val error = when {
            confirmPassword.isBlank() -> null
            confirmPassword != _uiState.value.password -> "Las contraseñas no coinciden"
            else -> null
        }

        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
            )
        }
    }

    fun onRegisterClicked(onSuccess: () -> Unit, onError: (Int) -> Unit) {

        val cedula = _uiState.value.cedula
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        if (_uiState.value.fullName.isBlank()) {
            onError(R.string.error_nombre_vacio)
            return
        }

        if (cedula.isBlank() || !cedula.all { it.isDigit() } || cedula.length < 8 || cedula.length > 10) {
            onError(R.string.error_cedula_invalida)
            return
        }


        if (password.isBlank() || password.length < 6 || !password.any { it.isUpperCase() } || !password.any { !it.isLetterOrDigit() }) {
            onError(R.string.error_password_invalida)
            return
        }

        if (password != confirmPassword) {
            onError(R.string.error_passwords_no_coinciden)
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        registerUseCase(_uiState.value) { success, message ->

            _uiState.update { it.copy(isLoading = false) }

            if (success) {
                onSuccess()
            } else {
                onError(message)
            }
        }
    }
}