package com.example.projectofinalitv.error

sealed class InformeError(val message: String){
    class FechaNoSeleccionada(message: String): InformeError(message)
    class TrabajadorNoSeleccionado(message: String): InformeError(message)
    class VehiculoNoSeleccionado(message: String): InformeError(message)
    class IntervaloNoValido(message: String): InformeError(message)
    class FrenadoNoValido(message: String): InformeError(message)
    class ContaminacionNoValida(message: String): InformeError(message)
    class ExportingError(message: String): InformeError(message)
    class NotFound(message: String): InformeError(message)
    class SameDataUpdate(message: String) : InformeError(message)
    class SobrecargaCitasDeTrabajador(message: String) : InformeError(message)
    class SobrecargaCitas(message: String) : InformeError(message)
}