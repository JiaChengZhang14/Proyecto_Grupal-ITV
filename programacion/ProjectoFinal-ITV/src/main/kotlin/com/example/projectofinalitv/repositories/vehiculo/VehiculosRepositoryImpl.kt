package com.example.projectofinalitv.repositories.vehiculo

import com.example.projectofinalitv.mapper.getTipoMotor
import com.example.projectofinalitv.mapper.getTipoVehiculo
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.services.database.DatabaseManager
import com.example.projectofinalitv.utils.toLocalDate
import com.example.projectofinalitv.utils.toLocalDateTime
import mu.KotlinLogging
import java.sql.Statement
import kotlin.math.log

private val logger = KotlinLogging.logger {  }

class VehiculosRepositoryImpl(
    private val database: DatabaseManager
): IVehiculosRepository {

    /**
     * función que llama al DatabaseManager para conseguir todos los vehículos
     * @author IvanRoncoCebadera
     * @return una lista con todos los vehículos que se hayan conseguido por parte del DatabaseManager
     */
    override fun getAll(): List<Vehiculo> {
        logger.debug { "El repositorio llama a la DatabaseManager para tomar todos los vehículos" }
        val vehiculos = mutableListOf<Vehiculo>()
        database.connection.use {
            val sql = "SELECT * FROM vehiculo;"

            it.prepareStatement(sql).use { stm ->
                val result = stm.executeQuery()
                while(result.next()){
                    vehiculos.add(
                        Vehiculo(
                            id = result.getLong("id_vehiculo"),
                            matricula = result.getString("matricula"),
                            marca = result.getString("marca"),
                            modelo = result.getString("modelo"),
                            fechaMatriculacion = result.getString("fecha_matriculacion").toLocalDate(),
                            fechaUltimaRevision = result.getString("fecha_ultima_revision").toLocalDateTime(),
                            tipoMotor = result.getString("tipo_motor").getTipoMotor(),
                            tipoVehiculo = result.getString("tipo_vehiculo").getTipoVehiculo(),
                            propietario = database.selectPropietarioById(result.getString("id_propietario"))!!
                        )
                    )
                }
            }
        }
        return vehiculos
    }

    /**
     * función que llama al DatabaseManager para conseguir un vehiculo, según su id
     * @author IvanRoncoCebadera
     * @param id es el identificador de vehiculo que se quiere buscar
     * @return el vehiculo que tenia el id buscado, o un nulo
     */
    override fun getById(id: Long): Vehiculo? {
        logger.debug { "El repositorio llama a la DatabaseManager para tomar un vehículo de id: $id" }
        return database.getVehiculoById(id)
    }

    /**
     * función que llama al DatabaseMAnager para comprobar si se inserta o actualiza un vehículo
     * @author IvanRoncoCebadera
     * @param el vehiculo que se insertara o que actualizara la BBDD
     * @return el vehiculo insertado o que ha actualizado la BBDD
     */
    override fun save(entity: Vehiculo): Vehiculo {
        return getById(entity.id)?.let{
            updateVehiculo(entity)
        }?: run {
            createVehiculo(entity)
        }
    }

    /**
     * función que llama al DatabaseMAnager para insertar un nuevo vehículo
     * @author IvanRoncoCebadera
     * @param el vehiculo que se insertará
     * @return el vehiculo insertado
     */
    private fun createVehiculo(vehiculo: Vehiculo): Vehiculo{
        logger.debug { "El repositorio llama al DatabaseManager para insertar un nuevo vehiculo" }
        var myId = 0L
            database.connection.use {
            val sql = "INSERT INTO vehiculo VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?);"

            it.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use{ stm ->
                stm.setString(1, vehiculo.matricula)
                stm.setString(2, vehiculo.propietario!!.dni)
                stm.setString(3, vehiculo.marca)
                stm.setString(4, vehiculo.modelo)
                stm.setObject(5, vehiculo.fechaMatriculacion)
                stm.setString(6, if(vehiculo.fechaUltimaRevision == null) "" else vehiculo.fechaUltimaRevision.toString())
                stm.setString(7, vehiculo.tipoMotor.toString())
                stm.setString(8, vehiculo.tipoVehiculo.toString())

                stm.executeUpdate()

                val id = stm.generatedKeys
                while(id.next()){
                    myId = id.getLong(1)
                }
            }
        }
        return vehiculo.copy(id = myId)
    }

    /**
     * función que llama al DatabaseMAnager para actualiza un vehículo
     * @author IvanRoncoCebadera
     * @param el vehiculo que se actualizara la BBDD
     * @return el vehiculo que ha actualizado la BBDD
     */
    private fun updateVehiculo(vehiculo: Vehiculo): Vehiculo{
        logger.debug { "El repositorio llama al DatabaseManager para actualizar la información de un vehiculo" }
        database.connection.use {
            val sql = "UPDATE vehiculo SET matricula = ?, marca = ?, modelo = ?, fecha_matriculacion = ?, fecha_ultima_revision = ?, tipo_motor = ?, tipo_vehiculo = ?, id_propietario = ?  WHERE id_vehiculo = ?;"

            it.prepareStatement(sql).use{stm ->
                stm.setString(1, vehiculo.matricula)
                stm.setString(2, vehiculo.marca)
                stm.setString(3, vehiculo.modelo)
                stm.setObject(4, vehiculo.fechaMatriculacion)
                stm.setString(5, if(vehiculo.fechaUltimaRevision == null) "" else vehiculo.fechaUltimaRevision.toString())
                stm.setString(6, vehiculo.tipoMotor.toString())
                stm.setString(7, vehiculo.tipoVehiculo.toString())
                stm.setString(8, vehiculo.propietario!!.dni)
                stm.setLong(9, vehiculo.id)

                stm.executeUpdate()

            }
        }
        return vehiculo
    }

    /**
     * función que llama al DatabaseManager para eliminar un vehículo, según su id
     * @author IvanRoncoCebadera
     * @param id es el identificador que se usará para buscar y eliminar el vehículo
     * @return true si se elimina el vehículo, false si no se consigue eliminar el vehículo
     */
    override fun deleteById(id: Long): Boolean {
        logger.debug { "El repositorio llama al DatabaseManager para borrar al vehículo de id: $id" }
        var res = 0
            database.connection.use {
            val sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?;"
            it.prepareStatement(sql).use { stm ->
                stm.setLong(1, id)

                res = stm.executeUpdate()
            }
        }
        return res >= 1
    }

    /**
     * función que llama al DatabaseManager para eliminar todos los vehículos
     * @author IvanRoncoCebadera
     * @return true si se eliminan los vehículos, false si no se consiguen eliminar los vehículos
     */
    override fun deleteAll(): Boolean {
        logger.debug { "El repositorio llama al DatabaseManager para borrar todos los vehículos" }
        var res = 0
        database.connection.use {
            val sql = "DELETE FROM vehiculo;"
            it.prepareStatement(sql).use { stm ->
                res = stm.executeUpdate()
            }
        }
        return res >= 0
    }

    /**
     * función que resetea el valor del autonumérico de la tabla de vehículo
     * @author IvanRoncoCebadera
     * @param nuevoValor el nuevo valor a aplicar a la tbla vehículo
     */
    fun resetearValorAutoIncrementDeLaTablaVehículo(nuevoValor: Long){
        logger.debug { "Se resetea el valor del autonumérico de la tabla de vehículo a: $nuevoValor" }
        database.connection.use {
            val sql = "ALTER TABLE vehiculo AUTO_INCREMENT = ?;"

            it.prepareStatement(sql).use { stm ->
                stm.setLong(1, nuevoValor)

                stm.executeUpdate()
            }
        }
    }
}