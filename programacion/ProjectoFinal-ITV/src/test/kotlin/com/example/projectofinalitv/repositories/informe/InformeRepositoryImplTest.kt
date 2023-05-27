package com.example.projectofinalitv.repositories.informe

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.*
import com.example.projectofinalitv.repositories.propietario.PropietarioRepositoryImpl
import com.example.projectofinalitv.repositories.vehiculo.VehiculosRepositoryImpl
import com.example.projectofinalitv.services.database.DatabaseManager
import com.example.projectofinalitv.utils.toLocalDate
import com.example.projectofinalitv.utils.toLocalDateTime
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class InformeRepositoryImplTest {

    private val configApp = ConfigApp().apply {
        APP_URL = "jdbc:mariadb://localhost:3306/empresaitvparatest?serverTimezone=UTC"
    }
    private val repositoryInforme = InformeRepositoryImpl(DatabaseManager(configApp))
    private val vehiculoRepository = VehiculosRepositoryImpl(DatabaseManager(configApp))
    private val propietarioRepository = PropietarioRepositoryImpl(DatabaseManager((configApp)))

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

    private val vehiculos = listOf(
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
        vehiculos[0],
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
        vehiculos[1],
        2
    )

    @BeforeEach
    fun insertarInformes() {
        repositoryInforme.resetearValorAutoIncrementDeLaTablaVehiculo(1)
        repositoryInforme.saveInforme(informe)

    }

    @BeforeEach
    fun insertVehiculos() {
        vehiculoRepository.resetearValorAutoIncrementDeLaTablaVehiculo(1)
        vehiculos.forEach {
            vehiculoRepository.save(it)
        }
    }

    @BeforeEach
    fun insertarPropietarios() {
        propietarios.forEach {
            propietarioRepository.save(it)
        }
    }


    @AfterEach
    fun eliminarVehiculos() {
        vehiculoRepository.deleteAll()
    }

    @AfterEach
    fun eliminarPropietarios() {
        propietarioRepository.deleteAll()
    }

    @AfterEach
    fun tearDownAfterAll() {
        repositoryInforme.deleteAllInformes()
    }



    @Test
    fun getInforme() {
        val res = repositoryInforme.getInforme(1)
        assertEquals(informe, res)
    }

    @Test
    fun saveInforme() {
        val res = repositoryInforme.saveInforme(informe)
        assertEquals(informe, res)
    }

    @Test
    fun deleteInforme() {
        val res = repositoryInforme.deleteInforme(1)
        assertEquals(true, res)
        val res2 = repositoryInforme.deleteInforme(2)
        assertEquals(false, res2)
    }

    @Test
    fun deleteAllInformes() {
        val res = repositoryInforme.deleteAllInformes()
        assertEquals(true, res)

    }
}
