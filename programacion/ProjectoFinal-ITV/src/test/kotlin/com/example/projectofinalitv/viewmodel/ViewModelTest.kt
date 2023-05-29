package com.example.projectofinalitv.viewmodel

import com.example.projectofinalitv.mapper.toInformeReference
import com.example.projectofinalitv.models.*
import com.example.projectofinalitv.repositories.informe.IInformeRepository
import com.example.projectofinalitv.repositories.propietario.IPropietarioRepository
import com.example.projectofinalitv.repositories.trabajador.ITrabajadoresRepository
import com.example.projectofinalitv.repositories.vehiculo.IVehiculosRepository
import com.example.projectofinalitv.services.storage.informe.IInformeMultipleDataStorage
import com.example.projectofinalitv.services.storage.informe.IInformeSingleDataStorage
import com.example.projectofinalitv.services.storage.informe.InformeStorageJson
import com.example.projectofinalitv.services.storage.trabajador.ITrabajadorStorage
import com.example.projectofinalitv.utils.toLocalDate
import com.example.projectofinalitv.utils.toLocalDateTime
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.internal.verification.Times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
internal class ViewModelTest {

    @Mock
    private lateinit var vehiculosRepository: IVehiculosRepository

    @Mock
    private lateinit var trabajadoresRepository: ITrabajadoresRepository

    @Mock
    private lateinit var propietariosRepository: IPropietarioRepository

    @Mock
    lateinit var informesRepository: IInformeRepository

    @Mock
    lateinit var informeStorage: InformeStorageJson

    @Mock
    private lateinit var informesStorageJson: IInformeMultipleDataStorage

    @Mock
    private lateinit var informeStorageHtml: IInformeSingleDataStorage

    @Mock
    private lateinit var trabajadoresStorageCsv: ITrabajadorStorage

