package com.example.projectofinalitv.repositories.informe

import com.example.projectofinalitv.mapper.getApto
import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.services.database.DatabaseManager
import com.example.projectofinalitv.utils.toLocalDateTime
import mu.KotlinLogging
import java.sql.Statement

private val logger = KotlinLogging.logger {  }

class InformeRepositoryImpl(
    private val database: DatabaseManager
): IInformeRepository {
    /**
     * función que recuperá y devuelve una cita, en caso de que exista, según la fecha en que se realizo y el vehículo al que se realizo
     * @author IvanRoncoCebadera
     * @param idInforme es el identificador del informe que se busca
     * @return el Informe encontrado, o un nulo si no se encuentra ningún Informe
     */
    override fun getInforme(idInforme: Long): Informe? {
        logger.debug { "Conseguimos el informe de id: $idInforme" }
        var informe: Informe? = null
        database.connection.use {
            val sql = "SELECT * FROM informe WHERE id_informe = ?;"
            it.prepareStatement(sql).use { stm ->
                stm.setLong(1, idInforme)

                val res = stm.executeQuery()
                if(res.next()){
                    informe = Informe(
                        idInforme = idInforme,
                        fechaInicio = res.getString("fecha_inicio").toLocalDateTime()!!,
                        fechaFinal = res.getString("fecha_final").toLocalDateTime()!!,
                        favorable = res.getString("favorable").getApto(),
                        frenado = res.getDouble("frenado"),
                        contaminacion = res.getDouble("contaminacion"),
                        interior = res.getString("interior").getApto(),
                        luces = res.getString("luces").getApto(),
                        vehiculo = database.getVehiculoById(res.getLong("id_vehiculo"))!!,
                        trabajadorId = res.getLong("id_trabajador")
                    )
                }
            }
        }
        return informe
    }

    /**
     * función que guarda los datos de un informe en la BBDD
     * @author IvanRoncoCebadera
     * @param informe el informe cuyos datos se van a guardar
     * @return el Informe que se guarda
     */
    override fun saveInforme(informe: Informe): Informe {
        return getInforme(informe.idInforme)?.let {
            updateInforme(informe)
        } ?: run {
            insertInforme(informe)
        }
    }

    /**
     * Funcion que actuliza los datos un informe en la base de datos
     * @param informe es el informe con los datos actualizados
     * @return devuelve el informe actualizado
     * @author JiaCheng Zhang, Kevin David Matute
     */
    private fun updateInforme(informe: Informe): Informe {
        logger.debug { "Se actualiza un informe" }
        database.connection.use {
            val sql = "UPDATE informe SET fecha_inicio = ?, fecha_final = ?, favorable = ?, frenado = ?, contaminacion = ?, interior = ?, luces = ?, id_vehiculo = ?, id_trabajador = ? WHERE id_informe = ?;"
            it.prepareStatement(sql).use { stm ->
                stm.setObject(1, informe.fechaInicio)
                stm.setObject(2, informe.fechaFinal)
                stm.setString(3, informe.favorable?.string ?: "")
                stm.setObject(4, informe.frenado)
                stm.setObject(5, informe.contaminacion)
                stm.setString(6, informe.interior?.string ?: "")
                stm.setString(7, informe.luces?.string ?: "")
                stm.setLong(8, informe.vehiculo.id)
                stm.setLong(9, informe.trabajadorId)
                stm.setLong(10, informe.idInforme)

                stm.executeUpdate()
            }
        }
        return informe
    }

    /**
     * Funcion que inserta un informe en la base de datos
     * @param informe es el informe que se va a insertar
     * @return devuelve el informe introducido
     * @author JiaCheng Zhang, Kevin David Matute
     */
    private fun insertInforme(informe: Informe): Informe {
        logger.debug { "Se inserta un nuevo informe" }
        var myId = -1L
        database.connection.use {
            val sql = "INSERT INTO informe VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
            it.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stm ->
                stm.setObject(1, informe.fechaInicio)
                stm.setObject(2, informe.fechaFinal)
                stm.setLong(3, informe.vehiculo.id)
                stm.setLong(4, informe.trabajadorId)
                stm.setString(5, informe.favorable?.string ?: "")
                stm.setObject(6, informe.frenado)
                stm.setObject(7, informe.contaminacion)
                stm.setString(8, informe.interior?.string ?: "")
                stm.setString(9, informe.luces?.string ?: "")

                stm.executeUpdate()

                val res = stm.generatedKeys
                if(res.next()){
                    myId = res.getLong(1)
                }
            }
        }
        return informe.copy(idInforme = myId)
    }

    /**
     * función que elimina un informe según el id
     * @author IvanRoncoCebadera
     * @param idInforme es el identificador del Informe a eliminar
     * @return true si se ha eliminado al Informe, false en caso contrario
     */
    override fun deleteInforme(idInforme: Long): Boolean {
        logger.debug { "Se borra el informe de id: $idInforme" }
        var res = 0
        database.connection.use {
            val sql = "DELETE FROM informe WHERE id_informe = ?;"
            it.prepareStatement(sql).use { stm ->
                stm.setLong(1, idInforme)

                res = stm.executeUpdate()
            }
        }
        return res >= 1
    }

    /**
     * función que elimina todos los informes de la BBDD
     * @author IvanRoncoCebadera
     * @return true si se ha eliminado algún Informe, false en caso contrario
     */
    override fun deleteAllInformes(): Boolean {
        logger.debug { "Se borran todos los informes" }
        var res = 0
        database.connection.use {
            val sql = "DELETE FROM informe;"
            res = it.prepareStatement(sql).executeUpdate()
        }
        return res >= 1
    }

    /**
     * funcion que restaura el valor del id autorincremental a un valor que se le introduce por parametros
     * @param nuevoValor es el valor al que se quiere restaurar el id autorincremental
     * @author JiaCheng Zhang & Kevin David Matute
     */
    fun resetearValorAutoIncrementDeLaTablaVehiculo(nuevoValor: Long){
        logger.debug { "Se resetea el valor del autonumérico de la tabla de vehículo a: $nuevoValor" }
        database.connection.use {
            val sql = "ALTER TABLE informe AUTO_INCREMENT = ?;"

            it.prepareStatement(sql).use { stm ->
                stm.setLong(1, nuevoValor)

                stm.executeUpdate()
            }
        }
    }
}