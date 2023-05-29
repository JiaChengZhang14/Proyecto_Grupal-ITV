package com.example.projectofinalitv.error

sealed class PropietarioError(val message: String) {
    class DniNoValido(message: String): PropietarioError("El DNI '$message', no es válido.")
    class DniYaExiste(message: String): PropietarioError("El DNI '$message', ya existe por lo que no se puede elegir.")
    class NombreNoValido(message: String): PropietarioError("El nombre '$message', no es válido.")
    class ApellidoNoValido(message: String): PropietarioError("Los apellidos '$message', no son válidos.")
    class TelefonoNoValido(message: String): PropietarioError("El telefono '$message', no es válido.")
    class EmailNoValido(message: String): PropietarioError("El email '$message', no es válido.")
    class SameDataUpdate(message: String): PropietarioError("No has cambiado ningún dato al editar el propietario de dni: $message")
    class NotFound(message: String): PropietarioError("El propietario de dni: $message, no fue encontrado")
    class TodaviaExisteElVehiculo(message: String) : PropietarioError(message)
}