    @InjectMocks
    private lateinit var viewModel: ViewModel


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
            tipoVehiculo = TipoVehiculo.TURISMO,
            propietario = propietarios[1]
        )
    )

    private val informe = Informe(
        1,
        "2023-05-01 09:00:00".toLocalDateTime()!!,
        "2023-05-01 09:30:00".toLocalDateTime()!!,
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
        "2023-05-02 14:30:00".toLocalDateTime()!!,
        IsApto.NO_APTO,
        6.0,
        40.0,
        IsApto.NO_APTO,
        IsApto.APTO,
        vehiculos[1],
        2
    )

    val informes = listOf(informe, informe2)

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

    //////////////////////////////////////////////Vehículos\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    fun updateSharedStateVehiculo(){
        viewModel.updateSharedStateVehiculo(vehiculos)

        assertTrue(viewModel.state.value.vehiculos.size == 2)
        assertTrue(viewModel.state.value.vehiculos[0] == vehiculos[0])
        assertTrue(viewModel.state.value.vehiculos[1] == vehiculos[1])
    }

    @Test
    fun saveVehiculo() {
        val vehiculo = vehiculos[0]
        whenever( vehiculosRepository.save(vehiculo.copy(id = Vehiculo.VEHICULO_ID)) ).thenReturn(vehiculo)

        val insertedVehiculo = viewModel.saveVehiculo(vehiculo)

        assertTrue(insertedVehiculo.get() == vehiculo)

        verify(vehiculosRepository, Times(1)).save(vehiculo.copy(id = Vehiculo.VEHICULO_ID))
    }

    @Test
    fun updateVehiculo() {
        val vehiculo = vehiculos[0].copy(marca = "Citroen")
        whenever(vehiculosRepository.save(vehiculo)).thenReturn(vehiculo)

        val updatedVehiculo = viewModel.updateVehiculo(vehiculo)

        assertTrue(updatedVehiculo.get() == vehiculo)

        verify(vehiculosRepository, Times(1)).save(vehiculo)
    }

    @Test
    fun updateVehiculoButDidntChanged() {
        viewModel.updateSharedStateVehiculo(vehiculos)

        val vehiculo = vehiculos[0]

        val updatedVehiculo = viewModel.updateVehiculo(vehiculo)

        assertEquals(updatedVehiculo.getError()!!.message, "No has cambiado ningún dato al editar el vehículo de id: ${vehiculo.id}")

        //No he pasado por el save() ya que devuelvo el error antes
        verify(vehiculosRepository, Times(0)).save(vehiculo)
    }

    @Test
    fun deleteVehiculo() {
        viewModel.updateSharedStateVehiculo(vehiculos)

        val id = vehiculos[0].id
        whenever(vehiculosRepository.deleteById(id)).thenReturn(true)

        val res = viewModel.deleteVehiculo(id)

        assertTrue(res.get()!!)
        assertTrue(viewModel.state.value.vehiculos.size == 1)

        verify(vehiculosRepository, Times(1)).deleteById(id)
    }

    @Test
    fun deleteVehiculoButNotFound() {
        val id = -1L
        whenever(vehiculosRepository.deleteById(id)).thenReturn(false)

        val res = viewModel.deleteVehiculo(id)

        assertEquals(res.getError()!!.message, "El vehículo de id: $id, no fue encontrado")

        verify(vehiculosRepository, Times(1)).deleteById(id)
    }

    @Test
    fun filtrarTablaVehiculosByMatricula() {
        viewModel.updateSharedStateVehiculo(vehiculos)

        val matricula = vehiculos[0].matricula
        val filteredVehiculos = viewModel.filtrarTablaVehiculos(TipoMotor.CUALQUIERA, TipoVehiculo.CUALQUIERA, matricula)

        assertTrue(filteredVehiculos.size == 1)
        assertEquals(filteredVehiculos[0], vehiculos[0])
    }

    @Test
    fun filtrarTablaVehiculosByTipoMotor() {
        viewModel.updateSharedStateVehiculo(vehiculos)

        val tipoMotor = vehiculos[0].tipoMotor
        val filteredVehiculos = viewModel.filtrarTablaVehiculos(tipoMotor, TipoVehiculo.CUALQUIERA, "")

        assertTrue(filteredVehiculos.size == 1)
        assertEquals(filteredVehiculos[0], vehiculos[0])
    }

    @Test
    fun filtrarTablaVehiculosByTipoVehiculo() {
        viewModel.updateSharedStateVehiculo(vehiculos)

        val tipoVehiculo = vehiculos[0].tipoVehiculo
        val filteredVehiculos = viewModel.filtrarTablaVehiculos(TipoMotor.CUALQUIERA, tipoVehiculo, "")

        assertTrue(filteredVehiculos.size == 1)
        assertEquals(filteredVehiculos[0], vehiculos[0])
    }

    //////////////////////////////////////////////Propietarios\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    fun getPropietarioForVehiculo() {
        viewModel.state.value = viewModel.state.value.copy(propietarios = propietarios)
        val res = viewModel.getPropietarioForVehiculo(propietarios[0].dni)
        assertEquals(propietarios[0], res)
    }

    @Test
    fun savePropietarioTest(){
        whenever(propietariosRepository.save(propietarios[0])).thenReturn(propietarios[0])
        val res =  viewModel.savePropietario(propietarios[0])
        assertEquals(Ok(propietarios[0]), res)
    }

    @Test
    fun updatePropietarioTest(){
        whenever(propietariosRepository.save(propietarios[0])).thenReturn(propietarios[0])
        val res = viewModel.updatePropietario(propietarios[0])
        assertEquals(Ok(propietarios[0]), res)
    }

    @Test
    fun deletePropietarioTestCorrecto(){
        whenever(propietariosRepository.deleteById(propietarios[0].dni)).thenReturn(true)
        val res = viewModel.deletePropietario(propietarios[0].dni)
        assertEquals(Ok(true), res)
    }

    @Test
    fun deletePropietarioTestIncorrecto(){
        whenever(propietariosRepository.deleteById(propietarios[0].dni)).thenReturn(false)
        val res = viewModel.deletePropietario(propietarios[0].dni)
        assertEquals(res.getError()!!.message, "El propietario de dni: ${propietarios[0].dni}, no fue encontrado")
    }

    @Test
    fun filtrarTablePropietariosTest(){
        viewModel.state.value = viewModel.state.value.copy(propietarios = propietarios)
        val res = viewModel.filtrarTablaPropietarios(propietarios[0].dni, propietarios[0].nombre)
        val res2 = viewModel.filtrarTablaPropietarios(propietarios[1].dni, propietarios[1].nombre)
        val res3 = viewModel.filtrarTablaPropietarios("", "")
        assertEquals(listOf(propietarios[0]), res)
        assertEquals(listOf(propietarios[1]), res2)
        assertEquals(propietarios, res3)
    }

    ////////////////////////////////////////////////INFORMES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    fun filtrarTablaInformes(){
        viewModel.state.value = viewModel.state.value.copy(trabajadores = trabajadores)
        val res = viewModel.filtrarTablaInformes(informe.matricula, informe.fechaInicio.toString(), enumValueOf(informe.tipoVehiculo))
        val res2 = viewModel.filtrarTablaInformes(informe2.matricula, informe2.fechaInicio.toString(), enumValueOf(informe2.tipoVehiculo))

        assertEquals(listOf(informe), res)
        assertEquals(listOf(informe2), res2)
    }


    @Test
    fun toListaInformesTest(){
        viewModel.state.value = viewModel.state.value.copy(trabajadores = trabajadores)
        val res = viewModel.toListaInformes()
        assertEquals(informes, res)
    }

    @Test
    fun deleteInformeTestCorrecto(){
        viewModel.state.value = viewModel.state.value.copy(trabajadores = trabajadores)
        whenever(informesRepository.deleteInforme(informe.idInforme)).thenReturn(true)
        val res = viewModel.deleteInforme(informe.idInforme)
        assertEquals(Ok(true), res)
    }


    @Test
    fun deleteInformeIncorrecto(){
        viewModel.state.value = viewModel.state.value.copy(trabajadores = trabajadores)
        whenever(informesRepository.deleteInforme(informe.idInforme)).thenReturn(false)
        val res = viewModel.deleteInforme(informe.idInforme)
        assertEquals(res.getError()!!.message, "No se ha encontrado al informe de id: ${informe.idInforme}")
    }

    @Test
    fun createInformeTest(){
        whenever(informesRepository.saveInforme(informe)).thenReturn(informe)
        val res = viewModel.createInforme(informe)
        assertEquals(Ok(informe), res)
    }

    @Test
    fun updateInformeTest(){
        viewModel.state.value = viewModel.state.value.copy(informeReference = informe2.copy(
            favorable = IsApto.APTO
        ).toInformeReference())
        whenever(informesRepository.saveInforme(informe2)).thenReturn(informe2)
        val res = viewModel.updateInforme(informe2)
        assertEquals(Ok(informe2), res)
    }



}

