package com.example.projectofinalitv.repositories.trabajador

import com.example.projectofinalitv.models.Trabajador
import com.example.projectofinalitv.services.database.DatabaseManager
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

class TrabajadoresRepositoryImpl(
    private val database: DatabaseManager
): ITrabajadoresRepository {

    override fun getAll(): List<Trabajador> {
        TODO("Not yet implemented")
    }
}