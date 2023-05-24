package com.example.projectofinalitv.controllers.vehiculo

import com.example.projectofinalitv.error.VehiculoError
import com.example.projectofinalitv.mapper.getTipoMotor
import com.example.projectofinalitv.mapper.getTipoVehiculo
import com.example.projectofinalitv.mapper.toPropietario
import com.example.projectofinalitv.mapper.toVehiculo
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.TipoMotor
import com.example.projectofinalitv.models.TipoVehiculo
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.routes.RoutesManager
import com.example.projectofinalitv.utils.conseguirIntervaloInicialYFinal
import com.example.projectofinalitv.utils.getResourceAsStream
import com.example.projectofinalitv.utils.toLocalDateTime
import com.example.projectofinalitv.viewmodel.TipoOperacion
import com.example.projectofinalitv.viewmodel.VehiculoReference
import com.example.projectofinalitv.viewmodel.ViewModel
import com.github.michaelbull.result.*
import javafx.collections.FXCollections
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

private val logger = KotlinLogging.logger {  }

class VehiculosDetallesController: KoinComponent {

    val viewModel: ViewModel by inject()

    @FXML
    private lateinit var botonCancelar: Button

    @FXML
    private lateinit var botonLimpiar: Button

    @FXML
    private lateinit var botonAceptar: Button

    @FXML
    private lateinit var propietarios: ComboBox<Propietario>

    @FXML
    private lateinit var imagenVehiculo: ImageView

    @FXML
    private lateinit var fechaMatriculacion: DatePicker

    @FXML
    private lateinit var fechaUltimaRevision: DatePicker

    @FXML
    private lateinit var intervalosDeTiempo: ComboBox<String>

    @FXML
    private lateinit var tipoMotor: ComboBox<String>

    @FXML
    private lateinit var tipoVehiculo: ComboBox<String>

    @FXML
    private lateinit var modelo: TextField

    @FXML
    private lateinit var marca: TextField

    @FXML
    private lateinit var matricula: TextField

    @FXML
    private lateinit var idVehiculo: TextField

    @FXML
    fun initialize(){

        //Para que en el campo de la fecha no se pueda mas que seleccionar con el picker
        fechaMatriculacion.isEditable = false

        initBinds()

        initEvents()
    }

    /**
     * función donde creamos los distintos eventos que tendrá la ventana de detalles de vehículo
     * @author IvanRoncoCebadera
     */
    private fun initEvents() {
        logger.debug { "Asociamos todos los eventos necesarios a los elementos de la vista." }

        botonLimpiar.setOnAction {
            onClickLimpiarAction()
        }

        botonCancelar.setOnAction {
            onClickCancelarOperacion(it)
        }

        botonAceptar.setOnAction {
            onClickAceptarOperacion()
        }
    }

    /**
     * función que crea los enlaces de los campos de la ventana de detalles de vehículo, con los datos del SharedState
     * @author IvanRoncoCebadera
     */
    private fun initBinds() {
        logger.debug { "Iniciamos todos los bindings que sean necesarios en la aplicación" }
        //Independiente del tipo de operación, cargo las posibles opciones del combo box
        //Le quito la opción CUALQUIERA ya que no deseo que sea un posible opción
        tipoMotor.items = FXCollections.observableArrayList(viewModel.state.value.tiposDeMotores.filter { it != TipoMotor.CUALQUIERA.toString() })
        tipoVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.tiposDeVehiculos.filter { it != TipoVehiculo.CUALQUIERA.toString() })

