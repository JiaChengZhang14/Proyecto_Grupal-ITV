package com.example.projectofinalitv.dto

data class InformeDTO(
    val fechaInicio: String,
    val fechaFinal : String,
    val favorable: String? = null,
    val frenado: String? = null,
    val contaminacion: String? = null,
    val interior: String? = null,
    val luces: String? = null,
    val trabajador: String,
    val vehiculo: VehiculoDTO
)