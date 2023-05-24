package com.example.projectofinalitv.services.storage.informe

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.error.InformeError
import com.example.projectofinalitv.models.Informe
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import mu.KotlinLogging
import org.jsoup.nodes.Document
import java.io.File
import java.nio.file.Files.createFile

class InformeStorage(private val configApp: ConfigApp) {

    private val logger = KotlinLogging.logger { }
    private var filePath = configApp.APP_FILE_PATH+File.separator+"informes"+File.separator

    /**
     * función que exporta los datos de un informe a un json
     * @author JiaCheng Zhang
     * @return devuelve el informe si se ha conseguido guardar y si no devuelve un error (Aplico Railway Oriented Programming)
     */
    fun exportSingleInformeToJson(data: Informe): Result<Informe, InformeError> {
        logger.debug { "Guardando informe en fichero json" }
        val file = File(filePath+"informe.json")
        if (!file.exists()){
            createFile(file.toPath())
        }
        return try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(data)
            file.writeText(jsonString)
            Ok(data)
        } catch (e: Exception) {
            Err(InformeError.ExportingError("Se ha producido un error al exportar el informe a json"))
        }

    }

    fun exportListOfInformeToJson(data: List<Informe>): Result<List<Informe>, InformeError> {
        logger.debug { "Guardando informes en fichero json" }
        val file = File(filePath+"informes.json")
        if (!file.exists()){
            createFile(file.toPath())
        }
        return try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(data)
            file.writeText(jsonString)
            Ok(data)
        } catch (e: Exception) {
            Err(InformeError.ExportingError("Se ha producido un error al exportar los informes a json"))
        }

    }


    /**
     * Funcion que exporta los datos de un informe a un fichero HTML
     * @author JiaCheng Zhang
     * @param data es el informe que se quiere exportar
     */
    fun exportSingleInformeToHtml(data: Informe) {
        logger.debug { "Exportando informe a HTML" }
        val document = Document("")
        val html = document.appendElement("html")
        val head = html.appendElement("head")
        val body = html.appendElement("body")


        val informeDiv = body.appendElement("div")
        informeDiv.addClass("informe")

        val titulo = informeDiv.appendElement("h1")
        titulo.text("Informe")

        val fechaInicio = informeDiv.appendElement("p")
        fechaInicio.text("Fecha de inicio: ${data.fechaInicio}")

        val fechaFinal = informeDiv.appendElement("p")
        fechaFinal.text("Fecha final: ${data.fechaFinal}")

        val favorable = informeDiv.appendElement("p")
        favorable.text("Favorable: ${data.favorable ?: ""}")

        val frenado = informeDiv.appendElement("p")
        frenado.text("Frenado: ${data.frenado ?: ""}")

        val contaminacion = informeDiv.appendElement("p")
        contaminacion.text("Contaminacion: ${data.contaminacion ?: ""}")

        val interior = informeDiv.appendElement("p")
        interior.text("Interior: ${data.interior ?: ""}")

        val luces = informeDiv.appendElement("p")
        luces.text("Luces: ${data.luces ?: ""}")


        val vehiculo = informeDiv.appendElement("p")
        vehiculo.text("Vehiculo: ${data.vehiculo.marca}")

        val trabajadorId = informeDiv.appendElement("p")
        vehiculo.text("Vehiculo: ${data.trabajadorId ?:""}")

        val file = File(filePath+"informe.html")
        if (!file.exists()){
            createFile(file.toPath())
        }
        file.writeText(document.html())
    }
}
