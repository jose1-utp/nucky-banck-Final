package com.example.proyect.nucky_banck.presentation.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.proyect.nucky_banck.data.repository.FirebaseAuthRepositoryImpl
import com.example.proyect.nucky_banck.domain.model.Movimiento
import com.example.proyect.nucky_banck.domain.model.User
import com.example.proyect.nucky_banck.domain.usecase.MovementsUseCase
import com.example.proyect.nucky_banck.domain.usecase.HomeUseCase
import com.example.proyect.nucky_banck.domain.usecase.LogoutUseCase
import com.example.proyect.nucky_banck.domain.usecase.PhotoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val homeUseCase: HomeUseCase = HomeUseCase(FirebaseAuthRepositoryImpl()),
    private val photoUseCase: PhotoUseCase = PhotoUseCase(FirebaseAuthRepositoryImpl()),
    private val logoutUseCase: LogoutUseCase = LogoutUseCase(FirebaseAuthRepositoryImpl()),
    private val historialUseCase: MovementsUseCase = MovementsUseCase(FirebaseAuthRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(User())
    val uiState: StateFlow<User> = _uiState.asStateFlow()

    private val _movimientos = MutableStateFlow<List<Movimiento>>(emptyList())
    val movimientos: StateFlow<List<Movimiento>> = _movimientos.asStateFlow()

    private val localPhotoUri = MutableStateFlow<Uri?>(null)
    val localPhoto: StateFlow<Uri?> = localPhotoUri.asStateFlow()
    fun uploadPhoto(cedula: String, uri: Uri) {
        localPhotoUri.value = uri
        photoUseCase(cedula, uri) { success, url ->
            if (success && url != null) {
                _uiState.update {
                    it.copy(photoUrl = url)
                }
            }
        }
    }

    fun loadUserData(cedula: String) {
        homeUseCase(cedula) { user ->
            user?.let {
                _uiState.update { state ->
                    state.copy(
                        fullName = user.fullName,
                        cedula   = user.cedula,
                        saldo    = user.saldo
                    )
                }
            }
        }
    }
    fun logout() {
        logoutUseCase()
    }
    fun cargarMovimientos(cedula: String) {
        historialUseCase(cedula) { lista ->
            _movimientos.value = lista
        }
    }
}
