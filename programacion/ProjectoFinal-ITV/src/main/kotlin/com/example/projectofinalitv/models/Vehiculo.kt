package com.example.projectofinalitv.models

import java.time.LocalDate
import java.time.LocalDateTime

data class Vehiculo(
    val id: Long = VEHICULO_ID,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val fechaMatriculacion: LocalDate = LocalDate.now(),
    val fechaUltimaRevision: LocalDateTime? = null,
    val tipoMotor: TipoMotor,
    val tipoVehiculo: TipoVehiculo,
    val propietario: Propietario
){
    companion object{
        const val VEHICULO_ID = -1L
    }

    val tipoMotorText get() = tipoMotor.toString()

    val tipoVehiculoText get() = tipoVehiculo.toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vehiculo

        if (id != other.id) return false
        if (matricula != other.matricula) return false
        if (marca != other.marca) return false
        if (modelo != other.modelo) return false
        if (fechaMatriculacion != other.fechaMatriculacion) return false
        if (fechaUltimaRevision != other.fechaUltimaRevision) return false
        if (tipoMotor != other.tipoMotor) return false
        if (tipoVehiculo != other.tipoVehiculo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + matricula.hashCode()
        result = 31 * result + marca.hashCode()
        result = 31 * result + modelo.hashCode()
        result = 31 * result + fechaMatriculacion.hashCode()
        result = 31 * result + (fechaUltimaRevision?.hashCode() ?: 0)
        result = 31 * result + tipoMotor.hashCode()
        result = 31 * result + tipoVehiculo.hashCode()
        return result
    }


}

enum class TipoMotor(val imagePath: String){
    CUALQUIERA("images/not_found.png"), ELECTRICO("images/electric.png"), GASOLINA("images/gasoline.png"), DIESEL("images/diesel.png"), HIBRIDO("images/hybrid.png"), OTRO("images/not_found.png")
}

enum class TipoVehiculo(){
    CUALQUIERA, TURISMO, FURGONETA, CAMION, MOTOCICLETA, OTRO
}