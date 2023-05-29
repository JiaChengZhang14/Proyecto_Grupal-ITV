package com.example.projectofinalitv.mapper

import com.example.projectofinalitv.dto.InformeDTO
import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.models.IsApto
import com.example.projectofinalitv.viewmodel.InformeReference
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
        idInforme = this.idInforme.toString(),
        fechaInicio = this.fechaInicio.toString(),
        fechaFinal = this.fechaFinal.toString(),
        favorable = if(this.favorable == IsApto.NOT_CHOOSEN) null else this.favorable.string,
        frenado = if((this.frenado == null) || (this.frenado.toString() == "") || (this.frenado.toString() == "0.0")) null else this.frenadoDosDecimales,
        contaminacion = if((this.contaminacion == null) || (this.contaminacion.toString() == "") || (this.contaminacion.toString() == "0.0")) null else this.contaminacionDosDecimales,
        interior = if(this.interior == IsApto.NOT_CHOOSEN) null else this.interior.string,
        luces = if(this.luces == IsApto.NOT_CHOOSEN) null else this.luces.string,
        vehiculo = this.vehiculo.toDto(),
        trabajador = this.trabajadorId.toString()
    )
}

/**
 * función que recibe un Informe y devuelve un InformeReference
 * @author IvanRoncoCebadera
 * @return un InformeReference creado a partir de un Informe
 */
fun Informe.toInformeReference(): InformeReference{
    logger.debug { "Transformamos un Informe a InformeReference" }
    return InformeReference(
        idInforme = this.idInforme,
        fechaInicio = this.fechaInicio,
        fechaFinal = this.fechaFinal,
        favorable = this.favorable,
        frenado = this.frenado,
        contaminacion = this.contaminacion,
        interior = this.interior,
        luces = this.luces,
        vehiculo = this.vehiculo,
        trabajadorId = this.trabajadorId
    )
}

/**
 * función que recibe un InformeReference y devuelve un Informe
 * @author IvanRoncoCebadera
 * @return un Informe creado a partir de un InformeReference
 */
fun InformeReference.toInforme(): Informe{
    logger.debug { "Transformamos un InformeReference a Informe" }
    return Informe(
        idInforme = this.idInforme,
        fechaInicio = this.fechaInicio!!,
        fechaFinal = this.fechaFinal!!,
        favorable = this.favorable,
        frenado = this.frenado,
        contaminacion = this.contaminacion,
        interior = this.interior,
        luces = this.luces,
        vehiculo = this.vehiculo!!,
        trabajadorId = this.trabajadorId
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
        "NO_APTO" -> IsApto.NO_APTO
        "NO APTO" -> IsApto.NO_APTO
        else -> IsApto.NOT_CHOOSEN
    }
}