package com.example.projectofinalitv.controllers.vehiculo

import com.example.projectofinalitv.mapper.getTipoMotor
import com.example.projectofinalitv.mapper.getTipoVehiculo
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.routes.RoutesManager
import com.example.projectofinalitv.utils.getResourceAsStream
import com.example.projectofinalitv.viewmodel.SharedState
import com.example.projectofinalitv.viewmodel.TipoOperacion
import com.example.projectofinalitv.viewmodel.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javafx.collections.FXCollections
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger {  }

class VehiculosViewController: KoinComponent {

    private val viewModel: ViewModel by inject()

    @FXML
    private lateinit var menuCloseBoton: MenuItem

    @FXML
    private lateinit var textFieldMatricula: TextField

    @FXML
    private lateinit var comboBoxTipoMotor: ComboBox<String>

    @FXML
    private lateinit var comboBoxTipoVehiculo: ComboBox<String>

    @FXML
    private lateinit var tablaVehiculo: TableView<Vehiculo>

    @FXML
    private lateinit var columnaIdVehiculo: TableColumn<Vehiculo, Long>

    @FXML
    private lateinit var columnaTipoVehiculo: TableColumn<Vehiculo, String>

    @FXML
    private lateinit var columnaTipoMotor: TableColumn<Vehiculo, String>

    @FXML
    private lateinit var idPropietario: TextField

    @FXML
    private lateinit var idVehiculo: TextField

    @FXML
    private lateinit var imagenVehiculo: ImageView

    @FXML
    private lateinit var matricula: TextField

    @FXML
    private lateinit var marca: TextField

    @FXML
    private lateinit var modelo: TextField

    @FXML
    private lateinit var tipoMotor: TextField

    @FXML
    private lateinit var tipoVehiculo: TextField

    @FXML
    private lateinit var fechaUltimaRevision: TextField

    @FXML
    private lateinit var fechaMatriculacion: TextField

    @FXML
    private lateinit var botonAñadir: Button

    @FXML
    private lateinit var botonEditar: Button

    @FXML
    private lateinit var botonBorrar: Button

    @FXML
    fun initialize(){
        initBinds()

        initEvents()

        initStyle()
    }

    /**
     * función donde proporcionamos estilo a los elementos de la ventana principal de gestión de vehículos
     * @author IvanRoncoCebadera
     */
    private fun initStyle() {
        logger.debug { "Iniciamos estilos ha aplicar a nuestro programa" }

        val styleOpacity = "-fx-opacity: 1"
        fechaMatriculacion.style = styleOpacity
        fechaMatriculacion.style = styleOpacity
        fechaUltimaRevision.style = styleOpacity
        idVehiculo.style = styleOpacity
        matricula.style = styleOpacity
        marca.style = styleOpacity
        modelo.style = styleOpacity
        tipoMotor.style = styleOpacity
        tipoVehiculo.style = styleOpacity
        idPropietario.style = styleOpacity
    }

    /**
     * función donde creamos los distintos eventos que tendrá la ventana de gestión de vehículos
     * @author IvanRoncoCebadera
     */
    private fun initEvents() {
        logger.debug { "Asociamos todos los eventos necesarios a los elementos de la vista." }

        eventosDeLaTabla()

        menuCloseBoton.setOnAction {
            onCloseActionClick(it)
        }

        botonAñadir.setOnAction {
            onClickAniadirVehiculo()
        }

        botonEditar.setOnAction {
            onClickEditarIntroducirVehiculo()
        }

        botonBorrar.setOnAction {
            onClickEliminarIntroducirVehiculo()
        }
    }

    /**
     * función que crea los enlaces de los campos de la ventana de gestión de vehículos, con los datos del SharedState
     * @author IvanRoncoCebadera
     */
    private fun initBinds() {
        logger.debug { "Iniciamos todos los bindings que sean necesarios en la aplicación" }

        comboBoxTipoMotor.items = FXCollections.observableArrayList(viewModel.state.value.tiposDeMotores)
        comboBoxTipoMotor.selectionModel.selectFirst()

        comboBoxTipoVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.tiposDeVehiculos)
        comboBoxTipoVehiculo.selectionModel.selectFirst()

        tablaVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos)
        tablaVehiculo.selectionModel.selectionMode = SelectionMode.SINGLE

        columnaIdVehiculo.cellValueFactory = PropertyValueFactory("matricula")
        columnaTipoVehiculo.cellValueFactory = PropertyValueFactory("tipoVehiculoText")
        columnaTipoMotor.cellValueFactory = PropertyValueFactory("tipoMotorText")

        viewModel.state.addListener { _, oldstate, newstate ->
            updateVehiculoDataCases(oldstate, newstate)
            updateTabla(oldstate, newstate)
        }
    }

    /**
     * función que actualiza la tabla de vehículos, en función del nuevo estado registrado, si este a cambiado, se actualiza, si no, no
     * @author IvanRoncoCebadera
     * @param oldstate es el estado antes del cambio
     * @param newstate es el estado después del cambio
     */
    private fun updateTabla(
        oldstate: SharedState,
        newstate: SharedState
    ) {
        if(oldstate.vehiculos != newstate.vehiculos){
            logger.debug { "Se actualizán los datos de la tabla" }
            tablaVehiculo.selectionModel.clearSelection()
            tablaVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos)
        }
    }

    /**
     * función que actualiza los campos de visualización de contenidos sobre vehículos, en función del nuevo estado registrado, si este a cambiado, se actualiza, si no, no
     * @author IvanRoncoCebadera
     * @param oldstate es el estado antes del cambio
     * @param newstate es el estado después del cambio
     */
    private fun updateVehiculoDataCases(
        oldstate: SharedState,
        newstate: SharedState
    ) {
        if (oldstate.vehiculoReference != newstate.vehiculoReference) {
            logger.debug { "Actualizamos los datos del vehículo que se muestra por pantalla" }
            idVehiculo.text =
                if(newstate.vehiculoReference.id == Vehiculo.VEHICULO_ID) "" else newstate.vehiculoReference.id.toString()
            matricula.text = newstate.vehiculoReference.matricula
            marca.text = newstate.vehiculoReference.marca
            modelo.text = newstate.vehiculoReference.modelo
            tipoMotor.text = newstate.vehiculoReference.tipoMotor.toString()
            tipoVehiculo.text = newstate.vehiculoReference.tipoVehiculo.toString()
            fechaMatriculacion.text = newstate.vehiculoReference.fechaMatriculacion.toString()
            fechaUltimaRevision.text =
                if(newstate.vehiculoReference.fechaUltimaRevision == null) "" else newstate.vehiculoReference.fechaUltimaRevision.toString()
            imagenVehiculo.image = Image(getResourceAsStream(newstate.vehiculoReference.tipoMotor.imagePath))
            idPropietario.text = newstate.vehiculoReference.dniPropietario
        }
    }

    /**
     * función donde creamos los distintos eventos que tendrá la tabla de vehículos
     * @author IvanRoncoCebadera
     */
    private fun eventosDeLaTabla() {
        logger.debug { "Iniciamos los eventos que afectán directamente a la tabla" }

        tablaVehiculo.selectionModel.selectedItemProperty().addListener { _, _, vehiculo ->
            vehiculo?.let { viewModel.onSelectedUpdateVehiculoReference(vehiculo) }
        }

        textFieldMatricula.setOnKeyReleased {
            onKeyReleaseFilterTableData()
        }

        comboBoxTipoMotor.selectionModel.selectedItemProperty().addListener{_, _, tipoMotor ->
            tipoMotor?.let{onComboMotorSelectFilterTableData()}
        }

        comboBoxTipoVehiculo.selectionModel.selectedItemProperty().addListener{_, _, tipoVehiculo ->
            tipoVehiculo?.let{onComboVehiculoSelectFilterTableData()}
        }
    }

    /**
     * función que nos muestra una ventana pidiendo confirmaación o denegación a la hora de eliminar un vehículo seleccionado
     * @author IvanRoncoCebadera
     */
    private fun onClickEliminarIntroducirVehiculo() {
        logger.debug { "Se inicia la acción de intertar eliminar un vehículo" }
        if (ningunVehiculoSeleccionado()) return
        val id = viewModel.state.value.vehiculoReference.id
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Estás a punto de eliminar un vehículo?"
            headerText = null
            contentText = "¿Seguro que deseas eliminar al vehículo de id: $id?"
        }.showAndWait().ifPresent{
            if(it == ButtonType.OK){
                viewModel.deleteVehiculo()
                    .onSuccess {
                        Alert(Alert.AlertType.INFORMATION).apply {
                            title = "Operación terminada"
                            headerText = null
                            contentText = "Se ha eliminado al vehículo de id: $id"
                        }.showAndWait()
                    }
                    .onFailure {
                        Alert(Alert.AlertType.INFORMATION).apply {
                            title = "Operación terminada"
                            headerText = null
                            contentText = "Ha habido un error inesperado: ${it.message}"
                        }.showAndWait()
                    }
            }else{
                Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Operación cancelada"
                    headerText = null
                    contentText = "Se cancela la operación de eliminar el vehículo de id:  $id"
                }.showAndWait()
            }
        }
    }

    /**
     * función que comprueba si se ha seleccionado algún vehículo
     * @author IvanRoncoCebadera
     * @return devuelve true, si no has seleccionado ningún vehículo, false en caso contrario
     */
    private fun ningunVehiculoSeleccionado(): Boolean {
        logger.debug { "Se comprueba si hay algún vehículo seleccinado" }
        if (tablaVehiculo.selectionModel.selectedItem == null) {
            Alert(Alert.AlertType.ERROR).apply {
                title = "Error al ejecutar acción"
                headerText = "No has seleccionado ningún vehículo!!!"
                contentText = null
            }.showAndWait()
            return true
        }
        return false
    }

    /**
     * función que cambia el tipo de operación a EDITAR y abre la ventana de detalles de vehículo
     * @author IvanRoncoCebadera
     */
    private fun onClickEditarIntroducirVehiculo() {
        logger.debug { "Se habré la ventana modal, para la edición de el vehículo seleccionado" }
        if (ningunVehiculoSeleccionado()) return
        viewModel.setTipoOperacion(TipoOperacion.EDITAR)
        RoutesManager.initDetalleViewVehiculo()
    }

    /**
     * función que cambia el tipo de operación a AÑADIR y abre la ventana de detalles de vehículo
     * @author IvanRoncoCebadera
     */
    private fun onClickAniadirVehiculo() {
        logger.debug { "Se habré la ventana modal, para la creación de un nuevo vehículo" }
        viewModel.setTipoOperacion(TipoOperacion.AÑADIR)
        RoutesManager.initDetalleViewVehiculo()
    }

    /**
     * función que llama a la función de filtrar la tabla cada vez que se elige un nuevo tipo de motor
     * @author IvanRoncoCebadera
     */
    private fun onComboMotorSelectFilterTableData() {
        logger.debug { "Se filtran los datos de la tabla tras seleccionar un tipo de motor" }
        filterTableData()
    }

    /**
     * función que llama a la función de filtrar la tabla cada vez que se elige un nuevo tipo de vehículo
     * @author IvanRoncoCebadera
     */
    private fun onComboVehiculoSelectFilterTableData() {
        logger.debug { "Se filtran los datos de la tabla tras seleccionar un tipo de vehículo" }
        filterTableData()
    }

    /**
     * función que llama a la función de filtrar la tabla cada vez que se deje de pulsar las teclas, al escribir en el buscador
     * @author IvanRoncoCebadera
     */
    private fun onKeyReleaseFilterTableData() {
        logger.debug { "Se filtán los vehículos de la tabla tras escribir en el filtro" }
        filterTableData()
    }

    /**
     * función que actualiza los datos de la tabla de vehículos según el filtro elegido
     * @author IvanRoncoCebadera
     */
    private fun filterTableData() {
        logger.debug { "se filtran los datos de la tabla" }
        tablaVehiculo.items = FXCollections.observableArrayList(
            viewModel.filtrarTablaVehiculos(
                comboBoxTipoMotor.selectionModel.selectedItem.toString().getTipoMotor(),
                comboBoxTipoVehiculo.selectionModel.selectedItem.toString().getTipoVehiculo(),
                textFieldMatricula.text
            )
        )
    }

    /**
     * función que nos abré una ventana para avisarnos de si queremos salir de la ventana de gestión de vehículos
     * @author IvanRoncoCebadera
     * @param event el evento que provoca que se llame a esta función, lo usaremos para cancelar la acción de salir de la ventana
     */
    fun onCloseActionClick(event: Event) {
        logger.debug { "Se trata de salir de la app" }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Quieres salir de la app?"
            headerText = "¿Seguro que desea proseguir?"
            contentText = "Estás apunto de salir de la aplicación de gestión de los vehículos."
        }.showAndWait().ifPresent {
            if(it != ButtonType.OK){
                event.consume()
            }
        }
    }
}