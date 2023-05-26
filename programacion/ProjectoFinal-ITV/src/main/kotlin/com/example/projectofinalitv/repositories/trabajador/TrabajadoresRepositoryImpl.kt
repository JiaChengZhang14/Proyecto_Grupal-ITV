package com.example.projectofinalitv.repositories.trabajador

import com.example.projectofinalitv.mapper.getApto
import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.models.Trabajador
import com.example.projectofinalitv.services.database.DatabaseManager
import com.example.projectofinalitv.utils.getEspecialidad
import com.example.projectofinalitv.utils.toLocalDate
import com.example.projectofinalitv.utils.toLocalDateTime
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

class TrabajadoresRepositoryImpl(
    private val database: DatabaseManager
): ITrabajadoresRepository {

    /**
     * funcion que obtiene todos los Trabajadores*@author KevinMatute & JiaCheng Zhang
     * @return una lista de los todos los trabajadores de la base de datos
    */
    override fun getAll(): List<Trabajador> {
        logger.debug { "" }
        var trabajadores = mutableListOf<Trabajador>()

        database.connection.use {
            val sqlTrabajadores = """
            SELECT * FROM trabajador;
        """.trimIndent()
            it.prepareStatement(sqlTrabajadores).use { stm ->
                val result = stm.executeQuery()
                while (result.next()) {
                    val id = result.getLong("id_trabajador")
                    trabajadores.add(
                        Trabajador(
                            idTrabajador = id,
                            idEstacion = result.getLong("id_itv"),
                            nombre = result.getString("nombre"),
                            telefono = result.getString("telefono"),
                            email = result.getString("email"),
                            nombreUsuario = result.getString("nombre_usuario"),
                            contraseñaUsuario = result.getString("contraseña_usuario"),
                            fechaContratacion = result.getString("fecha_contratacion").toLocalDate(),
                            especialidades = listOf(result.getString("especialidad").getEspecialidad()),
                            idResponsable = result.getLong("idResponsable"),
                            informes = getAllInformesByTrabajadorId(id)
                        )
                    )
                }
            }
        }
        return trabajadores
    }

    private fun getAllInformesByTrabajadorId(id: Long): List<Informe> {
        logger.debug { "Se consiguen todos los informes del trabajador de id: $id" }
        val informes = mutableListOf<Informe>()
        database.connection.use {
            val sql = "SELECT * FROM informe WHERE id_trabajador = ?;"

            it.prepareStatement(sql).use {stm ->
                var res = stm.executeQuery()
                while(res.next()){
                    informes.add(
                        Informe(
                            fechaFinal = res.getString("fecha_final").toLocalDateTime()!!,
                            fechaInicio = res.getString("fecha_inicio").toLocalDateTime()!!,
                            favorable = res.getString("favorable").getApto(),
                            frenado = res.getDouble("frenado"),
                            contaminacion = res.getDouble("contaminacion"),
                            interior = res.getString("interior").getApto(),
                            luces = res.getString("luces").getApto(),
                            vehiculo = database.getVehiculoById(res.getLong("id_vehiculo"))!!,
                            trabajadorId = id
                        )
                    )
                }
            }
        }
        return informes
    }
}