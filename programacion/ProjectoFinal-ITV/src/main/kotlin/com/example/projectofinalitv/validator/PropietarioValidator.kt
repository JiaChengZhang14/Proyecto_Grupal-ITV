package com.example.projectofinalitv.validator

import com.example.projectofinalitv.error.PropietarioError
import com.example.projectofinalitv.models.Propietario
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

fun Propietario.validatePropietario(propietarios: List<Propietario>): Result<Propietario, PropietarioError>{
    logger.debug { "Se valida que el propietario el dni repetido" }
    require(!propietarios.map { it.dni }.contains(this.dni)){
        return Err(PropietarioError.DniYaExiste(this.dni))
    }
    return Ok(this)
}