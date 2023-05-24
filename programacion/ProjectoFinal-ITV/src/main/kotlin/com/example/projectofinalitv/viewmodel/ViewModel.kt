package com.example.projectofinalitv.viewmodel

import com.example.projectofinalitv.error.VehiculoError
import com.example.projectofinalitv.mapper.toPropietario
import com.example.projectofinalitv.mapper.toPropietarioReference
import com.example.projectofinalitv.mapper.toVehiculo
import com.example.projectofinalitv.mapper.toVehiculoReference
import com.example.projectofinalitv.models.*
import com.example.projectofinalitv.repositories.propietario.IPropietarioRepository
import com.example.projectofinalitv.repositories.trabajador.ITrabajadoresRepository
import com.example.projectofinalitv.repositories.vehiculo.IVehiculosRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import javafx.beans.property.SimpleObjectProperty
import mu.KotlinLogging
import com.example.projectofinalitv.validator.validate
import java.time.LocalDate
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {  }

class ViewModel(
    private val vehiculosRepository: IVehiculosRepository,
    private val trabajadoresRepository: ITrabajadoresRepository,
    private val propietariosRepository: IPropietarioRepository
) {

    val state: SimpleObjectProperty<SharedState> = SimpleObjectProperty(SharedState())

    init {
        state.value = state.value.copy(
            vehiculos = vehiculosRepository.getAll()
        )
    }

    /**
     * función que permite asignar el tipo de operación que se está realizando
     * @author IvanRoncoCebadera
     * @param tipoOperacion es el tipo de operación que se establecerá en el SharedState
     */
    fun setTipoOperacion(tipoOperacion: TipoOperacion) {
        logger.debug { "Se cambia el tipo de operación que se va a realizar" }
        if(tipoOperacion != state.value.tipoOperacion){
            state.value = state.value.copy(
                tipoOperacion = tipoOperacion
            )
        }
    }

    /////////////////////////////////////Vehiculos////////////////////////////////////////////////////////////

    /**
     * función que actualiza la parte del estado referente a los vehiculos
     * @author IvanRoncoCebadera
     * @param vehiculos la lista de vehículos con la que actualizaremos el SharedState
     */
    private fun updateSharedStateVehiculo(vehiculos: List<Vehiculo>) {
        logger.debug { "Actualizamos el estado compartido tras la operación que modificó la base de datos" }
        state.value = state.value.copy(
            vehiculoReference = VehiculoReference(),
            vehiculos = vehiculos
        )
    }

    /**
     * función que llama al repositorio de vehículos para guardar un nuevo vehículo
     * @author IvanRoncoCebadera
     * @param vehiculo es el vehículo que se desea guardar
     * @return si todo sale bien, el vehículo guardado, si algo sale mal, el error que haya ocurrido
     */
    fun saveVehiculo(vehiculo: Vehiculo): Result<Vehiculo, VehiculoError> {
        logger.debug { "Se llama al repositorio de vehiculos para añadir un nuevo vehículo" }
        //Aplicar aquí un validator de que la matricula no se repita y cosas por el estilo, el validado de campos se hace en el controlador
        return vehiculo.validate(state.value.vehiculos).andThen {
            updateSharedStateVehiculo(state.value.vehiculos.filter { it.id == vehiculo.id } + vehiculo)
            Ok(vehiculosRepository.save(vehiculo))
        }
    }

    /**
     * función que llama al repositorio de vehículos para actualizar un vehículo
     * @author IvanRoncoCebadera
     * @param vehiculo es el vehículo que se desea actualizar
     * @return si todo sale bien, el vehículo actualizado, si algo sale mal, el error que haya ocurrido
     */
    fun updateVehiculo(vehiculo: Vehiculo): Result<Vehiculo, VehiculoError> {
        logger.debug { "Se llama al repositorio de vehiculos para actualizar un vehículo" }

        return vehiculo.validate(state.value.vehiculos).andThen {
            //Si no se ha editado nigún cambio, no se guarda
            if(vehiculo == state.value.vehiculoReference.toVehiculo(state.value.propietarioReference.toPropietario())){
                return Err(VehiculoError.SameDataUpdate(vehiculo.id))
            }

            updateSharedStateVehiculo(state.value.vehiculos.filter { it.id == vehiculo.id } + vehiculo)
            Ok(vehiculosRepository.save(vehiculo))
        }
    }

    /**
     * función que llama al repositorio de vehículos para borrar un vehículo
     * @author IvanRoncoCebadera
     * @return si todo sale bien, unit, si algo sale mal, el error que haya ocurrido
     */
    fun deleteVehiculo(): Result<Unit, VehiculoError> {
        logger.debug { "Intentamos eliminar el vehículo actualmente seleccionado" }

        val vehiculo = state.value.vehiculoReference

        if(!vehiculosRepository.deleteById(vehiculo.id)){
            return Err(VehiculoError.NotFound(vehiculo.matricula))
        }

        updateSharedStateVehiculo(state.value.vehiculos.filter { it.id != vehiculo.id })
        return Ok(Unit)
    }

    /**
     * función que sirve para filtrar los datos que se encuentran visibles en la tabla de vehículos
     * @author IvanRoncoCebadera
     * @param tipoMotor es el tipo de motor que se usará para filtar
     * @param tipoVehiculo es el tipo de vehiculo que se usará para filtar
     * @param matricula es la matricula
     * @return devuelve la lista de vehiculos a representar, filtrada como se ha pedido
     */
    fun filtrarTablaVehiculos(tipoMotor: TipoMotor, tipoVehiculo: TipoVehiculo, matricula: String): List<Vehiculo> {
        logger.debug { "Filtramos la tabla de vehiculos según el tipo de motor: $tipoMotor, el tipo de vehículo: $tipoVehiculo y la marca-modelo: $matricula" }
        return state.value.vehiculos
            .filter {
                it.matricula.lowercase().contains(matricula.lowercase())
            }
            .filter {
                if (tipoMotor == TipoMotor.CUALQUIERA) {
                    true
                } else {
                    it.tipoMotor == tipoMotor
                }
            }
            .filter {
                if (tipoVehiculo == TipoVehiculo.CUALQUIERA) {
                    true
                } else {
                    it.tipoVehiculo == tipoVehiculo
                }
            }
    }

    /**
     * función que actualiza los datos a mostrar del vehículo, según el que se haya seleccionado en la tabla
     * @author IvanRoncoCebadera
     * @param vehiculo es el vehículo seleccionado en la tabla de vehículos
     */
    fun onSelectedUpdateVehiculoReference(vehiculo: Vehiculo) {
        logger.debug { "Se actualizan los datos del vehiculo a mostar según la selección en la tabla" }
        state.value = state.value.copy(
            vehiculoReference = vehiculo.toVehiculoReference(),
            propietarioReference = vehiculo.propietario.toPropietarioReference()
        )
    }

    /////////////////////////////////////Propietarios////////////////////////////////////////////////////////////
    /**
     * función que busca el propietario asociado al vehículo que está siendo creado o editado
     * @author IvanRoncoCebadera
     * @return el proepietario si existe, o nulo si no existe
     */
    fun getPropietarioForVehiculo(dniPropietario: String): Propietario{
        logger.debug { "Se busca el propietario asociado al vehículo en creacion o edición" }
        return state.value.propietarios.filter { it.dni == dniPropietario}.get(0)
    }

}


