package com.example.proyect.nucky_banck.presentation.historial

import androidx.lifecycle.ViewModel
import com.example.proyect.nucky_banck.data.repository.FirebaseAuthRepositoryImpl
import com.example.proyect.nucky_banck.domain.model.Movimiento
import com.example.proyect.nucky_banck.domain.usecase.MovementsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovementsViewModel(private val historialUseCase: MovementsUseCase = MovementsUseCase(FirebaseAuthRepositoryImpl())
) : ViewModel() {

    private val _movimientos = MutableStateFlow<List<Movimiento>>(emptyList())
    val movimientos: StateFlow<List<Movimiento>> = _movimientos.asStateFlow()

    fun cargarHistorial(cedula: String) {
        historialUseCase(cedula) { lista ->
            _movimientos.value = lista
        }
    }
}
