package com.example.projectofinalitv.mapper

import com.example.projectofinalitv.dto.InformeDTO
import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.models.IsApto
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

/**
 * función que recibe un Informe y devuelve un InformeDto
 * @author IvanRoncoCebadera
 * @return un InformeDto creado a partir de un Informe
 */
fun Informe.toDto(): InformeDTO{
    logger.debug { "Transformamos un Informe a InformeDto" }
    return InformeDTO(
        fechaInicio = this.fechaInicio.toString(),
        fechaFinal = this.fechaFinal.toString(),
        favorable = if(this.favorable == null) "" else this.favorable.string,
        frenado = if(this.frenado == null) "" else this.frenadoDosDecimales,
        contaminacion = if(this.contaminacion == null) "" else this.contaminacionDosDecimales,
        interior = if(this.interior == null) "" else this.interior.string,
        luces = if(this.luces == null) "" else this.luces.string,
        vehiculo = this.vehiculo.toDto(),
        trabajador = this.trabajadorId.toString()
    )
}

/**
 * función que parsea un String al IsApto adecuado, es una función de extensión del tipo String.
 * @author IvanRoncoCebadera
 * @return el String parseado como IsApto
 */
fun String.getApto(): IsApto {
    logger.debug { "Averiguamos si es apto o no según el texto: $this" }
    return when(this.uppercase()){
        "APTO" -> IsApto.APTO
        else -> IsApto.NO_APTO
    }
}

