package com.example.projectofinalitv.services.storage.informe

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.error.InformeError
import com.example.projectofinalitv.mapper.toDto
import com.example.projectofinalitv.models.Informe
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import mu.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.nio.file.Path


class InformeStorageJson(
    private val configApp: ConfigApp
): IInformeSingleDataStorage, IInformeMultipleDataStorage {

    private val logger = KotlinLogging.logger { }
    private var filePath = configApp.APP_FILE_PATH+File.separator+"informes"

    /**
     * funciÃ³n que exporta los datos de un informe a un json
     * @author JiaCheng Zhang
     * @return devuelve el informe si se ha conseguido guardar y si no devuelve un error (Aplico Railway Oriented Programming)
     */
    override fun exportSingleData(data: Informe): Result<Informe, InformeError> {
        logger.debug { "Guardando informe en fichero json" }
        val file = File(filePath+File.separator+"informe.json")
        if (!file.exists()){
            Files.createDirectories(Path.of(filePath))
        }
        return try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(data.toDto())
            file.writeText(jsonString)
            Ok(data)
        } catch (e: Exception) {
            Err(InformeError.ExportingError("Se ha producido un error al exportar el informe a json"))
        }
    }

    /**
     * función que exporta una lista de informes a Json
     * @param data es la lista de informes que se quiere exportar
     * @author JiaCheng Zhang
     * @return En caso de que haya salido bien la exportacion, devolverá la lista que se ha introducido por parametros, en caso negativo, se devolverá un error
     */
    override fun exportMultipleData(data: List<Informe>): Result<List<Informe>, InformeError> {
        logger.debug { "Guardando informes en fichero json" }
        val file = File(filePath+File.separator+"informes.json")
        if (!file.exists()){
            Files.createDirectories(Path.of(filePath))
        }
        return try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(data.map { it.toDto() })
            file.writeText(jsonString)
            Ok(data)
        } catch (e: Exception) {
            Err(InformeError.ExportingError("Se ha producido un error al exportar los informes a json"))
        }

    }
}