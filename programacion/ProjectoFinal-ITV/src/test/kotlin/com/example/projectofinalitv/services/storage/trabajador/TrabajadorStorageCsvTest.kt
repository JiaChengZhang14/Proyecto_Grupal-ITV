package com.example.projectofinalitv.services.storage.trabajador

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.Especialidad
import com.example.projectofinalitv.models.Trabajador
import com.example.projectofinalitv.services.storage.informe.InformeStorageJson
import com.github.michaelbull.result.Ok
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TrabajadorStorageCsvTest {
    private val configApp = ConfigApp()
    private var filePath = configApp.APP_FILE_PATH + File.separator + "trabajadores"
    private val file = File(filePath + File.separator + "trabajador.csv")
    private val storage = TrabajadorStorageCsv(configApp)

    private val trabajadores = listOf(
        Trabajador(
            1,
            1,
            "Juan Pérez",
            "123456789",
            "juan@example.com",
            "juancito",
            "contraseña1",
            LocalDate.now(),
            listOf(Especialidad.ADMINISTRACION),
            1,
            listOf()
        ),
        Trabajador(
            2,
            1,
            "María López",
            "987654321",
            "maria@example.com",
            "marialo",
            "contraseña2",
            LocalDate.now(),
            listOf(Especialidad.MECANICA),
            1,
            listOf()
        )

    )


    @BeforeAll
    fun setUp() {
        if (file.exists()){
            file.delete()
        }
    }


    @Test
    fun exportMultipleData() {
        val res = storage.exportMultipleData(trabajadores)
        assertEquals(Ok(trabajadores), res)
        assertTrue(file.exists())
    }
}