data class SharedState(
    val trabajadores: List<Trabajador> = listOf(),
    val vehiculos: List<Vehiculo> = listOf(),
    val propietarios: List<Propietario> = listOf(),
    val trabajadorReference: TrabajadorReference = TrabajadorReference(),
    val vehiculoReference: VehiculoReference = VehiculoReference(),
    val propietarioReference: PropietarioReference = PropietarioReference(),
    val citaReference: Pair<LocalDateTime, LocalDateTime> = Pair(LocalDateTime.now(), LocalDateTime.now()),
    val tipoOperacion: TipoOperacion = TipoOperacion.AÑADIR,
    val tiposDeVehiculos: List<String> = TipoVehiculo.values().map { it.toString() },
    val tiposDeMotores: List<String> = TipoMotor.values().map { it.toString() }
){

}

enum class TipoOperacion {
    AÑADIR, EDITAR
}

//Tras terminar el ejercicio si este objeto sigue teniendo lo mismo que Propietario, borrarlo y poner Propietario
data class PropietarioReference(
    val dni: String = Propietario.PROPIETARIO_ID,
    val nombre: String = "",
    val apellidos: String = "",
    val telefono: String = "",
    val email: String = ""
)

//Tras terminar el ejercicio si este objeto sigue teniendo lo mismo que Vehiculo, borrarlo y poner Vehiculo
data class VehiculoReference(
    val id: Long = Vehiculo.VEHICULO_ID,
    val matricula: String = "",
    val marca: String = "",
    val modelo: String = "",
    val fechaMatriculacion: LocalDate = LocalDate.now(),
    val fechaUltimaRevision: LocalDateTime? = null,
    val tipoMotor: TipoMotor = TipoMotor.OTRO,
    val tipoVehiculo: TipoVehiculo = TipoVehiculo.OTRO,
    val dniPropietario: String = ""
)

//Tras terminar el ejercicio si este objeto sigue teniendo lo mismo que Trabajador, borrarlo y poner Trabajador
data class TrabajadorReference(
    val idTrabajador: Long = Trabajador.TRABAJADOR_ID,
    val idEstacion: Long = Trabajador.TRABAJADOR_ID,
    val nombre: String = "",
    val telefono: String = "",
    val email: String = "",
    val nombreUsuario: String = "",
    val contraseñaUsuario: String = "",
    val fechaContratacion: LocalDate = LocalDate.now(),
    var especialidades: List<Especialidad> = listOf(),
    val idResponsable: Long = -1L,
    val citas: Map<Pair<LocalDateTime, LocalDateTime>, List<Long>> = mapOf()
)

