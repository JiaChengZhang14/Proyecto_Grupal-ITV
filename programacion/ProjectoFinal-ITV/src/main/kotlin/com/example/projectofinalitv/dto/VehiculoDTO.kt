package com.example.projectofinalitv.dto

import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.TipoMotor
import com.example.projectofinalitv.models.TipoVehiculo
import com.example.projectofinalitv.models.Vehiculo
import java.time.LocalDate
import java.time.LocalDateTime

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
