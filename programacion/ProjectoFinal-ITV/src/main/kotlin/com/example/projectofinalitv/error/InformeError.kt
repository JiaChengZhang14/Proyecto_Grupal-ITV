package com.example.projectofinalitv.error

sealed class InformeError(val message: String){
    class ExportingError(message: String): InformeError(message)
}