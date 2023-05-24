package com.example.projectofinalitv.services.database

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.Propietario
import mu.KotlinLogging
import java.sql.DriverManager

private val logger = KotlinLogging.logger {  }

class DatabaseManager(
    private val config: ConfigApp
) {
    val connection get() = DriverManager.getConnection(config.APP_URL, config.APP_USER, config.APP_PASSWORD)

    /**
     * función que consulta la BBDD para conseguir un propietario, según su id
     * @author IvanRoncoCebadera
     * @param id es el dni con el que se buscará a un propietario para tomar sus datos
     * @return el propietario que tenia el id buscado, o un nulo
     */
    fun selectPropietarioById(id: String): Propietario? {
        logger.debug { "Tomamos un propietario de la BBDD, el de id $id" }
        var propietario: Propietario? = null
        connection.use {
            val sql = "SELECT * FROM propietario WHERE dni = ?;"

            it.prepareStatement(sql).use { stm ->

                stm.setString(1, id)

                val result = stm.executeQuery()
                while (result.next()) {
                    propietario =
                        Propietario(
                            dni = id,
                            nombre = result.getString("nombre"),
                            apellidos = result.getString("apellidos"),
                            telefono = result.getString("telefono"),
                            email = result.getString("email")
                        )
                }
            }
        }
        return propietario
    }
}