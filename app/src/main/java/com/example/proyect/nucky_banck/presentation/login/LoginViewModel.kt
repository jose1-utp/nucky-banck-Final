package com.example.proyect.nucky_banck.presentation.login

import androidx.lifecycle.ViewModel
import com.example.proyect.nucky_banck.data.repository.FirebaseAuthRepositoryImpl
import com.example.proyect.nucky_banck.domain.usecase.LoginUseCase
import com.example.proyect.nucky_banck.R
import com.example.proyect.nucky_banck.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
class LoginViewModel(private val loginUseCase: LoginUseCase = LoginUseCase(FirebaseAuthRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(User())
    val uiState: StateFlow<User> = _uiState.asStateFlow()

    fun onCedulaChange(cedula: String) {
        _uiState.update {
            it.copy(cedula = cedula)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun onLoginClicked(
        onSuccess: () -> Unit,
        onError: (Int) -> Unit
    ) {

        loginUseCase(
            _uiState.value.cedula,
            _uiState.value.password
        ) { success, message ->

            if (success) {

                onSuccess()

            } else {

                onError(message)
            }
        }
    }
}