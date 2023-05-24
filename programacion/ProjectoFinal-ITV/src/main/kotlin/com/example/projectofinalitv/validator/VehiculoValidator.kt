package com.example.projectofinalitv.validator

import com.example.projectofinalitv.error.VehiculoError
import com.example.projectofinalitv.models.Vehiculo
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

/**
 * función que valida que la matrícula del vehículo no está repetida
 * @author IvanRoncoCebadera
 * @param vehiculos es la lista de vehiculos completa, donde podremos validar que la matricula es única
 * @return el vehiculo validado si todo va bien, o en caso de que algo salga mal, el error que ha ocurrido
 */
fun Vehiculo.validate(vehiculos: List<Vehiculo>): Result<Vehiculo, VehiculoError>{
    logger.debug { "Se valida que no haya campos repetidos" }
    require(!vehiculos.map { it.matricula }.contains(this.matricula)){
        return Err(VehiculoError.MatriculaYaExiste(this.matricula))
    }
    return Ok(this)
}