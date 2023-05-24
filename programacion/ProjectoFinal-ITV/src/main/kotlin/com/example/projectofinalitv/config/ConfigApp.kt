package com.example.projectofinalitv.config

import mu.KotlinLogging
import java.io.File
import java.io.FileNotFoundException
import java.util.*

private  val logger = KotlinLogging.logger {  }

class ConfigApp {

    val APP_URL by lazy {
        readProperty("app.database.url") ?: "jdbc:mariadb://localhost:3306/empresaitv?serverTimezone=UTC"
    }

    val APP_USER: String by lazy {
        readProperty("app.database.user") ?: "root"
    }

    val APP_PASSWORD: String by lazy {
        readProperty("app.database.password") ?: ""
    }

    val APP_INIT: Boolean by lazy {
        (readProperty("app.database.init") ?: "true") == "true"
    }

    val APP_DELETE_ALL_BD: Boolean by lazy {
        (readProperty("app.database.deleteData") ?: "false") == "true"
    }

    val APP_FILE_PATH: String by lazy {
        System.getProperty("user.dir")+File.separator+readProperty("app.file.path")
    }

    /**
     * función que carga la propiedad pedida del fichero "application.properties"
     * @author IvanRoncoCebadera
     * @param property es el nombre de la propiedad cuyo valor se pide conseguir
     * @return el valor de la propiedad pedida por su nombre, o nulo si no encuentra la propiedad pedida
     */
    private fun readProperty(property: String): String?{
        logger.debug { "Cargamos la propiedad $property" }
        val properties = Properties()
        properties.load(
            ClassLoader.getSystemResourceAsStream("application.properties") ?: throw FileNotFoundException("El fichero de propiedades no se ha encontrado.")
        )
        return properties.getProperty(property)
    }
}