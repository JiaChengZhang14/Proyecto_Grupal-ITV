package com.example.projectofinalitv.services.storage.informe

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.Informe
import mu.KotlinLogging
import org.jsoup.nodes.Document
import java.io.File
import java.nio.file.Files
import java.nio.file.Files.createFile
import java.nio.file.Path

private val logger = KotlinLogging.logger {  }

class InformeStorageHtml(
    private val configApp: ConfigApp
) {

    private var filePath = configApp.APP_FILE_PATH+File.separator+"informes"

    /**
     * Funcion que exporta los datos de un informe a un fichero HTML
     * @author JiaCheng Zhang
     * @param data es el informe que se quiere exportar
     */
    fun exportSingleInformeToHtml(data: Informe) {
        logger.debug { "Exportando informe a HTML" }

        val file = File(filePath+File.separator+"informe.html")
        if (!file.exists()){
            Files.createDirectories(Path.of(filePath))
        }
        file.writeText("<html>\n")
        file.appendText("<head>\n")
        file.appendText("<title>Informe en HTML</title>\n")
        file.appendText("</head>\n")
        file.appendText("<body>\n")
        file.appendText("<h1>Informe</h1>")
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

    }
}