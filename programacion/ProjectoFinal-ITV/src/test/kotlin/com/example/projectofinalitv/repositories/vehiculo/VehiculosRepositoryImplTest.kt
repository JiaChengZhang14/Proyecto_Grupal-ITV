package com.example.projectofinalitv.repositories.vehiculo

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.TipoMotor
import com.example.projectofinalitv.models.TipoVehiculo
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.repositories.propietario.PropietarioRepositoryImpl
import com.example.projectofinalitv.services.database.DatabaseManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class VehiculosRepositoryImplTest {

    private val repositoryVehiculo: PropietarioRepositoryImpl = PropietarioRepositoryImpl(DatabaseManager(ConfigApp()))

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

    @BeforeAll
    fun setUp(){
        propietarios.forEach {
            repositoryVehiculo.save(it)
        }
    }

    @Test
    fun getAll() {
        val vehiculosSelected = repository.getAll()

        //Esto es una nota, acordaos de borrarme!!!!
        //Si pones el assertAll(), estalla, sería la hostía si conseguis poner un assertAll() y que funcione, pero no os rayeis mucho

        assertTrue(vehiculosSelected.size == 2)
        assertEquals(vehiculosSelected[0], vehiculos[0])
        assertEquals(vehiculosSelected[1], vehiculos[1])
    }

    @Test
    fun getById() {
        val id = 1L
        val vehiculoSelected = repository.getById(id)

        assertTrue(vehiculoSelected!!.id == id)
        assertEquals(vehiculoSelected, vehiculos[0])
    }

    @Test
    fun getByIdButNotFound() {
        val id = -1L
        val vehiculoSelected = repository.getById(id)

        assertEquals(vehiculoSelected, null)

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
                tipoVehiculo = TipoVehiculo.CAMION,
                propietario = propietarios[0]
            )
        val vehiculoSelected = repository.save(vehiculo)

        assertTrue(vehiculoSelected!!.id == newId)
        assertEquals(vehiculoSelected, vehiculo.copy(id = newId))
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
                tipoVehiculo = TipoVehiculo.CAMION,
                propietario = propietarios[0]
            )
        val vehiculoSelected = repository.save(vehiculo)

        assertTrue(vehiculoSelected!!.id == id)
        assertEquals(vehiculoSelected, vehiculo)
    }

    @Test
    fun deleteByIdCorrectly() {
        val idCorrecto = 1L

        val res = repository.deleteById(idCorrecto)

        assertTrue(res)
    }

    @Test
    fun deleteByIdIncorrectly() {
        val idIncorrecto = -1L

        val res = repository.deleteById(idIncorrecto)

        assertFalse(res)
    }

    @Test
    fun deleteAllCorrectly() {
        val res = repository.deleteAll()

        assertTrue(res)
    }

    @Test
    fun deleteAllIncorrectly() {
        val res = repository.deleteAll()

        assertFalse(res)
    }
}