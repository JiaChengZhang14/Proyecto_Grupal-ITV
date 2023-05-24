package com.example.projectofinalitv.repositories.propietario

import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.services.database.DatabaseManager
import mu.KotlinLogging

class PropietarioRepositoryImpl(
    private val database: DatabaseManager
): IPropietarioRepository {

    private val logger = KotlinLogging.logger {  }

    /**
     * @author JiaCheng Zhang, Kevin Matute
     * @return Devuelve una lista con todos los propietarios de la base de datos.
     */
    override fun getAll(): List<Propietario> {
        val propietarios = mutableListOf<Propietario>()
        val sql = """SELECT * FROM vehiculo""".trimIndent()
        database.connection.use {
            it.prepareStatement(sql).use { stm ->
                val res = stm.executeQuery()
                while(res.next()){
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
        logger.debug { "getting all propietarios by id" }
        val sql = """SELECT * FROM propietario WHERE dni = ?""".trimIndent()
        var propietario: Propietario? = null
        database.connection.use {

            it.prepareStatement(sql).use { stm ->

                stm.setString(1, id)

                val res = stm.executeQuery()
                while (res.next()) {
                    propietario =
                        Propietario(
                            dni = res.getString("dni"),
                            nombre = res.getString("nombre"),
                            apellidos = res.getString("apellidos"),
                            telefono = res.getString("telefono"),
                            email = res.getString("email"),
                        )
                }
            }
        }
        return propietario
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

    override fun deleteById(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteAll(): Boolean {
        TODO("Not yet implemented")
    }
}