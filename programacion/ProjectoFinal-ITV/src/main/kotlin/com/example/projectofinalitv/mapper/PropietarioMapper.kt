package com.example.projectofinalitv.mapper

import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.viewmodel.PropietarioReference
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

/**
 * función que convierte el Propietario en un PropietarioReference
 * @author IvanRoncoCebadera
 * @return el Propietario introducido, como PropietarioReference
 */
fun Propietario.toPropietarioReference(): PropietarioReference{
    logger.debug { "Se transforma un Propietario en PropietarioReference" }
    return PropietarioReference(
        dni = dni,
        nombre = nombre,
        apellidos = apellidos,
        telefono = telefono,
        email = email
    )
}

/**
 * función que convierte el PropietarioReference en un Propietario
 * @author IvanRoncoCebadera
 * @return el PropietarioReference introducido, como Propietario
 */
fun PropietarioReference.toPropietario(): Propietario{
    logger.debug { "Se transforma un PropietarioReference en Propietario" }
    return Propietario(
        dni = dni,
        nombre = nombre,
        apellidos = apellidos,
        telefono = telefono,
        email = email
    )
}