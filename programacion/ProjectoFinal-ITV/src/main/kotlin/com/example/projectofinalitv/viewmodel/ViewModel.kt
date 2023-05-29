package com.example.projectofinalitv.viewmodel

import com.example.projectofinalitv.error.InformeError
import com.example.projectofinalitv.error.PropietarioError
import com.example.projectofinalitv.error.VehiculoError
import com.example.projectofinalitv.mapper.*
import com.example.projectofinalitv.models.*
import com.example.projectofinalitv.repositories.informe.IInformeRepository
import com.example.projectofinalitv.repositories.propietario.IPropietarioRepository
import com.example.projectofinalitv.repositories.trabajador.ITrabajadoresRepository
import com.example.projectofinalitv.repositories.vehiculo.IVehiculosRepository
import com.example.projectofinalitv.services.storage.informe.IInformeMultipleDataStorage
import com.example.projectofinalitv.services.storage.informe.IInformeSingleDataStorage
import com.example.projectofinalitv.services.storage.trabajador.ITrabajadorStorage
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import javafx.beans.property.SimpleObjectProperty
import mu.KotlinLogging
import com.example.projectofinalitv.validator.validate
import com.example.projectofinalitv.validator.validateInforme
import com.example.projectofinalitv.validator.validatePropietario
import java.time.LocalDate
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {  }

