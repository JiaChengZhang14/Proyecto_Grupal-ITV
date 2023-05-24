package com.example.projectofinalitv.models

import java.time.LocalDateTime


data class Informe(
    val fechaInicio: LocalDateTime,
    val fechaFinal: LocalDateTime,
    val favorable: IsApto? = null,
    val frenado: Double? = null,
    val contaminacion: Double? = null,
    val interior: IsApto? = null,
    val luces: IsApto? = null,
    val vehiculo: Vehiculo,
    val trabajadorId: Long?
)

enum class IsApto {
    APTO, NO_APTO
}