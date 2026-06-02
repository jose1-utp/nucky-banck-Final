package com.example.proyect.nucky_banck.domain.repository

import com.example.proyect.nucky_banck.domain.model.Movimiento
import com.example.proyect.nucky_banck.domain.model.User

interface AuthRepository {

    fun login(cedula: String, password: String, onResult: (Boolean, Int) -> Unit)

    fun register(user: User, onResult: (Boolean, Int) -> Unit)

    fun getUser(cedula: String, onResult: (User?) -> Unit)

    fun transferir(
        cedulaOrigen: String,
        cedulaDestino: String,
        monto: Double,
        onResult: (Boolean, String) -> Unit
    )

    fun getMovimientos(cedula: String, onResult: (List<Movimiento>) -> Unit)
}
