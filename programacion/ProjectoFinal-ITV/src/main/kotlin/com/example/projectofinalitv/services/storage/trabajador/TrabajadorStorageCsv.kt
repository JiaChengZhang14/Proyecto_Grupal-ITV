package com.example.projectofinalitv.services.storage.trabajador

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.error.TrabajadorError
import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.Trabajador
import com.example.projectofinalitv.models.Vehiculo
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

private val logger = KotlinLogging.logger {  }

class TrabajadorStorageCsv(
    private val configApp: ConfigApp
): ITrabajadorStorage {
    private val filePath = configApp.APP_FILE_PATH + File.separator + "trabajadores"

    /**
     * funcion que exxporta a .CSV los trabajdores de la base de datos
     * @author KevinMatute
     * @param items es una lista de ols trabajadores que se quieran exortar
    */
    //revisar para meter try catch  yresult
    override fun exportMultipleData(data: List<Trabajador>): Result<List<Trabajador>, TrabajadorError>{
        logger.debug { "Se exportan los datos de los trabajadores a Csv" }
        val file = File(filePath + File.separator + "trabajador.csv")
        if (!file.exists()){
            Files.createDirectories(Path.of(filePath))
        }
        return try{
            file.writeText("idTrabajador;idEstacion;nombre;telefono;email;nombreUsuario;contraseñaUsuario;fechaContratacion;especialidades;idResponsable;informes;salario" + "\n")
            data.forEach {
                file.appendText("${it.idTrabajador};${it.idEstacion};${it.nombre};${it.telefono};${it.email};${it.nombreUsuario};${it.contraseniaUsuario};${it.fechaContratacion};${it.especialidades.joinToString(separator = "|", prefix = "[", postfix = "]")};${it.idResponsable};${it.informes.toCsv()};${it.salario}" + "\n")
            }
            Ok(data)
        }catch (e: Exception){
            Err(TrabajadorError.ExportingError("Ha ocurrido un error al exportar a los trabajadores a CSV"))
        }
    }

    private fun List<Informe>.toCsv(): String {
        logger.debug { "Se transforma la lista de informes en un string para el csv" }
        return this.map {
            listOf(
                it.fechaInicio.toString(),
                it.fechaFinal.toString(),
                if(it.favorable == null) "" else it.favorable.string,
                if(it.frenado == null) "" else it.frenadoDosDecimales,
                if(it.contaminacion == null) "" else it.contaminacionDosDecimales,
                if(it.interior == null) "" else it.interior.string,
                if(it.luces == null) "" else it.luces.string,
                it.vehiculo.toCsv()
            )
        }.joinToString(separator = "|")
    }

    private fun Vehiculo.toCsv(): String {
        return "(${this.id}~${this.matricula}~${this.marca}~${this.modelo}~${this.fechaMatriculacion}~${this.fechaUltimaRevision}~${this.tipoMotorText}~${this.tipoVehiculoText}~${this.propietario.toCsv()})"
    }

    private fun Propietario.toCsv(): String {
        return "[${this.dni}||${this.nombre}||${this.apellidos}||${this.email}||${this.telefono}]"
    }
}
