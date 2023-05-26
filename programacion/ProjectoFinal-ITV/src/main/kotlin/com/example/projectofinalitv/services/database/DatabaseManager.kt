package com.example.projectofinalitv.services.database

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.mapper.getTipoMotor
import com.example.projectofinalitv.mapper.getTipoVehiculo
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.utils.toLocalDate
import com.example.projectofinalitv.utils.toLocalDateTime
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path
import java.sql.DriverManager

private val logger = KotlinLogging.logger {  }

class DatabaseManager(
    private val config: ConfigApp
) {
    val connection get() = DriverManager.getConnection(config.APP_URL, config.APP_USER, config.APP_PASSWORD)

    /**
     * función que consigue un vehiculo, según su id
     * @author IvanRoncoCebadera
     * @param id es el identificador de vehiculo que se quiere buscar
     * @return el vehiculo que tenia el id buscado, o un nulo
     */
    fun getVehiculoById(id: Long): Vehiculo? {
        logger.debug { "Se busca al vehículo de id: $id" }
        var vehiculo: Vehiculo? = null
        connection.use {
            val sql = "SELECT * FROM vehiculo WHERE id_vehiculo = ?;"

            it.prepareStatement(sql).use { stm ->

                stm.setLong(1, id)

                val result = stm.executeQuery()
                while (result.next()) {
                    vehiculo =
                        Vehiculo(
                            id = result.getLong("id_vehiculo"),
                            matricula = result.getString("matricula"),
                            marca = result.getString("marca"),
                            modelo = result.getString("modelo"),
                            fechaMatriculacion = result.getString("fecha_matriculacion").toLocalDate(),
                            fechaUltimaRevision = result.getString("fecha_ultima_revision").toLocalDateTime(),
                            tipoMotor = result.getString("tipo_motor").getTipoMotor(),
                            tipoVehiculo = result.getString("tipo_vehiculo").getTipoVehiculo(),
                            propietario = selectPropietarioById(result.getString("id_propietario"))!!
                        )
                }
            }
        }
        return vehiculo
    }

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