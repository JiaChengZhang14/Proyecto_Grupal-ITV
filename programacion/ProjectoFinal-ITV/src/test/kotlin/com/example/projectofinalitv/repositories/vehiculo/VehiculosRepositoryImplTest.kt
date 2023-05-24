package com.example.projectofinalitv.repositories.vehiculo

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.TipoMotor
import com.example.projectofinalitv.models.TipoVehiculo
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.services.database.DatabaseManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class VehiculosRepositoryImplTest {

    private val repository: VehiculosRepositoryImpl = VehiculosRepositoryImpl(DatabaseManager(ConfigApp()))

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

    @Test
    fun getAll() {
        whenever( database.selectAllVehiculos() ).thenReturn(vehiculos)
        val vehiculosSelected = repository.getAll()

        //Esto es una nota, acordaos de borrarme!!!!
        //Si pones el assertAll(), estalla, sería la hostía si conseguis poner un assertAll() y que funcione, pero no os rayeis mucho

        assertTrue(vehiculosSelected.size == 2)
        assertEquals(vehiculosSelected[0], vehiculos[0])
        assertEquals(vehiculosSelected[1], vehiculos[1])

        verify( database, times(1) ).selectAllVehiculos()
    }

    @Test
    fun getById() {
        val id = 1L
        whenever( database.selectVehiculoById(id) ).thenReturn(vehiculos[0])
        val vehiculoSelected = repository.getById(id)

        assertTrue(vehiculoSelected!!.id == id)
        assertEquals(vehiculoSelected, vehiculos[0])

        verify( database, times(1) ).selectVehiculoById(id)
    }

    @Test
    fun getByIdButNotFound() {
        val id = -1L
        whenever( database.selectVehiculoById(id) ).thenReturn(null)
        val vehiculoSelected = repository.getById(id)

        assertEquals(vehiculoSelected, null)

        verify( database, times(1) ).selectVehiculoById(id)
    }

    @Test
    fun create() {
        val id = -1L
        val newId = 3L
        val vehiculo =
            Vehiculo(
                id = id,
                matricula = "ZZZZ999",
                marca = "Peugeot",
                modelo = "Gris",
                tipoMotor = TipoMotor.OTRO,
                tipoVehiculo = TipoVehiculo.CAMION
            )
        whenever( database.selectVehiculoById(id) ).thenReturn(null)
        whenever( database.insertVehiculo(vehiculo) ).thenReturn(vehiculo.copy(id = newId))
        val vehiculoSelected = repository.save(vehiculo)

        assertTrue(vehiculoSelected!!.id == newId)
        assertEquals(vehiculoSelected, vehiculo.copy(id = newId))

        verify( database, times(1) ).selectVehiculoById(id)
        verify( database, times(1) ).insertVehiculo(vehiculo)
    }

    @Test
    fun update(){
        val id = 3L
        val vehiculo =
            Vehiculo(
                id = id,
                matricula = "ZZZZ999",
                marca = "Peugeot",
                modelo = "Gris",
                tipoMotor = TipoMotor.OTRO,
                tipoVehiculo = TipoVehiculo.CAMION
            )
        whenever( database.selectVehiculoById(id) ).thenReturn(vehiculo)
        whenever( database.insertVehiculo(vehiculo) ).thenReturn(vehiculo)
        val vehiculoSelected = repository.save(vehiculo)

        assertTrue(vehiculoSelected!!.id == id)
        assertEquals(vehiculoSelected, vehiculo)

        verify( database, times(1) ).selectVehiculoById(id)
        verify( database, times(1) ).insertVehiculo(vehiculo)
    }

    @Test
    fun deleteByIdCorrectly() {
        val idCorrecto = 1L
        whenever( database.deleteVehiculo(idCorrecto) ).thenReturn(true)

        val res = repository.deleteById(idCorrecto)

        assertTrue(res)

        verify( database, times(1) ).deleteVehiculo(idCorrecto)
    }

    @Test
    fun deleteByIdIncorrectly() {
        val idIncorrecto = -1L
        whenever( database.deleteVehiculo(idIncorrecto) ).thenReturn(false)

        val res = repository.deleteById(idIncorrecto)

        assertFalse(res)

        verify( database, times(1) ).deleteVehiculo(idIncorrecto)
    }

    @Test
    fun deleteAllCorrectly() {
        whenever( database.deleteAllVehiculos() ).thenReturn(true)

        val res = repository.deleteAll()

        assertTrue(res)

        verify( database, times(1) ).deleteAllVehiculos()
    }

    @Test
    fun deleteAllIncorrectly() {
        whenever( database.deleteAllVehiculos() ).thenReturn(false)

        val res = repository.deleteAll()

        assertFalse(res)

        verify( database, times(1) ).deleteAllVehiculos()
    }
}