package com.example.projectofinalitv.models

import java.time.LocalDate

data class Trabajador(
    val idTrabajador: Long,
    val idEstacion: Long,
    val nombre: String,
    val telefono: String,
    val email: String,
    val nombreUsuario: String,
    val contraseniaUsuario: String, // El cifrado de este campo se hace en la BBDD, despues cuando se recuperé ya estará cifrado, igualmente ver si hay un password field o algo qu lo haga!!!!!
    val fechaContratacion: LocalDate,
    var especialidades: List<Especialidad>,
    val idResponsable: Long,
    val informes: List<Informe> = listOf()
){
    companion object{
        const val TRABAJADOR_ID = -1L
    }
    init {
        comprobarSiEsElResponsable()
    }



    /**
     * función que comprueba si el trabajador es el responsable, en caso afirmativo le añade esa responsabilidad
     * @author IvanRoncoCebadera
     */
    private fun comprobarSiEsElResponsable() {
        if (idTrabajador == idResponsable) {
            especialidades = especialidades + Especialidad.RESPONSABLE
        }
    }
    val salario get() = getSalarioTotal()
    /**
     * función que calcula automaticamente el salario del trabajador según sus especialidad y sus años de antigüedad
     * @author IvanRoncoCebadera
     * @return el salario total del trabajador
     */
    private fun getSalarioTotal(): Double {
        var resultado = 0.0
        especialidades.forEach {
            resultado += it.paga
        }
        // Dividido por tres ya qu sumamos 100€ al salario por cada tres años de antigüedad
        repeat(calcularAniosContratados()/3){
            resultado += 100
        }
        return resultado
    }
    /**
     * función que calcula los años que lleva trabajados el trabajador, hasta el día de hoy
     * @author IvanRoncoCebadera
     * @return el número de años totales que tiene de antigüedad en la empresa el trabajador
     */
    private fun calcularAniosContratados(): Int {
        return LocalDate.now().year-fechaContratacion.year
    }

    override fun toString(): String {
        return "Trabajador(idTrabajador=$idTrabajador, idEstacion=$idEstacion, nombre='$nombre', salario=$salario, informes=$informes)"
    }
}

enum class Especialidad(val paga: Double) {
    ELECTRICIDAD(1800.0), MOTOR(1700.0), MECANICA(1600.0), INTERIOR(1750.0), ADMINISTRACION(1650.0), RESPONSABLE(1000.0)
}
