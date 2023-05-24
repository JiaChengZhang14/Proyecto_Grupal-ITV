package com.example.projectofinalitv.models

data class Propietario(
    val dni: String,
    val nombre: String,
    val apellidos: String,
    val telefono: String,
    val email: String
){
    companion object{
        const val PROPIETARIO_ID = ""
    }
}