package com.example.projectofinalitv.models

import java.time.LocalDateTime


data class Informe(
    val idInforme: Long = INFORME_ID,
    val fechaInicio: LocalDateTime,
    val fechaFinal: LocalDateTime,
    val favorable: IsApto = IsApto.NOT_CHOOSEN,
    val frenado: Double? = null,
    val contaminacion: Double? = null,
    val interior: IsApto = IsApto.NOT_CHOOSEN,
    val luces: IsApto = IsApto.NOT_CHOOSEN,
    val vehiculo: Vehiculo,
    val trabajadorId: Long
){
    companion object{
        const val INFORME_ID = -1L

        var contador = 0
    }



    val matricula get() = "${vehiculo.matricula}"

    val tipoVehiculo get() = "${vehiculo.tipoVehiculoText}"

    val frenadoDosDecimales get() = String.format("%.2f", frenado)

    val contaminacionDosDecimales get() = String.format("%.2f", contaminacion)
    override fun toString(): String {
        return "Informe(idInforme=$idInforme, intervalo=$fechaInicio-$fechaFinal, trabajadorId=$trabajadorId, vehiculo=$vehiculo)"
    }
}

enum class IsApto(val string: String){
    APTO("apto"), NO_APTO("no apto"), NOT_CHOOSEN("")
}