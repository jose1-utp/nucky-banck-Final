package com.example.proyect.nucky_banck.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class FirebaseUserDataSource {

    private val database = FirebaseDatabase.getInstance().getReference("usuarios")

    fun getUser(cedula: String): Task<DataSnapshot> {
        return database.child(cedula).get()
    }

    fun saveUser(cedula: String, userData: Map<String, String>): Task<Void> {
        return database.child(cedula).setValue(userData)
    }

    fun actualizarSaldo(cedula: String, nuevoSaldo: Double): Task<Void> {
        return database.child(cedula).child("saldo").setValue(nuevoSaldo)
    }

    fun guardarMovimiento(cedula: String, movimiento: Map<String, String>): Task<Void> {
        return database.child(cedula).child("movimientos").push().setValue(movimiento)
    }

    fun getMovimientos(cedula: String): Task<DataSnapshot> {
        return database.child(cedula).child("movimientos").get()
    }

    fun savePhoto(cedula: String, photoUrl: String): Task<Void> {
        return database.child(cedula).child("photoUrl").setValue(photoUrl)
    }
}
