package com.example.projectofinalitv.dto

data class VehiculoDTO(
    val id: String,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val fechaMatriculacion: String,
    val fechaUltimaRevision: String,
    val tipoMotor: String,
    val tipoVehiculo: String,
    val propietario: PropietarioDTO
)