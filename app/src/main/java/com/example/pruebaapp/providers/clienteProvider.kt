package com.example.pruebaapp.providers

import com.example.pruebaapp.models.Cliente
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class ClienteProvider {
    val db = Firebase.firestore.collection("Cliente")

    fun create(cliente: Cliente): Task<Void> {
        return db.document(cliente.id!!).set(cliente)
    }
}