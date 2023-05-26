package com.example.projectofinalitv.utils

import com.example.projectofinalitv.models.Especialidad
import com.example.projectofinalitv.routes.RoutesManager
import mu.KotlinLogging
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {  }

fun String.getEspecialidad(): Especialidad {
    logger.debug { "Conseguimos la especialidad del trabajador según el texto: $this" }
    return when(this.uppercase()){
        "ELECTRICIDAD" -> Especialidad.ELECTRICIDAD
        "MOTOR" -> Especialidad.MOTOR
        "INTERIOR" -> Especialidad.INTERIOR
        else -> Especialidad.ADMINISTRACION
    }
}

/**
 * función que consigue el recurso pedido como URL
 * @author IvanRoncoCebadera
 * @param resource es el nombre del recurso a conseguir
 * @return el recurso pedido como URL
 * @throws RuntimeException si no se encuentra el recurso pedido
 */
fun getResource(resource: String): URL {
    logger.debug { "Conseguimos el recurso: $resource como URL" }
    return RoutesManager.app::class.java.getResource(resource)
        ?: throw RuntimeException("No se ha encontrado el recurso: $resource")
}

/**
 * función que consigue el recurso pedido como Stream
 * @author IvanRoncoCebadera
 * @param resource es el nombre del recurso a conseguir
 * @return el recurso pedido como Stream
 * @throws RuntimeException si no se encuentra el recurso pedido
 */
fun getResourceAsStream(resource: String): InputStream {
    logger.debug { "Conseguimos el recurso: $resource como Stream" }
    return RoutesManager.app::class.java.getResourceAsStream(resource)
        ?: throw RuntimeException("No se ha encontrado el recurso como stream: $resource")
}

/**
 * función que parsea un String a un LocalDate, es una función de extensión del tipo String.
 * @author IvanRoncoCebadera
 * @return el String parseado como LocalDate o por defecto, la fecha actual
 */
fun String.toLocalDate(): LocalDate {
    logger.debug { "Parseamos la fecha al tipo LocalDate" }
    var fecha = LocalDate.now()
    try {
        fecha = LocalDate.parse(this)
    }catch (_: Exception){
        var datos = this.split("/")
        if(datos.size == 3){
            try {
                fecha = LocalDate.of(datos[0].toInt(), datos[1].toInt(), datos[2].toInt())
            }catch (_: Exception){
                fecha = LocalDate.of(datos[2].toInt(), datos[1].toInt(), datos[0].toInt())
            }
        }
    }
    return fecha
}

/**
 * función que parsea un String a un LocalDateTime, es una función de extensión del tipo String.
 * @author IvanRoncoCebadera
 * @return el String parseado como LocalDateTime o por defecto, la fecha y hora del momento en el que se paso por esta función
 */
fun String.toLocalDateTime(): LocalDateTime? {
    logger.debug { "Parseamos la fecha al tipo LocalDateTime" }
    if(this == ""){
        return null
    }
    var fecha = LocalDateTime.now()
    val regexLocalDateTime = Regex("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}")
    val fechaYHora = this.split(" ")
    if(this.matches(regexLocalDateTime) || fechaYHora.size == 2){
        if(fechaYHora.size == 2){
            //Este apartado de si se puede partir por " ", es debido a que 'localtime()' en MySQL no da la fecha como: 2007-12-03 10:15:3 y no como 2007-12-03T10:15:3.
            try{
                fecha = LocalDateTime.parse(fechaYHora.joinToString(separator = "T"))
            }catch (_: Exception){
                return fecha
            }
        }else{
            try{
                fecha = LocalDateTime.parse(this)
            }catch (_: Exception){
                return fecha
            }
        }
    }
    return fecha
}

/**
 * función que toma la fecha y la hora para juntarlas en un LocalDateTime
 * @author IvanRoncoCebadera
 * @param fecha es la fecha
 * @param hora es la hora, minutos y segundos para crear el LocalDateTime
 * @return el LocalDateTime que resulta de juntar la hora y la fecha introducida
 */
fun toLocalDateTimeFromFechaHora(fecha: LocalDate, hora: String): LocalDateTime {
    logger.debug { "Creamos un LocalDateTime, apartir de: $fecha, y $hora" }
    return "$fecha $hora".toLocalDateTime()!!
}

/**
 * función que recuperá los intevalos iniciales y finales de una revision
 * @author IvanRoncoCebadera
 * @param ultimaRevision es el LocalDateTime que representa la hora del inicio del intervalo
 * @return un par de par de valores, los primeros son la hora y minutos iniciales, los segundos son la hora y minutos finales
 */
fun conseguirIntervaloInicialYFinal(ultimaRevision: LocalDateTime?): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    logger.debug { "Se consigue el intervalo inicial y final de: $ultimaRevision" }
    if (ultimaRevision == null){
        return Pair(Pair(-1,-1), Pair(-1,-1))
    }
    val minutoPincipioIntervalo = ultimaRevision!!.minute
    val horaPrincipoIntervalo = ultimaRevision!!.hour

    var minutosFinIntervalo = minutoPincipioIntervalo+30
    if(minutosFinIntervalo == 60) minutosFinIntervalo = 0
    val horaFinIntervalo = if(minutosFinIntervalo != 0) horaPrincipoIntervalo else horaPrincipoIntervalo + 1

    return Pair(Pair(horaPrincipoIntervalo, minutoPincipioIntervalo), Pair(horaFinIntervalo, minutosFinIntervalo))
}