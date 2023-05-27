package com.example.projectofinalitv.controllers.cita

import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.routes.RoutesManager
import com.example.projectofinalitv.viewmodel.SharedState
import com.example.projectofinalitv.viewmodel.TipoOperacion
import com.example.projectofinalitv.viewmodel.ViewModel
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CitasViewController : KoinComponent {

    private val logger = KotlinLogging.logger { }

    @FXML
    lateinit var fechaTabla: TableColumn<Informe, String>

    @FXML
    lateinit var matriculaTabla: TableColumn<Informe, String>

    @FXML
    lateinit var vehiculoTabla: TableColumn<Informe, String>

    @FXML
    lateinit var labelTrabajador: TextField

    @FXML
    lateinit var labelVehiculo: TextField

    @FXML
    lateinit var labelContamination: TextField

    @FXML
    lateinit var labelLuces: TextField

    @FXML
    lateinit var labelInterior: TextField

    @FXML
    lateinit var labelFrenado: TextField

    @FXML
    lateinit var labelFavorable: TextField

    @FXML
    lateinit var labelFechaFinal: TextField

    @FXML
    lateinit var labelFechaInicio: TextField

    @FXML
    lateinit var tablaInforme: TableView<Informe>

    @FXML
    lateinit var buscadorMatricula: TextField

    @FXML
    lateinit var buscadorFecha: TextField

    @FXML
    lateinit var comboTipoVehiculo: ComboBox<String>

    private val viewModel: ViewModel by inject()



    @FXML
    fun initialize() {
        //tablaCitas.items = FXCollections.observableArrayList(viewModel.state.value.trabajadores)
        tablaInforme.selectionModel.selectionMode = SelectionMode.SINGLE
        initBindings()
        //nfvjkncjkbdfnkj b
    }

    private fun initBindings() {
        comboTipoVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.tiposDeVehiculos)
        comboTipoVehiculo.selectionModel.selectFirst()

        val informes = viewModel.state.value.trabajadorReference.informe
        tablaInforme.items = FXCollections.observableArrayList(informes)
        tablaInforme.selectionModel.selectionMode = SelectionMode.SINGLE

        fechaTabla.cellValueFactory = PropertyValueFactory("intervalo")
        matriculaTabla.cellValueFactory = PropertyValueFactory("matricula")
        vehiculoTabla.cellValueFactory = PropertyValueFactory("vehiculoTipo")

        viewModel.state.addListener { _, oldState, newState ->
            updateInformeData(oldState, newState)
        }

    }

    private fun updateInformeData(oldState: SharedState, newState: SharedState) {
        if (oldState.informeReference != newState.informeReference) {
            logger.debug { "Actualizando datos que se muestran por pantalla" }
            labelFechaInicio.text = newState.informeReference.fechaInicial.toString()
            labelFechaFinal.text = newState.informeReference.fechaFinal.toString()
            labelFavorable.text = newState.informeReference.favorable
            labelFrenado.text = newState.informeReference.frenado.toString()
            labelContamination.text = newState.informeReference.contaminacion
            labelInterior.text = newState.informeReference.interior
            labelLuces.text = newState.informeReference.luces
            labelTrabajador.text = newState.informeReference.idTrabajador.toString()
            labelVehiculo.text = newState.informeReference.tipoVehiculo.toString()

        }
    }


    @FXML
    fun onPropietarios() {
        RoutesManager.initMainViewPropietario()
    }

    @FXML
    fun onVehiculos() {
        RoutesManager.initMainViewVehiculo()
    }

    fun onCloseActionClick(it: Event) {
        Platform.exit()
    }

    fun onAñadirAction() {
        RoutesManager.initDetalleViewCita()
    }

    fun onEditarAction() {
        logger.debug { "Iniciando ventana de edicion" }
        if (ningunInformeSeleccionado()) return
        viewModel.setTipoOperacion(TipoOperacion.EDITAR)
        RoutesManager.initDetalleViewCita()
    }

    private fun ningunInformeSeleccionado(): Boolean {
        logger.debug { "Comprobando si se ha seleccionado un informe" }
        if (tablaInforme.selectionModel.selectedItem == null) {
            Alert(Alert.AlertType.ERROR).apply {
                title = "Error al ejecutar acción"
                headerText = "No has seleccionado ningún informe!!!"
                contentText = null
            }.showAndWait()
            return true
        }
        return false
    }

    fun onBorrarAction() {
       ningunInformeSeleccionado()
    }

    fun onAcercaDeAction(){
        RoutesManager.initAcercaDeView()
    }
}