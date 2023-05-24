package com.example.projectofinalitv.services.database

import com.example.gestionvehiculosconimagenes_kotlin.model.TipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.TipoVehiculo
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.Propietario
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.koin.core.component.KoinComponent
import org.koin.core.component.getScopeId
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DatabaseManagerTest: KoinComponent {

    private val databaseManager: DatabaseManager = DatabaseManager(ConfigApp())

    val propietarios = listOf<Propietario>(
        Propietario(
            dni = "12345678A",
            nombre = "Iván",
            apellidos = "Ronco Cebadera",
            telefono = "758374829",
            email = "ivan@gmail.com"
        ),
        Propietario(
            dni = "98765432Z",
            nombre = "Jia",
            apellidos = "Zheng",
            telefono = "893759402",
            email = "jia@gmail.com"
        )
    )

    val vehiculos = listOf<Vehiculo>(
        Vehiculo(
            id = 1,
            matricula = "AAAA345",
            marca = "Peugeot",
            modelo = "Gris",
            tipoMotor = TipoMotor.ELECTRICO,
            tipoVehiculo = TipoVehiculo.MOTOCICLETA,
            propietario = propietarios[0]
        ),
        Vehiculo(
            id = 2,
            matricula = "BBBB123",
            marca = "Toyota",
            modelo = "Amarillo",
            tipoMotor = TipoMotor.DIESEL,
            tipoVehiculo = TipoVehiculo.MOTOCICLETA,
            propietario = propietarios[1]
        )
    )

    @BeforeAll
    fun setUp(){
        //propietarios.forEach { databaseManager.insertPropietario(it) }
        databaseManager.resetearValorAutoIncrementDeLaTablaVehículo(1L)
        vehiculos.forEach{ databaseManager.insertVehiculo(it) }
    }

    @BeforeEach
    fun resetAutoIncrement(){
        databaseManager.resetearValorAutoIncrementDeLaTablaVehículo(1L)
    }

    @AfterAll
    fun tearAllDown(){
        //databaseManager.deleteAllPropietarios()
        //databaseManager.deleteAllVehiculos()
    }

    @Test
    fun selectAllVehiculos() {
        val vehiculosSelected = databaseManager.selectAllVehiculos()

        assertAll(
            { assertTrue(vehiculosSelected.size==2) },
            { assertEquals(vehiculosSelected[0], vehiculos[0]) },
            { assertEquals(vehiculosSelected[1], vehiculos[1]) }
        )
    }

    @Test
    fun selectVehiculoById() {
    }

    @Test
    fun insertVehiculo() {
    }

    @Test
    fun updateVehiculo() {
    }

    @Test
    fun deleteVehiculo() {
    }

    @Test
    fun deleteAllVehiculos() {
    }
}