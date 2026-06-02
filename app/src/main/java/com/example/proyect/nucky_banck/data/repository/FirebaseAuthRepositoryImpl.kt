package com.example.proyect.nucky_banck.data.repository

import android.net.Uri
import com.example.proyect.nucky_banck.data.datasource.FirebaseUserDataSource
import com.example.proyect.nucky_banck.domain.model.Movimiento
import com.example.proyect.nucky_banck.domain.model.User
import com.example.proyect.nucky_banck.domain.repository.AuthRepository
import com.example.proyect.nucky_banck.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Es el intermediario entre los UseCases y Firebase (FirebaseUserDataSource).
// Implementa todos los métodos definidos en AuthRepository.
class FirebaseAuthRepositoryImpl(private val dataSource: FirebaseUserDataSource = FirebaseUserDataSource()
) : AuthRepository {

    override fun login(cedula: String, password: String, onResult: (Boolean, Int) -> Unit) {
        dataSource.getUser(cedula)
            .addOnSuccessListener { dataUser ->
                if (!dataUser.exists()) {
                    onResult(false, R.string.error_login_failed)
                    return@addOnSuccessListener
                }
                val dbPassword = dataUser.child("password").value.toString()

                if (dbPassword == password) {
                    onResult(true, 0)
                } else {
                    onResult(false, R.string.error_login_failed)
                }

            }.addOnFailureListener {
                onResult(false, R.string.error_login_failed)
            }
    }


    override fun register(user: User, onResult: (Boolean, Int) -> Unit) {
        dataSource.getUser(user.cedula)
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {
                    onResult(false, R.string.error_user_exists)
                    return@addOnSuccessListener
                }

                val userData = mapOf(
                    "nombre" to user.fullName,
                    "cedula" to user.cedula,
                    "password" to user.password,
                    "saldo" to "100000.0")

                dataSource.saveUser(user.cedula, userData)
                    .addOnSuccessListener {
                        onResult(true, R.string.register_success_message)
                    }
                    .addOnFailureListener {
                        onResult(false, R.string.error_register_failed)
                    }

            }.addOnFailureListener {
                onResult(false, R.string.error_register_failed)
            }
    }


    override fun getUser(cedula: String, onResult: (User?) -> Unit) {
        dataSource.getUser(cedula)
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    onResult(null)
                    return@addOnSuccessListener
                }
                val saldoGuardado = snapshot.child("saldo").value
                val saldo = saldoGuardado?.toString()?.toDoubleOrNull() ?: 100000.0

                val user = User(
                    fullName = snapshot.child("nombre").value.toString(),
                    cedula = snapshot.child("cedula").value.toString(),
                    password = snapshot.child("password").value.toString(),
                    saldo = saldo,
                    photoUrl = snapshot.child("photoUrl").value?.toString() ?: ""
                )

                onResult(user)

            }.addOnFailureListener {
                onResult(null)
            }
    }

    override fun transferir(cedulaOrigen: String, cedulaDestino: String, monto: Double, onResult: (Boolean, String) -> Unit) {
        if (cedulaOrigen == cedulaDestino) {
            onResult(false, "No puedes transferirte a ti mismo")
            return
        }
        dataSource.getUser(cedulaDestino)
            .addOnSuccessListener { snapshotDestino ->
                if (!snapshotDestino.exists()) {
                    onResult(false, "El usuario destino no existe")
                    return@addOnSuccessListener
                }

                val nombreDestino = snapshotDestino.child("nombre").value.toString()
                val saldoDestino = snapshotDestino.child("saldo").value?.toString()?.toDoubleOrNull() ?: 0.0

                dataSource.getUser(cedulaOrigen)
                    .addOnSuccessListener { snapshotOrigen ->

                        val nombreOrigen = snapshotOrigen.child("nombre").value.toString()
                        val saldoActual = snapshotOrigen.child("saldo").value?.toString()?.toDoubleOrNull() ?: 100000.0

                        if (monto > saldoActual) {
                            onResult(false, "Saldo insuficiente. Tu saldo es: ${"$%,.2f".format(saldoActual)}")
                            return@addOnSuccessListener
                        }

                        val nuevoSaldoOrigen = saldoActual - monto
                        val nuevoSaldoDestino = saldoDestino + monto

                        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

                        dataSource.actualizarSaldo(cedulaOrigen, nuevoSaldoOrigen)
                            .addOnSuccessListener {
                                dataSource.actualizarSaldo(cedulaDestino, nuevoSaldoDestino)
                                    .addOnSuccessListener {

                                        val movOrigen = mapOf(
                                            "tipo" to "enviado",
                                            "nombre" to nombreDestino,
                                            "cedula" to cedulaDestino,
                                            "monto" to monto.toString(),
                                            "fecha" to fecha
                                        )

                                        val movDestino = mapOf(
                                            "tipo" to "recibido",
                                            "nombre" to nombreOrigen,
                                            "cedula" to cedulaOrigen,
                                            "monto" to monto.toString(),
                                            "fecha" to fecha
                                        )

                                        dataSource.guardarMovimiento(cedulaOrigen, movOrigen)
                                        dataSource.guardarMovimiento(cedulaDestino, movDestino)

                                        onResult(true, "Transferencia exitosa. Nuevo saldo: ${"$%,.2f".format(nuevoSaldoOrigen)}")

                                    }.addOnFailureListener {
                                        onResult(false, "Error al actualizar el saldo de destino")
                                    }
                            }.addOnFailureListener {
                                onResult(false, "Error al actualizar tu saldo")
                            }

                    }.addOnFailureListener {
                        onResult(false, "Error al obtener tu saldo")
                    }

            }.addOnFailureListener {
                onResult(false, "Error al verificar el usuario de destino")
            }
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun getMovimientos(cedula: String, onResult: (List<Movimiento>) -> Unit) {
        dataSource.getMovimientos(cedula)
            .addOnSuccessListener { snapshot ->

                val lista = mutableListOf<Movimiento>()

                for (item in snapshot.children) {
                    val mov = Movimiento(
                        tipo = item.child("tipo").value.toString(),
                        nombre = item.child("nombre").value.toString(),
                        cedula = item.child("cedula").value.toString(),
                        monto = item.child("monto").value?.toString()?.toDoubleOrNull() ?: 0.0,
                        fecha = item.child("fecha").value.toString()
                    )
                    lista.add(mov)
                }

                onResult(lista.reversed())

            }.addOnFailureListener {
                onResult(emptyList())
            }
    }

    override fun uploadPhoto(cedula: String, imagen: Uri, onResult: (Boolean, String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_photos/$cedula.jpg")
        storageRef.putFile(imagen)
            .addOnSuccessListener {
                storageRef.downloadUrl
                    .addOnSuccessListener { downloadUri ->
                        val photoUrl = downloadUri.toString()
                        dataSource.savePhoto(cedula, photoUrl)
                            .addOnSuccessListener {
                                onResult(true, photoUrl)
                            }
                            .addOnFailureListener {
                                onResult(false, null)
                            }
                    }
            }

            .addOnFailureListener {
                onResult(false, null)
            }
    }
}
