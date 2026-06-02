package com.example.proyect.nucky_banck.presentation.transfer

import androidx.lifecycle.ViewModel
import com.example.proyect.nucky_banck.data.repository.FirebaseAuthRepositoryImpl
import com.example.proyect.nucky_banck.domain.model.Transfer
import com.example.proyect.nucky_banck.domain.usecase.TransferUseCase
import com.example.proyect.nucky_banck.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Maneja la lógica de la pantalla de transferencia.
// Sigue el mismo patrón del LoginViewModel y RegisterViewModel.
class TransferViewModel(private val transferUseCase: TransferUseCase = TransferUseCase(FirebaseAuthRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(Transfer())
    val uiState: StateFlow<Transfer> = _uiState.asStateFlow()

    // Actualiza la cédula destino cuando el usuario escribe
    fun onCedulaDestinoChange(value: String) {
        _uiState.update {
            it.copy(
                cedulaDestino = value,
                cedulaDestinoError = null,
                generalError = null,
                transferSuccess = false,
                successMessage = ""
            )
        }
    }

    // Actualiza el monto cuando el usuario escribe
    fun onMontoChange(value: String) {
        _uiState.update {
            it.copy(
                monto = value,
                montoError = null,
                generalError = null,
                transferSuccess = false,
                successMessage = ""
            )
        }
    }

    // Se llama cuando el usuario presiona el botón "Transferir"
    fun onTransferirClicked(cedulaOrigen: String) {

        if (validarCampos()) {
            realizarTransferencia(cedulaOrigen)
        }
    }

    // Valida que los campos no estén vacíos y sean correctos
    private fun validarCampos(): Boolean {

        val state = _uiState.value

        val cedulaDestinoError = when {
            state.cedulaDestino.isBlank() -> "Ingresa la cédula del destinatario"
            !state.cedulaDestino.all { it.isDigit() } -> "La cédula solo debe contener números"
            else -> null
        }

        val montoError = when {
            state.monto.isBlank() -> "Ingresa el monto a transferir"
            state.monto.toDoubleOrNull() == null -> "El monto debe ser un número válido"
            state.monto.toDouble() <= 0 -> "El monto debe ser mayor a cero"
            else -> null
        }

        _uiState.update {
            it.copy(
                cedulaDestinoError = cedulaDestinoError,
                montoError = montoError
            )
        }

        // Solo sigue si no hay errores
        return cedulaDestinoError == null && montoError == null
    }

    // Llama al UseCase para ejecutar la transferencia
    private fun realizarTransferencia(cedulaOrigen: String) {

        val state = _uiState.value

        // Muestra el loading mientras procesa
        _uiState.update { it.copy(isLoading = true, generalError = null) }

        transferUseCase(
            cedulaOrigen = cedulaOrigen,
            cedulaDestino = state.cedulaDestino,
            monto = state.monto.toDouble()
        ) { exito, mensaje ->

            if (exito) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        transferSuccess = true,
                        successMessage = mensaje
                    )
                }
            } else {
                // Hubo un error
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = mensaje
                    )
                }
            }
        }
    }
}
