package com.example.projectofinalitv.dto

data class TrabajadorDTO(
    val idTrabajador: Long,
    val idEstacion: Long,
    val nombre: String,
    val telefono: String,
    val email: String,
    val nombreUsuario: String,
    val contraseniaUsuario: String,
    val fechaContratacion: String,
    var especialidades: List<String>,
    val idResponsable: String,
    val citas: List<InformeDTO>,
    val salario: String
)