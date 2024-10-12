package com.example.pruebaapp.models

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

class Cliente(
    val id: String? = null,
    val nombre: String? = null,
    val apellido: String? = null,
    val rut: String? = null,
    val correo: String? = null,
    val telefono: String? = null,
    val contrasena: String? = null,
) {
    fun toJson() = klaxon.toJsonString(this)

    companion object {
        fun fromJson(json: String) = klaxon.parse<Cliente>(json)
    }
}