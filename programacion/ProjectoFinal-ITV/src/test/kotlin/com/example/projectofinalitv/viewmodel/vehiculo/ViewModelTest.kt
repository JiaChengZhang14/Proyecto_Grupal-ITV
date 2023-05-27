package com.example.projectofinalitv.viewmodel.vehiculo
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.TipoMotor
import com.example.projectofinalitv.models.TipoVehiculo
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.repositories.propietario.IPropietarioRepository
import com.example.projectofinalitv.repositories.trabajador.ITrabajadoresRepository
import com.example.projectofinalitv.repositories.vehiculo.IVehiculosRepository
import com.example.projectofinalitv.services.storage.informe.IInformeMultipleDataStorage
import com.example.projectofinalitv.services.storage.informe.IInformeSingleDataStorage
import com.example.projectofinalitv.services.storage.trabajador.ITrabajadorStorage
import com.example.projectofinalitv.viewmodel.ViewModel
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
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
    }
}