class ViewModel(
    private val vehiculosRepository: IVehiculosRepository,
    private val trabajadoresRepository: ITrabajadoresRepository,
    private val propietariosRepository: IPropietarioRepository,
    private val informesRepository: IInformeRepository,
    private val informesStorageJson: IInformeMultipleDataStorage,
    private val informeStorageHtml: IInformeSingleDataStorage,
    private val trabajadoresStorageCsv: ITrabajadorStorage
) {

    val state: SimpleObjectProperty<SharedState> = SimpleObjectProperty(SharedState())

    init {
        state.value = state.value.copy(
            vehiculos = vehiculosRepository.getAll(),
            propietarios = propietariosRepository.getAll(),
            trabajadores = trabajadoresRepository.getAll(),
            intervalos = listOf(
                "9:00:00-9:30:00",
                "9:30:00-10:00:00",
                "10:00:00-10:30:00",
                "10:30:00-11:00:00",
                "11:00:00-11:30:00",
                "11:30:00-12:00:00",
                "12:00:00-12:30:00",
                "12:30:00-13:00:00",
                "13:00:00-13:30:00",
                "13:30:00-14:00:00",
                "14:00:00-14:30:00",
                "14:30:00-15:00:00",
                "15:00:00-15:30:00",
                "15:30:00-16:00:00",
                "16:00:00-16:30:00",
                "16:30:00-17:00:00",
                "17:00:00-17:30:00",
                "17:30:00-18:00:00",
                "18:00:00-18:30:00",
                "18:30:00-19:00:00"
            )
        )
    }

    /**
     * función que permite asignar el tipo de operación que se está realizando
     * @author IvanRoncoCebadera
     * @param tipoOperacion es el tipo de operación que se establecerá en el SharedState
     */
    fun setTipoOperacion(tipoOperacion: TipoOperacion) {
        logger.debug { "Se cambia el tipo de operación que se va a realizar a: $tipoOperacion" }
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
    fun updateSharedStateVehiculo(vehiculos: List<Vehiculo>) {
        logger.debug { "Actualizamos el estado compartido tras la operación que modificó la tabla de vehículos" }
        state.value = state.value.copy(
            vehiculoReference = VehiculoReference(),
            vehiculos = vehiculos
        )
    }

    /**
     * función que llama al repositorio de vehículos para guardar un nuevo vehículo
     * @author IvanRoncoCebadera
     * @param vehiculo es el vehículo que se desea guardar
     * @return si el completo de la operación sale bien, el vehículo guardado, si algo sale mal, el error que haya ocurrido
     */
    fun saveVehiculo(vehiculo: Vehiculo): Result<Vehiculo, VehiculoError> {
        logger.debug { "Se llama al repositorio de vehiculos para añadir un nuevo vehículo" }

        var vehiculoC = vehiculo.copy(id = Vehiculo.VEHICULO_ID)

        return vehiculoC.validate(state.value.vehiculos).andThen {
            vehiculoC = vehiculosRepository.save(vehiculoC)
            updateSharedStateVehiculo(state.value.vehiculos + vehiculoC)
            Ok(vehiculoC)
        }
    }

    /**
     * función que llama al repositorio de vehículos para actualizar un vehículo
     * @author IvanRoncoCebadera
     * @param vehiculo es el vehículo que se desea actualizar
     * @return si el completo de la operación sale bien, el vehículo actualizado, si algo sale mal, el error que haya ocurrido
     */
    fun updateVehiculo(vehiculo: Vehiculo): Result<Vehiculo, VehiculoError> {
        logger.debug { "Se llama al repositorio de vehiculos para actualizar un vehículo" }

        return vehiculo.validate(state.value.vehiculos.filter { it.id != vehiculo.id }).andThen {
            //Si no se ha editado nigún cambio, no se guarda
            if(state.value.vehiculos.contains(vehiculo)){
                return Err(VehiculoError.SameDataUpdate(vehiculo.id))
            }

            updateSharedStateVehiculo(state.value.vehiculos.filter { it.id != vehiculo.id } + vehiculo)
            Ok(vehiculosRepository.save(vehiculo))
        }
    }

    /**
     * función que llama al repositorio de vehículos para borrar un vehículo
     * @author IvanRoncoCebadera
     * @return si el completo de la operación sale bien, true, si algo sale mal, el error que haya ocurrido
     */
    fun deleteVehiculo(id: Long): Result<Boolean, VehiculoError> {
        logger.debug { "Intentamos eliminar el vehículo actualmente seleccionado" }

        if (toListaInformes().map { it.vehiculo.id }.contains(id)){
            return Err(VehiculoError.VehiculoConCitas(id.toString()))
        }

        if(!vehiculosRepository.deleteById(id)){
            return Err(VehiculoError.NotFound(id.toString()))
        }

        updateSharedStateVehiculo(state.value.vehiculos.filter { it.id != id })
        return Ok(true)
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

        return state.value.propietarios.filter { it.dni == dniPropietario}[0]
    }

    /**
     * función que actualiza la parte del estado referente a los propietarios
     * @author IvanRoncoCebadera
     * @param vehiculos la lista de propietarios con la que actualizaremos el SharedState
     */
    private fun updateSharedStatePropietario(propietarios: List<Propietario>) {
        logger.debug { "Actualizamos el estado compartido tras la operación que modificó la base de datos" }
        state.value = state.value.copy(
            propietarioReference = PropietarioReference(),
            propietarios = propietarios
        )
    }

    /**
     * función que llama al repositorio de propietarios para guardar un nuevo vehículo
     * @author IvanRoncoCebadera
     * @param vehiculo es el vehículo que se desea guardar
     * @return si el completo de la operación sale bien, el vehículo guardado, si algo sale mal, el error que haya ocurrido
     */
    fun savePropietario(propietario: Propietario): Result<Propietario, PropietarioError> {
        logger.debug { "Se llama al repositorio de propietarios para añadir un nuevo propietario" }

        return propietario.validatePropietario(state.value.propietarios).andThen {
            updateSharedStatePropietario(state.value.propietarios + propietario)
            Ok(propietariosRepository.save(propietario))
        }
    }

    /**
     * función que llama al repositorio de propietarios para actualizar un vehículo
     * @author IvanRoncoCebadera
     * @param propietario es el vehículo que se desea actualizar
     * @return si el completo de la operación sale bien, el vehículo actualizado, si algo sale mal, el error que haya ocurrido
     */
    fun updatePropietario(propietario: Propietario): Result<Propietario, PropietarioError> {
        logger.debug { "Se llama al repositorio de propietarios para actualizar un propietario" }

        // Validamos que el propieario no se cambie el DNI a otro ya existente, pero puede tener su antiguo DNI
        return propietario.validatePropietario(state.value.propietarios.filter { it.dni != propietario.dni }).andThen {
            //Si no se ha editado nigún cambio, no se guarda
            if(propietario == state.value.propietarioReference.toPropietario()){
                return Err(PropietarioError.SameDataUpdate(propietario.dni))
            }

            updateSharedStatePropietario(state.value.propietarios.filter { it.dni != propietario.dni } + propietario)
            Ok(propietariosRepository.save(propietario))
        }
    }

    /**
     * función que llama al repositorio de propietarios para borrar un propietario
     * @author IvanRoncoCebadera
     * @return si el completo de la operación sale bien, true, si algo sale mal, el error que haya ocurrido
     */
    fun deletePropietario(dni: String): Result<Boolean, PropietarioError> {
        logger.debug { "Intentamos eliminar el propietario actualmente seleccionado" }

        if(state.value.vehiculos.map { it.propietario.dni }.contains(dni)){
            return Err(PropietarioError.TodaviaExisteElVehiculo("El propietario de dni: $dni, aun está asociado a por lo menos un vehículo, por lo que no se puede borrar."))
        }

        if(!propietariosRepository.deleteById(dni)){
            return Err(PropietarioError.NotFound(dni))
        }

        updateSharedStatePropietario(state.value.propietarios.filter { it.dni != dni })
        return Ok(true)
    }

    /**
     * función que actualiza los datos a mostrar del propietario, según el que se haya seleccionado en la tabla
     * @author IvanRoncoCebadera
     * @param propietario es el propietario seleccionado en la tabla de propietarios
     */
    fun onSelectedUpdatePropietarioReference(propietario: Propietario) {
        logger.debug { "Se actualizan los datos del propietario a mostar según la selección en la tabla" }
        state.value = state.value.copy(
            propietarioReference = propietario.toPropietarioReference()
        )
    }

    /**
     * función que sirve para filtrar los datos que se encuentran visibles en la tabla de vehículos
     * @author IvanRoncoCebadera
     * @param dni es el dni con el que se filtra
     * @param nombre es el nombre con el que se filtra
     * @return devuelve la lista de vehiculos a representar, filtrada como se ha pedido
     */
    fun filtrarTablaPropietarios(dni: String, nombre: String): List<Propietario> {
        logger.debug { "Filtramos la tabla de vehiculos según el dni: $dni y el nombre: $nombre " }
        return state.value.propietarios
            .filter {
                it.dni.lowercase().contains(dni.lowercase())
            }
            .filter {
                it.nombre.lowercase().contains(nombre.lowercase())
            }
    }

    ////////////////////////////////////////////////////Informes\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\º

    /**
     * funcion que actializa el informe reference con los datos del informe seleccionado
     * @author IvanRoncoCebadera
     * @param informe es el informe que se ha seleccionado
     */
    fun onSelectedUpdateInformeReference(informe: Informe) {
        logger.debug { "Actualizamos el estado compartido tras la operación que modificó la base de datos" }

        val informes = mutableListOf<Informe>()

        state.value.trabajadores.first { it.idTrabajador == informe.trabajadorId }
            .informes.forEach { informeF ->
                if (informeF.idInforme == informe.idInforme) {
                    informes.add(informe)
                } else {
                    informes.add(informeF)
                }
            }
        state.value = state.value.copy(
            trabajadores =
            state.value.trabajadores.map { trabajador ->
                if(trabajador.idTrabajador == informe.trabajadorId){
                    trabajador.copy(informes = informes)
                }else{
                    trabajador
                }
            },
            informeReference = informe.toInformeReference()
        )
    }

    /**
     * Funcion que filtra la tabla de informes
     * @param   matricula es la matricula del vehiculo
     * @param fecha  es la fecha inicial del informe
     * @param   tipoVehiculo es el tipo de vehiculo
     * @return Devuelve la lista de informes
     */
    fun filtrarTablaInformes(matricula: String, fecha: String, tipoVehiculo: TipoVehiculo): List<Informe> {
        logger.debug { "Filtramos la tabla de informes según la matricula: $matricula, la fecha inicial: $fecha y el tipo de vehículo: $tipoVehiculo " }
        return toListaInformes()
            .filter {
                it.vehiculo.matricula.lowercase().contains(matricula.lowercase())
            }
            .filter {
                it.fechaInicio.toString().lowercase().contains(fecha.lowercase())
            }
            .filter {
                if (tipoVehiculo == TipoVehiculo.CUALQUIERA) {
                    true
                } else {
                    it.vehiculo.tipoVehiculo == tipoVehiculo
                }
            }
    }

    /**
     * función que consigue todos los informes según los trabajadores que hay en el SharedState
     * @author IvanRoncoCebadera
     * @return la lista de informe conseguida a partir de la lista de trbajajadores
     */
    fun toListaInformes(): List<Informe> {
        logger.debug { "Conseguimos todos los informes a traves de los trabajadores" }
        val informes = mutableListOf<Informe>()
        state.value.trabajadores.forEach {
            it.informes.forEach{informe ->
                informes.add(informe)
            }
        }
        return informes
    }

    /**
     * Funcion que elimina un informe
     * @param es el id del informe que se quiere eliminar
     * @return Devuelve true si sale bien, y un error si sale mal
     * @author Jiacheng Zhang, Kevin David Matute
     */
    fun deleteInforme(id: Long): Result<Boolean, InformeError> {
        logger.debug { "Intentamos eliminar el informe actualmente seleccionado" }

        if(!informesRepository.deleteInforme(id)){
            return Err(InformeError.NotFound("No se ha encontrado al informe de id: $id"))
        }

        updateSharedStateInforme(state.value.trabajadores.map { trabajador ->
            trabajador.copy(
                informes = trabajador.informes.filter { it.idInforme != id }
            )
        })
        return Ok(true)
    }

    /**
     * Funcion que actualiza el estado compartido de informe
     * @param trabajadores son los trabajadores
     * @author Ivan Ronco Cebadera
     */
    private fun updateSharedStateInforme(trabajadores: List<Trabajador>) {
        logger.debug { "Se actualiza el SharedState con los nuevo datos de los informes" }
        state.value = state.value.copy(
            informeReference = InformeReference(),
            trabajadores = trabajadores
        )
    }

    /**
     * Funcion que crea informes
     * @param  informe es el informe que se va a crear
     * @author JiaCheng Zhang, Kevin David Matute
     * @return si sale bien, devuelve un informe y si sale mal un error
     */
    fun createInforme(informe: Informe): Result<Informe, InformeError> {
        logger.debug { "Se llama al repositorio de propietarios para añadir un nuevo informe" }

        return informe.validateInforme(toListaInformes()).andThen {
            val informeSaved = informesRepository.saveInforme(informe)
            updateSharedStateInforme(state.value.trabajadores.map { trabajador ->
                if(informeSaved.trabajadorId == trabajador.idTrabajador) {
                    trabajador.copy(
                        informes = trabajador.informes + informeSaved
                    )
                }else{
                    trabajador
                }
            })
            Ok(informesRepository.saveInforme(informeSaved))
        }
    }
    /**
     * Funcion que actualiza informes
     * @param  informe es el informe que se va a actualizar
     * @author JiaCheng Zhang, Kevin David Matute
     * @return si sale bien, devuelve un informe y si sale mal un error
     */
    fun updateInforme(informe: Informe): Result<Informe, InformeError> {
        logger.debug { "Se llama al repositorio de propietarios para actualizar un informe" }

        // Validamos que el propieario no se cambie el DNI a otro ya existente, pero puede tener su antiguo DNI
        return informe.validateInforme(toListaInformes().filter { it.idInforme != informe.idInforme }).andThen {
            //Si no se ha editado nigún cambio, no se guarda
            if(informe == state.value.informeReference.toInforme()){
                return Err(InformeError.SameDataUpdate("No has cambiado ningún dato del informe de id: ${informe.idInforme}"))
            }

            val informesSaved = informesRepository.saveInforme(informe)

            if(informe.trabajadorId != state.value.informeReference.trabajadorId){
                deleteInformeFromTrabajador(state.value.informeReference.idInforme, state.value.informeReference.trabajadorId)
            }

            updateSharedStateInforme(
                state.value.trabajadores.map { trabajador ->
                    if(informesSaved.trabajadorId == trabajador.idTrabajador) {
                        trabajador.copy(
                            informes = trabajador.informes.filter { it.idInforme != informesSaved.idInforme } + informesSaved
                        )
                    }else{
                        trabajador
                    }
                }
            )
            Ok(informesSaved)
        }
    }

    /**
     * Funcion que elimina un inforne de un trabajador
     * @param informeId es el id del informe
     * @param trabajadorId es el id del trabajador
     * @author JiaCHeng Zhang, Kevin David Matute
     */
    private fun deleteInformeFromTrabajador(informeId: Long, trabajadorId: Long) {
        state.value = state.value.copy(
            trabajadores = state.value.trabajadores.map {trabajador ->
                if (trabajadorId == trabajador.idTrabajador) {
                    trabajador.copy(
                        informes = trabajador.informes.filter { it.idInforme != informeId }
                    )
                } else {
                    trabajador
                }
            }
        )
    }

    /**
     * funcion llama al storage json
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun exportarCitasAJSON(){
        logger.debug { "Exportamos todos las citas a un fichero JSON." }
        informesStorageJson.exportMultipleData(toListaInformes())
    }

  /**
     * funcion llama al storage json
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun exportarCitaAJSON(cita: Informe?){
        logger.debug { "Exportamos la cita a un fichero JSON." }
        if(cita != null){
            informesStorageJson.exportSingleData(cita)
        }
    }

    /**
     * funcion llama al storage html
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun exportarCitaAHTML(cita: Informe?){
        logger.debug { "Exportamos la cita a un fichero HTML." }
        if(cita != null){
            informeStorageHtml.exportSingleData(cita)
        }
    }

    //////////////////////////////////////////////////Trabajador\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * funcion llama al storage csv
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun exportarTrabajadoresACSV(){
        logger.debug { "Exportamos todos los trabajadores a un fichero CSV." }
        trabajadoresStorageCsv.exportMultipleData(state.value.trabajadores)
    }

}


data class SharedState(
    val trabajadores: List<Trabajador> = listOf(),
    val vehiculos: List<Vehiculo> = listOf(),
    val propietarios: List<Propietario> = listOf(),
    val trabajadorReference: TrabajadorReference = TrabajadorReference(),
    val vehiculoReference: VehiculoReference = VehiculoReference(),
    val propietarioReference: PropietarioReference = PropietarioReference(),
    val informeReference: InformeReference = InformeReference(),
    val tipoOperacion: TipoOperacion = TipoOperacion.AÑADIR,
    val tiposDeVehiculos: List<String> = TipoVehiculo.values().map { it.toString() },
    val tiposDeMotores: List<String> = TipoMotor.values().map { it.toString() },
    val intervalos: List<String> = emptyList()
)

data class InformeReference (
    val idInforme: Long = Informe.INFORME_ID,
    val fechaInicio: LocalDateTime? = null,
    val fechaFinal: LocalDateTime? = null,
    val favorable: IsApto = IsApto.NOT_CHOOSEN,
    val frenado: Double? = null,
    val contaminacion: Double? = null,
    val interior: IsApto = IsApto.NOT_CHOOSEN,
    val luces: IsApto = IsApto.NOT_CHOOSEN,
    val vehiculo: Vehiculo? = null,
    val trabajadorId: Long = Trabajador.TRABAJADOR_ID
)

enum class TipoOperacion {
    AÑADIR, EDITAR
}

//Tras terminar el ejercicio si este objeto sigue teniendo lo mismo que Propietario, borrarlo y poner Propietario
data class PropietarioReference(
    val dni: String = "",
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
    val contraseniaUsuario: String = "",
    val fechaContratacion: LocalDate = LocalDate.now(),
    var especialidades: List<Especialidad> = listOf(),
    val idResponsable: Long = -1L,
    val informes: List<Informe> = emptyList()
)

