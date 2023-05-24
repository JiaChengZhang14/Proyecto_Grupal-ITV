package com.example.projectofinalitv.dto

data class InformeDTO(
    val fechaInicio: String,
    val fechaFinal: String,
    val favorable: String? = null,
    val frenado: Double? = null,
    val contaminacion: Double? = null,
    val interior: String? = null,
    val luces: String? = null,
    val trabajador: TrabajadorDTO,
    val vehiculo: VehiculoDTO
)
