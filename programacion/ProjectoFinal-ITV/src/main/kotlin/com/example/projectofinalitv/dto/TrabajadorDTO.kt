package com.example.projectofinalitv.dto

data class TrabajadorDTO(
    val idTrabajador: Long,
    val idEstacion: Long,
    val nombre: String,
    val telefono: String,
    val email: String,
    val nombreUsuario: String,
    val contraseñaUsuario: String, // El cifrado de este campo se hace en la BBDD, despues cuando se recuperé ya estará cifrado, igualmente ver si hay un password field o algo qu lo haga!!!!!
    val fechaContratacion: String,
    var especialidades: List<String>,
    val idResponsable: String,
    val citas: List<InformeDTO>,
    val salario: String
)

