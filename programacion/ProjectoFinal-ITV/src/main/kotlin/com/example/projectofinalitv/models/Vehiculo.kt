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

    override fun toString(): String {
        return "Vehiculo(id=$id, matricula='$matricula', propietario=$propietario)"
    }


}

enum class TipoMotor(val imagePath: String){
    CUALQUIERA("images/not_found.png"), ELECTRICO("images/electric.png"), GASOLINA("images/gasoline.png"), DIESEL("images/diesel.png"), HIBRIDO("images/hybrid.png"), OTRO("images/not_found.png")
}

enum class TipoVehiculo(){
    CUALQUIERA, TURISMO, FURGONETA, CAMION, MOTOCICLETA, OTRO
}