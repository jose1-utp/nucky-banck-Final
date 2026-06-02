package com.example.proyect.nucky_banck.domain.repository

import android.net.Uri
import com.example.proyect.nucky_banck.domain.model.Movimiento
import com.example.proyect.nucky_banck.domain.model.User

interface AuthRepository {

    fun login(cedula: String, password: String, onResult: (Boolean, Int) -> Unit)

    fun register(user: User, onResult: (Boolean, Int) -> Unit)

    fun getUser(cedula: String, onResult: (User?) -> Unit)

    fun transferir(cedulaOrigen: String, cedulaDestino: String, monto: Double, onResult: (Boolean, String) -> Unit)

    fun getMovimientos(cedula: String, onResult: (List<Movimiento>) -> Unit)
    fun logout()

    fun uploadPhoto(cedula: String, imagen: Uri, onResult: (Boolean, String?) -> Unit)

}
