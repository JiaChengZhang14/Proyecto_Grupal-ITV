package com.example.projectofinalitv.error

import java.time.LocalDate

sealed class VehiculoError(val message: String){
    class MatriculaNoValida(message: String): VehiculoError("La matricula '$message', no es válida.")
    class MatriculaYaExiste(message: String): VehiculoError("La matricula '$message', ya existe por lo que no se puede elegir.")
    class MarcaNoValida(message: String): VehiculoError("La marca '$message', no es válida.")
    class ModeloNoValido(message: String): VehiculoError("El modelo '$message', no es válido.")
    class FechaMatriculacionNoValida(message: LocalDate): VehiculoError("La fecha de matriculación '$message', no es válida.")
    class SameDataUpdate(message: Long): VehiculoError("No has cambiado ningún dato al editar el vehículo de id: $message")
    class NotFound(message: String): VehiculoError("El vehículo de matrícula: $message, no fue encontrado")
    class PropietarioNotFound(message: String): VehiculoError("El propietario del vehículo de matrícula: $message, no fue encontrado")
}