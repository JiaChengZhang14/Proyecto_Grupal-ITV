package com.example.projectofinalitv.services.storage.informe

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.error.InformeError
import com.example.projectofinalitv.models.Informe
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

private val logger = KotlinLogging.logger {  }

class InformeStorageHtml(
    private val configApp: ConfigApp
) : IInformeSingleDataStorage{

    var filePath = configApp.APP_FILE_PATH+File.separator+"informes"

    /**
     * Funcion que exporta los datos de un informe a un fichero HTML
     * @author JiaCheng Zhang
     * @param data es el informe que se quiere exportar
     */
    override fun exportSingleData(data: Informe): Result<Informe, InformeError> {
        logger.debug { "Exportando informe a HTML" }

        val contador = conseguirContadorActual()

        val file = File(filePath+File.separator+"informe$contador.html")
        if (!file.exists()){
            Files.createDirectories(Path.of(filePath))
        }
        return try {
            file.writeText("<html>\n")
            file.appendText("<head>\n")
            file.appendText("<title>Informe en HTML</title>\n")
            file.appendText("</head>\n")
            file.appendText("<body>\n")
            file.appendText("<h1>Informe</h1>")
            file.appendText("<p>${data.idInforme}</p>\n")
            file.appendText("<p>${data.fechaInicio}</p>\n")
            file.appendText("<p>${data.fechaFinal}</p>\n")
            file.appendText("<p>${data.favorable}</p>\n")
            file.appendText("<p>${data.frenado}</p>\n")
            file.appendText("<p>${data.contaminacion}</p>\n")
            file.appendText("<p>${data.interior}</p>\n")
            file.appendText("<p>${data.luces}</p>\n")
            file.appendText("<p>${data.vehiculo.id}</p>\n")
            file.appendText("<p>${data.trabajadorId}</p>\n")
            file.appendText("</body>\n")
            file.appendText("</html>\n")
            Ok(data)
        }catch (e: Exception){
            Err(InformeError.ExportingError("Ha ocurrido un error al tratar de exportar el informe seleccionado a HTML"))
        }
    }

    /**
     * función que consigue el contador del número actual de los fiheros de informe.html
     * @author IvánRoncoCebadera
     * @return el número actual a incluir en el nombre del nuevo fichero informe.html
     */
    private fun conseguirContadorActual(): Int {
        val regexNum = Regex("[0-9]*")
        val posiblesNumeros = Files.list(Path.of(filePath)).map { it.toString() }.filter { it.contains(".html") }
            .map { it.removePrefix(filePath + File.separator + "informe") }
            .map { it.removeSuffix(".html") }
            .filter { it.matches(regexNum) && it != "" }.toList()
        if (posiblesNumeros.isNotEmpty()) {
            val numero = posiblesNumeros.map { it.toInt() }.maxOrNull()
            if (numero != null) {
                return numero + 1
            }
        }
        return 1
    }
}