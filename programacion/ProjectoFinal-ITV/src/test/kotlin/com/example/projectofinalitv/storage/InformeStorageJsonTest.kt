package com.example.projectofinalitv.storage

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.*
import com.example.projectofinalitv.services.storage.informe.InformeStorageJson
import com.example.projectofinalitv.utils.toLocalDate
import com.example.projectofinalitv.utils.toLocalDateTime
import com.github.michaelbull.result.Ok
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class InformeStorageJsonTest {
    private val configApp = ConfigApp()
    private var filePath = configApp.APP_FILE_PATH +File.separator+"informes"+File.separator+ "informesTest"

    private val singleFile = File(filePath + File.separator+"informe1.json")
    private val multiFile = File(filePath + File.separator + "informes.json")
    private val storageJson =  InformeStorageJson(configApp).apply {
        filePath = filePath+File.separator+"informesTest"
    }


    private val propietarios = listOf(
        Propietario(
            dni = "12345678A",
            nombre = "Juan",
            apellidos = "Pérez",
            telefono = "123456789",
            email = "juan@example.com"
        ),
        Propietario(
            dni = "98765432B",
            nombre = "María",
            apellidos = "López",
            telefono = "987654321",
            email = "maria@example.com"
        )
    )

    private val informe = Informe(
        1,
        "2023-05-01 09:00:00".toLocalDateTime()!!,
        "2023-05-01 10:30:00".toLocalDateTime()!!,
        IsApto.APTO,
        8.5,
        35.0,
        IsApto.APTO,
        IsApto.APTO,
        Vehiculo(
            id = 1,
            matricula = "FBCD123",
            marca = "Ford",
            modelo = "Focus",
            fechaMatriculacion = "2020-01-01".toLocalDate(),
            fechaUltimaRevision = "2023-05-01 09:00:00".toLocalDateTime(),
            tipoMotor = TipoMotor.GASOLINA,
            tipoVehiculo = TipoVehiculo.TURISMO,
            propietario = propietarios[0]
        ),
        1
    )
    private val informe2 = Informe(
        2,
        "2023-05-02 14:00:00".toLocalDateTime()!!,
        "2023-05-02 15:30:00".toLocalDateTime()!!,
        IsApto.NO_APTO,
        6.0,
        40.0,
        IsApto.NO_APTO,
        IsApto.APTO,
        Vehiculo(
            id = 2,
            matricula = "WXYZ987",
            marca = "Volkswagen",
            modelo = "Golf",
            fechaMatriculacion = "2018-06-15".toLocalDate(),
            fechaUltimaRevision = "2023-05-02 14:00:00".toLocalDateTime(),
            tipoMotor = TipoMotor.DIESEL,
            tipoVehiculo = TipoVehiculo.TURISMO,
            propietario = propietarios[1]
        ),
        2
    )


        @BeforeAll
        fun setUp() {
            if (singleFile.exists()){
                singleFile.delete()
            }
            if (multiFile.exists()){
                multiFile.delete()
            }
        }


    @Test
    fun exportSingleData() {
        println(singleFile)
        val res = storageJson.exportSingleData(informe)
        assertEquals(Ok(informe), res)
        assertTrue(singleFile.exists())

    }

    @Test
    fun exportMultipleData() {
        val informes = listOf(informe, informe2)
        val res = storageJson.exportMultipleData(informes)
        assertEquals(Ok(informes), res)
        assertTrue(multiFile.exists())
    }

}