        intervalosDeTiempo.items = FXCollections.observableArrayList(
            listOf(
                "",
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

        propietarios.items = FXCollections.observableArrayList(viewModel.state.value.propietarios)
        propietarios.selectionModel.selectFirst()

        imagenVehiculo.image = Image(getResourceAsStream(viewModel.state.value.vehiculoReference.tipoMotor.imagePath))

        if (viewModel.state.value.tipoOperacion == TipoOperacion.EDITAR) {
            idVehiculo.text =
                if(viewModel.state.value.vehiculoReference.id == Vehiculo.VEHICULO_ID) "" else viewModel.state.value.vehiculoReference.id.toString()

            matricula.text = viewModel.state.value.vehiculoReference.matricula
            marca.text = viewModel.state.value.vehiculoReference.marca
            modelo.text = viewModel.state.value.vehiculoReference.modelo

            fechaMatriculacion.value = viewModel.state.value.vehiculoReference.fechaMatriculacion

            val ultimaRevision = viewModel.state.value.vehiculoReference.fechaUltimaRevision

            val intervalos = conseguirIntervaloInicialYFinal(ultimaRevision)

            fechaUltimaRevision.value = ultimaRevision?.let{
                it.toLocalDate()
            }
            intervalosDeTiempo.selectionModel.select(
                if(intervalos.first.first != -1)"${intervalos.first.first}:${intervalos.first.second}:00-${intervalos.second.first}:${intervalos.second.second}:00" else ""
            )

            tipoMotor.selectionModel.select(viewModel.state.value.vehiculoReference.tipoMotor.toString())
            tipoVehiculo.selectionModel.select(viewModel.state.value.vehiculoReference.tipoVehiculo.toString())
            propietarios.selectionModel.select(viewModel.state.value.propietarioReference.toPropietario())
        }else{
            //Si hemos elegido añadir, en el combo box empezmos por defecto en la primera opción
            tipoMotor.selectionModel.selectLast()
            tipoVehiculo.selectionModel.selectLast()
            propietarios.selectionModel.selectFirst()
        }
    }

    /**
     * función que pasa por el proceso de guardar o de editar según dicte el tipo de operación a ejecutar, cierra la ventana tras terminar la operación, siempre y cuando no se halla cancelado
     * @author IvánRoncoCebadera
     */
    private fun onClickAceptarOperacion() {
        if(viewModel.state.value.tipoOperacion == TipoOperacion.AÑADIR){
            generatedVehiculo()
                .andThen { vehiculo ->
                    viewModel.saveVehiculo(vehiculo.toVehiculo(viewModel.getPropietarioForVehiculo(vehiculo.dniPropietario)))
                }
                .onSuccess {
                    Alert(Alert.AlertType.CONFIRMATION).apply {
                        title = "Vehículo creado"
                        headerText = "El vehículo creado se ha guardado correctamente"
                        contentText = "El vehículo ${it.matricula} fue guardado"
                    }.showAndWait()
                }
                .onFailure {
                    Alert(Alert.AlertType.ERROR).apply {
                        title = "Vehículo no creado"
                        headerText = "El vehículo no pudo ser creado"
                        contentText = it.message
                    }.showAndWait()
                    return
                }
        }else{
            generatedVehiculo()
                .andThen {vehiculo ->
                    viewModel.updateVehiculo(vehiculo.toVehiculo(viewModel.getPropietarioForVehiculo(vehiculo.dniPropietario)))
                }
                .onSuccess {
                    Alert(Alert.AlertType.CONFIRMATION).apply {
                        title = "Vehículo editado"
                        headerText = "El vehículo editado se ha guardado correctamente"
                        contentText = "El vehículo ${it.matricula} fue editado correctamente"
                    }.showAndWait()
                }
                .onFailure {
                    Alert(Alert.AlertType.ERROR).apply {
                        title = "Vehículo no editado"
                        headerText = "El vehículo no pudo ser editado"
                        contentText = it.message
                    }.showAndWait()
                    return
                }
        }
        RoutesManager.activeStage.close()
    }

    /**
     * función que valida todos los datos seleccionados y que crea el VehiculoReference correspondiente a esos datos
     * @author IvanRoncoCebadera
     * @return el VehiculoReference si todo a salido bien, o en caso de que algún campo no sea válido, el error indicando que campo es erroneo
     */
    private fun generatedVehiculo(): Result<VehiculoReference, VehiculoError> {
        logger.debug { "Generamos un vehículo según los campos que tenemos, tras validar todo" }

        val regexMatricula = Regex("[A-Z]{4}[0-9]{3}")
        require(matricula.text.matches(regexMatricula)){
            return Err(VehiculoError.MatriculaNoValida(matricula.text))
        }
        require(marca.text.isNotEmpty()){
            return Err(VehiculoError.MarcaNoValida(marca.text))
        }
        require(modelo.text.isNotEmpty()){
            return Err(VehiculoError.ModeloNoValido(modelo.text))
        }
        if(fechaMatriculacion.value.isAfter(LocalDate.now())){
            return Err(VehiculoError.FechaMatriculacionNoValida(fechaMatriculacion.value))
        }
        if(fechaUltimaRevision.value.isAfter(LocalDate.now())){
            return Err(VehiculoError.FechaMatriculacionNoValida(fechaUltimaRevision.value))
        }

        return Ok(VehiculoReference(
            id = idVehiculo.text.toLongOrNull()?: Vehiculo.VEHICULO_ID,
            matricula = matricula.text,
            marca = marca.text,
            modelo = modelo.text,
            fechaMatriculacion = fechaMatriculacion.value,
            fechaUltimaRevision = toLocalDateTime(fechaUltimaRevision.value, intervalosDeTiempo.selectionModel.selectedItem),
            tipoMotor = tipoMotor.selectionModel.selectedItem.getTipoMotor(),
            tipoVehiculo = tipoVehiculo.selectionModel.selectedItem.getTipoVehiculo(),
            dniPropietario = propietarios.selectionModel.selectedItem.dni
        ))
    }

    /**
     * función que nos abré una ventana para avisarnos de si queremos salir de la ventana de detalles de vehículo
     * @author IvanRoncoCebadera
     * @param event el evento que provoca que se llame a esta función, lo usaremos para cancelar la acción de salir de la ventana
     */
    fun onClickCancelarOperacion(event: Event) {
        val operacion = viewModel.state.value.tipoOperacion.toString().lowercase()
        logger.debug { "Se cancela la operación $operacion que estabamos realizando" }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Quieres salir de la operación $operacion?"
            headerText = "¿Seguro que desea proseguir?"
            contentText = "Estás apunto de salir de la $operacion sobre vehículos."
        }.showAndWait().ifPresent {
            if(it != ButtonType.OK){
                event.consume()
            }
        }
    }

    /**
     * función que limpia todos los campos de vehículo dejandolos vacios o con la selección por defecto
     * @author IvanRoncoCebadera
     */
    private fun onClickLimpiarAction() {
        logger.debug { "Limpiamos los datos de todos los campos" }
        matricula.text = ""
        marca.text = ""
        modelo.text = ""
        tipoMotor.selectionModel.selectLast()
        tipoVehiculo.selectionModel.selectLast()
        propietarios.selectionModel.selectFirst()
        fechaMatriculacion.value = null
        fechaUltimaRevision.value = null
        intervalosDeTiempo.selectionModel.selectFirst()
    }
}