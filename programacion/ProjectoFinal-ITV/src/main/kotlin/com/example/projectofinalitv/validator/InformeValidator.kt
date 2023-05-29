package com.example.projectofinalitv.validator

import com.example.projectofinalitv.error.InformeError
import com.example.projectofinalitv.models.Informe
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

fun Informe.validateInforme(informes: List<Informe>): Result<Informe, InformeError>{
    logger.debug { "Validamos que se pueda meter otra cita más en el intervalo: ${this.fechaInicio}-${this.fechaFinal}" }

    if(informes.filter { it.trabajadorId == this.trabajadorId }.filter { it.fechaInicio == this.fechaInicio }.size >= 4){
        return Err(InformeError.SobrecargaCitasDeTrabajador("El trabajador de id: ${this.trabajadorId}, no puede aceptar más citas en el intervalo ${this.fechaInicio}-${this.fechaFinal}."))
    }

    if(informes.filter { it.fechaInicio == this.fechaInicio }.size >= 8){
        return Err(InformeError.SobrecargaCitas("No se pueden acepta mas citas en el intervalo ${this.fechaInicio}-${this.fechaFinal}."))
    }

    if(informes.filter { it.vehiculo == this.vehiculo && it.fechaInicio.toLocalDate() == this.fechaInicio.toLocalDate() }.isNotEmpty()){
        return Err(InformeError.SobrecargaCitas("El vehículo de matrícula: ${this.vehiculo.matricula}, ya tiene una cita el día ${this.fechaInicio.toLocalDate()}."))
    }

    return Ok(this)
}