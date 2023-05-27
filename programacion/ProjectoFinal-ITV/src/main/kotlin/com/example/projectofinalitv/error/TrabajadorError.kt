package com.example.projectofinalitv.error

sealed class TrabajadorError(val message: String){
    class ExportingError(message: String): TrabajadorError(message)
}