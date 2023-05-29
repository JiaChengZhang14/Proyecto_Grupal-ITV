package com.example.projectofinalitv.validator

import com.example.projectofinalitv.error.PropietarioError
import com.example.projectofinalitv.models.Propietario
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

/**
 * función que comprueba que no se este insertando un propietario con un dni ya existente
 * @author IvanRoncoCebadera
 * @param propietarios es la lista de propietarios con la que confirmaremos que no se repite el dni
 * @return en caso de que el completo de la operación salga bien, el propietario validado, en caso contrario el error ocurrido
 */
fun Propietario.validatePropietario(propietarios: List<Propietario>): Result<Propietario, PropietarioError>{
    logger.debug { "Se valida que el propietario no tenga un dni repetido" }
    require(!propietarios.map { it.dni }.contains(this.dni)){
        return Err(PropietarioError.DniYaExiste(this.dni))
    }
    return Ok(this)
}