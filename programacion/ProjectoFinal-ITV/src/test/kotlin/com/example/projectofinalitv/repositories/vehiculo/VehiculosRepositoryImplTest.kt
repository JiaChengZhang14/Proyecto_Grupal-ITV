package com.example.projectofinalitv.repositories.vehiculo

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.TipoMotor
import com.example.projectofinalitv.models.TipoVehiculo
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.repositories.propietario.PropietarioRepositoryImpl
import com.example.projectofinalitv.services.database.DatabaseManager
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class VehiculosRepositoryImplTest {

    private val configApp = ConfigApp().apply {
        APP_URL = "jdbc:mariadb://localhost:3306/empresaitvparatest?serverTimezone=UTC"
    }

    private val repositoryPropietario: PropietarioRepositoryImpl = PropietarioRepositoryImpl(DatabaseManager(configApp))

    private val repositoryVehiculo: VehiculosRepositoryImpl = VehiculosRepositoryImpl(DatabaseManager(configApp))

    private val propietarios = listOf(
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

    private val vehiculos = listOf(
        Vehiculo(
            id = 1,
            matricula = "CCCC345",
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
    fun setPropietariosUp(){
        propietarios.forEach {
            repositoryPropietario.save(it)
        }
    }

    @BeforeEach
    fun setUp(){
        repositoryVehiculo.resetearValorAutoIncrementDeLaTablaVehiculo(1)
        vehiculos.forEach {
            repositoryVehiculo.save(it)
        }
    }

    @AfterAll
    fun tearPropietarioDown(){
        repositoryPropietario.deleteAll()
    }

    @AfterEach
    fun tearDown(){
        repositoryVehiculo.deleteAll()
    }

    @Test
    fun getAll() {
        val vehiculosSelected = repositoryVehiculo.getAll()

        assertTrue(vehiculosSelected.size == 2)
        assertEquals(vehiculosSelected[0], vehiculos[0])
        assertEquals(vehiculosSelected[1], vehiculos[1])
    }

    @Test
    fun getById() {
        val id = 1L
        val selectedVehiculos = repositoryVehiculo.getById(id)

        assertTrue(selectedVehiculos!!.id == id)
        assertEquals(selectedVehiculos, vehiculos[0])
    }

    @Test
    fun getByIdButNotFound() {
        val id = -1L
        val selectedVehiculos = repositoryVehiculo.getById(id)

        assertEquals(selectedVehiculos, null)
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
        val savedVehiculo = repositoryVehiculo.save(vehiculo)
        val selectedVehiculos = repositoryVehiculo.getAll()

        assertTrue(savedVehiculo!!.id == newId)
        assertEquals(savedVehiculo, vehiculo.copy(id = newId))
        assertTrue(selectedVehiculos.size == 3)
        assertEquals(selectedVehiculos[2], savedVehiculo)
    }

    @Test
    fun update(){
        val id = vehiculos[0].id
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
        val updatedVehiculo = repositoryVehiculo.save(vehiculo)
        val selectedVehiculos = repositoryVehiculo.getAll()

        assertEquals(updatedVehiculo, vehiculo)
        assertTrue(selectedVehiculos.size == 2)
        assertEquals(selectedVehiculos[0], updatedVehiculo)
    }

    @Test
    fun deleteByIdCorrectly() {
        val idCorrecto = vehiculos[0].id

        val res = repositoryVehiculo.deleteById(idCorrecto)
        val selectedVehiculo = repositoryVehiculo.getAll()

        assertTrue(res)
        assertTrue(selectedVehiculo.size == 1)
        assertNotEquals(selectedVehiculo[0], vehiculos[0])
    }

    @Test
    fun deleteByIdIncorrectly() {
        val idIncorrecto = -1L

        val res = repositoryVehiculo.deleteById(idIncorrecto)
        val selectedVehiculo = repositoryVehiculo.getAll()

        assertFalse(res)
        assertTrue(selectedVehiculo.size == 2)
    }

    @Test
    fun deleteAllCorrectly() {
        val oldSelectedVehiculos = repositoryVehiculo.getAll()
        val res = repositoryVehiculo.deleteAll()
        val newSelectedVehiculos = repositoryVehiculo.getAll()

        assertTrue(res)
        assertTrue(oldSelectedVehiculos.size == 2)
        assertTrue(newSelectedVehiculos.size == 0)
    }

    @Test
    fun deleteAllIncorrectly() {
        repositoryVehiculo.deleteAll()
        val oldSelectedVehiculos = repositoryVehiculo.getAll()
        val res = repositoryVehiculo.deleteAll()
        val newSelectedVehiculos = repositoryVehiculo.getAll()

        assertFalse(res)
        assertTrue(oldSelectedVehiculos.size == 0)
        assertTrue(newSelectedVehiculos.size == 0)
    }
}