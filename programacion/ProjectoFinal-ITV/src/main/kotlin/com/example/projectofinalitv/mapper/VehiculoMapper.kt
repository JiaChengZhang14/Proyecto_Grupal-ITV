package com.example.projectofinalitv.mapper

import com.example.projectofinalitv.dto.VehiculoDTO
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.TipoMotor
import com.example.projectofinalitv.models.TipoVehiculo
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.viewmodel.VehiculoReference
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

/**
 * función que transforma un Vehiculo en un VehiculoDto
 * @author IvanRoncoCebadera
 * @return el VehiculoDto creado con respecto al Vehiculo
 */
fun Vehiculo.toDto(): VehiculoDTO{
    logger.debug { "transformo el Vehículo en VehiculoDto" }
    return VehiculoDTO(
        id = id.toString(),
        matricula = matricula,
        marca = marca,
        modelo = modelo,
        fechaMatriculacion = fechaMatriculacion.toString(),
        fechaUltimaRevision = if(fechaUltimaRevision == null) "" else fechaUltimaRevision.toString(),
        tipoMotor = tipoMotorText,
        tipoVehiculo = tipoVehiculoText,
        propietario = propietario.toDto()
    )
}

/**
 * función que transforma un VehiculoReference en un Vehiculo
 * @author IvanRoncoCebadera
 * @param propietario es el propietario que pertenece al vehiculo
 * @return es el Vehiculo creado con respecto al VehiculoReference
 */
fun VehiculoReference.toVehiculo(propietario: Propietario): Vehiculo{
    logger.debug { "transformo el VehículoReference en Vehiculo" }
    return Vehiculo(
        id = id,
        matricula = matricula,
        marca = marca,
        modelo = modelo,
        fechaMatriculacion = fechaMatriculacion,
        fechaUltimaRevision = fechaUltimaRevision,
        tipoMotor = tipoMotor,
        tipoVehiculo = tipoVehiculo,
        propietario = propietario
    )
}

/**
 * función que transforma un Vehiculo en un VehiculoReference
 * @author IvanRoncoCebadera
 * @return es el VehiculoReference creado con respecto al Vehiculo
 */
fun Vehiculo.toVehiculoReference(): VehiculoReference{
    logger.debug { "transformo el Vehículo en VehiculoReference" }
    return VehiculoReference(
        id = id,
        matricula = matricula,
        marca = marca,
        modelo = modelo,
        fechaMatriculacion = fechaMatriculacion,
        fechaUltimaRevision = fechaUltimaRevision,
        tipoMotor = tipoMotor,
        tipoVehiculo = tipoVehiculo,
        dniPropietario = this.propietario.dni
    )
}

/**
 * función que parsea un String al TipoMotor adecuado, es una función de extensión del tipo String.
 * @author IvanRoncoCebadera
 * @return el String parseado como TipoMotor o por defecto, TipoMotor.OTRO
 */
fun String.getTipoMotor(): TipoMotor {
    logger.debug { "Conseguimos el tipo del motor según el texto: $this" }
    return when(this.uppercase()){
        "ELECTRICO" -> TipoMotor.ELECTRICO
        "GASOLINA" -> TipoMotor.GASOLINA
        "DIESEL" -> TipoMotor.DIESEL
        "HIBRIDO" -> TipoMotor.HIBRIDO
        "OTRO" -> TipoMotor.OTRO
        else -> TipoMotor.CUALQUIERA
    }
}

/**
 * función que parsea un String al TipoVehiculo adecuado, es una función de extensión del tipo String.
 * @author IvanRoncoCebadera
 * @return el String parseado como TipoVehiculo o por defecto, TipoVehiculo.OTRO
 */
fun String.getTipoVehiculo(): TipoVehiculo {
    logger.debug { "Conseguimos el tipo del coche según el texto: $this" }
    return when(this.uppercase()){
        "TURISMO" -> TipoVehiculo.TURISMO
        "FURGONETA" -> TipoVehiculo.FURGONETA
        "CAMION" -> TipoVehiculo.CAMION
        "MOTOCICLETA" -> TipoVehiculo.MOTOCICLETA
        "OTRO" -> TipoVehiculo.OTRO
        else -> TipoVehiculo.CUALQUIERA
    }
}