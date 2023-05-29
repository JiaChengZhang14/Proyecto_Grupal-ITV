package com.example.projectofinalitv.repositories.trabajador

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.models.*
import com.example.projectofinalitv.repositories.informe.InformeRepositoryImpl
import com.example.projectofinalitv.repositories.propietario.PropietarioRepositoryImpl
import com.example.projectofinalitv.repositories.vehiculo.VehiculosRepositoryImpl
import com.example.projectofinalitv.services.database.DatabaseManager
import com.example.projectofinalitv.utils.toLocalDate
import com.example.projectofinalitv.utils.toLocalDateTime
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate
import javax.xml.crypto.Data

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TrabajadoresRepositoryImplTest {
    private val configApp = ConfigApp().apply {
        APP_URL = "jdbc:mariadb://localhost:3306/empresaitvparatest?serverTimezone=UTC"
    }

    private val repository = TrabajadoresRepositoryImpl(DatabaseManager(configApp))
    private val vehiculoRepository =  VehiculosRepositoryImpl(DatabaseManager(configApp))
    private val propietarioRepository = PropietarioRepositoryImpl(DatabaseManager((configApp)))
    private val informeRepository = InformeRepositoryImpl(DatabaseManager(configApp))

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
    private val trabajadores = listOf(
        Trabajador(
            1,
            1,
            "Juan Pérez",
            "123456789",
            "juan@example.com",
            "juancito",
            "contraseña1",
            "2023-05-27".toLocalDate(),
            listOf(Especialidad.ADMINISTRACION),
            1,
            listOf(informe)
        ),
        Trabajador(
            2,
            1,
            "María López",
            "987654321",
            "maria@example.com",
            "marialo",
            "contraseña2",
            "2023-05-27".toLocalDate(),
            listOf(Especialidad.MECANICA),
            1,
            listOf(informe2)
        )

    )


    @BeforeAll
    fun insertVehiculos(){
        vehiculoRepository.resetearValorAutoIncrementDeLaTablaVehiculo(1)
        vehiculos.forEach {
            vehiculoRepository.save(it)
        }
    }

    @BeforeAll
    fun insertarPropietarios(){
        propietarios.forEach {
            propietarioRepository.save(it)
        }
    }
    @BeforeAll
    fun insertarInformes(){
        informeRepository.resetearValorAutoIncrementDeLaTablaVehiculo(1)
        val informes = listOf(informe, informe2)
        informes.forEach {
            informeRepository.saveInforme(it)
        }
    }

    @AfterAll
    fun eliminarVehiculos(){
        vehiculoRepository.deleteAll()
    }
    @AfterAll
    fun eliminarPropietarios(){
        propietarioRepository.deleteAll()
    }
    @AfterAll
    fun eliminarInformes(){
        informeRepository.deleteAllInformes()
    }

    @Test
    fun getAll() {
        val res = repository.getAll()
        assertEquals(trabajadores[0], res[0])
        assertEquals(trabajadores[1], res[1])
    }

    @Test
    fun getAllInformesByTrabajadorId() {
        val res = repository.getAllInformesByTrabajadorId(1)
        val res2 = repository.getAllInformesByTrabajadorId(2)
        assertEquals(listOf(informe),res)
        assertEquals(listOf(informe2), res2)

    }
}