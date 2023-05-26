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
    val trabajadorId: Long
){
    val frenadoDosDecimales get() = String.format("%.2f", frenado)

    val contaminacionDosDecimales get() = String.format("%.2f", contaminacion)

    val intervalo get() = "$fechaInicio-$fechaFinal"

    val vehiculoTipo get() = "${vehiculo.tipoVehiculo}"
}

enum class IsApto(val string: String){
    APTO("apto"), NO_APTO("no apto")
}