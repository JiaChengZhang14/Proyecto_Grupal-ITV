package com.example.projectofinalitv.repositories.propietario

import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.services.database.DatabaseManager
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

class PropietarioRepositoryImpl(
    private val database: DatabaseManager
) : IPropietarioRepository {

    /**
     * @author JiaCheng Zhang, Kevin Matute
     * @return Devuelve una lista con todos los propietarios de la base de datos.
     */
    override fun getAll(): List<Propietario> {
        logger.debug { "getting all propietarios" }
        val propietarios = mutableListOf<Propietario>()
        val sql = """SELECT * FROM propietario""".trimIndent()
        database.connection.use {
            it.prepareStatement(sql).use { stm ->
                val res = stm.executeQuery()
                while (res.next()) {
                    propietarios.add(
                        Propietario(
                            dni = res.getString("dni"),
                            nombre = res.getString("nombre"),
                            apellidos = res.getString("apellidos"),
                            telefono = res.getString("telefono"),
                            email = res.getString("email"),
                        )
                    )
                }
            }
        }
        return propietarios
    }


    /**
     * Funcion que hace la consulta a la base de datos para recuperar un propietario
     * @autor JiaCheng Zhang, Kevin Matute
     * @param id  Es el dni del propietario que se quiere recuperar
     * @return Devuelve el propietario con el dni que se ha introducido por parametros
     */
    override fun getById(id: String): Propietario? {
        logger.debug { "getting propietario by id" }
        return database.selectPropietarioById(id)
    }


    /**
     * función que añade a la base de datos
     * @author JiaCheng Zhang, Kevin Matute
     * @param entity Es el propietario que se quiere añadir a la base de datos
     * @return devuelve el propietario que se ha añadido
     */
    override fun save(entity: Propietario): Propietario {
        logger.debug { "adding into database" }
        val sql = """INSERT INTO propietario values(?,?,?,?,?)""".trimIndent()

        database.connection.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, entity.dni)
                stm.setString(2, entity.nombre)
                stm.setString(3, entity.apellidos)
                stm.setString(4, entity.telefono)
                stm.setString(5, entity.email)

                stm.executeUpdate()
            }
        }
        return entity
    }

    /**
     * funcion que borra un propietario de la base de datos en base a su id
     * @author KevinMatute
     * @param id es el indentificador del propieterio, este se usara bara buscar el propietario y eliminarlo
     * @return true si se elimina correctamente el propietario, false si no se elimina correctamente
     */
    override fun deleteById(id: String): Boolean {
        logger.debug { "Se elimina un propietario con id: $id" }

        val res: Int
        database.connection.use {
            val sql = """
            DELETE FROM propietario WHERE dni = ?
        """.trimIndent()
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, id)
                res = stm.executeUpdate()
            }
        }
        return res >= 1
    }


    /**
     * función que elimina todos los propietarios de la base de datos
     * @author KevinMatute
     * @return true si se eliminan todos los propietarios, false si no se eliminan correctamente.
     */
    override fun deleteAll(): Boolean {
        logger.debug { "Se llama a database para borrar todos los propietarios: deleteAll" }
        var res: Int
        database.connection.use {
            val sql = """
            DELETE FROM propietario;""".trimIndent()
            it.prepareStatement(sql).use { stm ->
                res = stm.executeUpdate()
            }
            return res >= 0
        }
    }
}