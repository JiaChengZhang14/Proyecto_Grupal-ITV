package com.example.projectofinalitv.controllers.cita

import com.example.projectofinalitv.mapper.getTipoVehiculo
import com.example.projectofinalitv.mapper.toInforme
import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.models.Trabajador
import com.example.projectofinalitv.routes.RoutesManager
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
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CitasViewController : KoinComponent {

    private val logger = KotlinLogging.logger { }

    @FXML
    private lateinit var botonExportarCitaHTML: MenuItem

    @FXML
    private lateinit var botonExportarCitaJSON: MenuItem

    @FXML
    private lateinit var botonExportarCitas: MenuItem

    @FXML
    private lateinit var botonExportarTrabajadores: MenuItem

    @FXML
    private lateinit var fechaTabla: TableColumn<Informe, String>

    @FXML
    private lateinit var matriculaTabla: TableColumn<Informe, String>

    @FXML
    private lateinit var vehiculoTabla: TableColumn<Informe, String>

    @FXML
    private lateinit var labelIdInforme: TextField

    @FXML
    private lateinit var labelTrabajador: TextField

    @FXML
    private lateinit var labelVehiculo: TextField

    @FXML
    private lateinit var labelContamination: TextField

    @FXML
    private lateinit var labelLuces: TextField

    @FXML
    private lateinit var labelInterior: TextField

    @FXML
    private lateinit var labelFrenado: TextField

    @FXML
    private lateinit var labelFavorable: TextField

    @FXML
    private lateinit var labelFechaFinal: TextField

    @FXML
    private lateinit var labelFechaInicio: TextField

    @FXML
    private lateinit var tablaInforme: TableView<Informe>

    @FXML
    private lateinit var buscadorMatricula: TextField

    @FXML
    private lateinit var buscadorFecha: TextField

    @FXML
    private lateinit var comboTipoVehiculo: ComboBox<String>

    @FXML
    private lateinit var botonAñadir: Button

    @FXML
    private lateinit var botonEditar: Button

    @FXML
    private lateinit var botonBorrar: Button

    @FXML
    private lateinit var botonVehiculos: Button

    @FXML
    private lateinit var botonPropietarios: Button

    private val viewModel: ViewModel by inject()

    @FXML
    fun initialize() {
        initEvent()

        initStyle()

        initBindings()
    }

    /**
     * función donde proporcionamos estilo a los elementos de la ventana principal de gestión de vehículos
     * @author IvanRoncoCebadera
     */
    private fun initStyle() {
        logger.debug { "Iniciamos estilos ha aplicar a nuestra ventana de gestión de vehículos" }

        val styleOpacity = "-fx-opacity: 1"
        labelIdInforme.style = styleOpacity
        labelContamination.style = styleOpacity
        labelFavorable.style = styleOpacity
        labelFrenado.style = styleOpacity
        labelInterior.style = styleOpacity
        labelFechaFinal.style = styleOpacity
        labelFechaInicio.style = styleOpacity
        labelLuces.style = styleOpacity
        labelTrabajador.style = styleOpacity
        labelVehiculo.style = styleOpacity
    }

    /**
     * Funcion que inicia los bindings de la vista
     * @author Ivan Ronco Cebadera
     */
    private fun initBindings() {
        comboTipoVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.tiposDeVehiculos)
        comboTipoVehiculo.selectionModel.selectFirst()

        tablaInforme.items = FXCollections.observableArrayList(viewModel.toListaInformes())
        tablaInforme.selectionModel.selectionMode = SelectionMode.SINGLE
        tablaInforme.focusModel

        fechaTabla.cellValueFactory = PropertyValueFactory("fechaInicio")
        matriculaTabla.cellValueFactory = PropertyValueFactory("matricula")
        vehiculoTabla.cellValueFactory = PropertyValueFactory("tipoVehiculo")

        viewModel.state.addListener { _, oldState, newState ->
            updateInformeData(oldState, newState)
            updateTabla(oldState, newState)
        }
    }

    /**
     * Funcion que inicia los eventos de la vista
     * @author Ivan Ronco Cebadera
     */
    fun initEvent(){
        logger.debug { "Asociamos todos los eventos necesarios a los elementos de la vista." }

        eventosDeLaTabla()

        botonAñadir.setOnAction {
            onAniadirAction()
        }

        botonEditar.setOnAction {
            onEditarAction()
        }

        botonBorrar.setOnAction {
            onBorrarAction()
        }

        botonPropietarios.setOnAction {
            onPropietarios()
        }

        botonVehiculos.setOnAction {
            onVehiculos()
        }

        botonExportarTrabajadores.setOnAction {
            viewModel.exportarTrabajadoresACSV()
        }

        botonExportarCitas.setOnAction {
            viewModel.exportarCitasAJSON()
        }

        botonExportarCitaJSON.setOnAction {
            if(!ningunInformeSeleccionado()){
                viewModel.exportarCitaAJSON(viewModel.state.value.informeReference.toInforme())
            }
        }

        botonExportarCitaHTML.setOnAction {
            if(!ningunInformeSeleccionado()){
                viewModel.exportarCitaAHTML(viewModel.state.value.informeReference.toInforme())
            }
        }
    }

    /**
     * Funcion que ejecuta los eventos de la tabla (buscar, filtrar, etc)
     * @author Ivan Ronco Cebadera
     */
    private fun eventosDeLaTabla() {
        logger.debug { "Iniciamos los eventos que afectán directamente a la tabla" }

        tablaInforme.selectionModel.selectedItemProperty().addListener { _, _, informe ->
            informe?.let{viewModel.onSelectedUpdateInformeReference(informe)}
        }

        buscadorMatricula.setOnKeyReleased {
            onKeyReleaseMatriculaFilterTableData()
        }

        buscadorFecha.setOnKeyReleased {
            onKeyReleaseFechaFilterTableData()
        }

        comboTipoVehiculo.selectionModel.selectedItemProperty().addListener{_, _, tipoVehiculo ->
            tipoVehiculo?.let{onComboVehiculoSelectFilterTableData()}
        }
    }

    /**
     * Funcion que filtra los elementos de la tabla por tipo de vehiculo
     * @author Ivan Ronco Cebadera
     */
    private fun onComboVehiculoSelectFilterTableData() {
        logger.debug { "filtramos a los informes según la matrícula: ${comboTipoVehiculo.selectionModel.selectedItem}" }
        filterTableData()
    }

    /**
     * Funcion que filtra los elementos de la tabla por matricula
     * @author Ivan Ronco Cebadera
     */
    private fun onKeyReleaseMatriculaFilterTableData() {
        logger.debug { "filtramos a los informes según la matrícula: ${buscadorMatricula.text}" }
        filterTableData()
    }
    /**
     * Funcion que filtra los elementos de la tabla por lo que se escribe en el textField
     * @author Ivan Ronco Cebadera
     */
    private fun onKeyReleaseFechaFilterTableData() {
        logger.debug { "filtramos a los informes según la fecha de inicio: ${buscadorFecha.text}" }
        filterTableData()
    }

    /**
     * Funcion que filtra los datos de la tabla, segun la matricula, fecha y tipo de vehiculo
     * @author Ivan Ronco Cebadera
     */
    private fun filterTableData() {
        logger.debug { "se filtran los datos de la tabla" }
        tablaInforme.items = FXCollections.observableArrayList(
            viewModel.filtrarTablaInformes(
                buscadorMatricula.text,
                buscadorFecha.text,
                comboTipoVehiculo.selectionModel.selectedItem.getTipoVehiculo()
            )
        )
    }

    /**
     * función que actualiza la tabla de informes, en función del nuevo estado registrado, si este a cambiado, se actualiza, si no, no
     * @author IvanRoncoCebadera
     * @param oldstate es el estado antes del cambio
     * @param newstate es el estado después del cambio
     */
    private fun updateTabla(
        oldstate: SharedState,
        newstate: SharedState
    ) {
        if(oldstate.trabajadores != newstate.trabajadores){
            logger.debug { "Se actualizán los datos de la tabla" }
            tablaInforme.selectionModel.clearSelection()
            tablaInforme.items = FXCollections.observableArrayList(viewModel.toListaInformes())
        }
    }

    /**
     * Funcion que actualiza el estado de la tabla
     * @author Ivan Ronco Cebadera
     * @param oldState Es el estado viejo de la tabla
     * @param newState Es el nuevo estado de la tabla
     */
    private fun updateInformeData(oldState: SharedState, newState: SharedState) {
        if (oldState.informeReference != newState.informeReference) {
            logger.debug { "Actualizando datos que se muestran por pantalla" }
            labelIdInforme.text = if(newState.informeReference.idInforme == Informe.INFORME_ID) "" else newState.informeReference.idInforme.toString()
            labelFechaInicio.text = newState.informeReference.fechaInicio.toString()
            labelFechaFinal.text = newState.informeReference.fechaFinal.toString()
            labelFavorable.text = newState.informeReference.favorable.string
            val frenado = newState.informeReference.frenado
            labelFrenado.text = if((frenado == null) || (frenado.toString() == "") || (frenado.toString() == "0.0")) "" else frenado.toString()
            val contaminacion = newState.informeReference.contaminacion
            labelContamination.text = if((contaminacion == null) || (contaminacion.toString() == "") || (contaminacion.toString() == "0.0")) "" else contaminacion.toString()
            labelInterior.text = newState.informeReference.interior.string
            labelLuces.text = newState.informeReference.luces.string
            labelTrabajador.text = if(newState.informeReference.trabajadorId == Trabajador.TRABAJADOR_ID) "" else newState.informeReference.trabajadorId.toString()

            // Comprobar si esto está decente
            labelVehiculo.text = newState.informeReference.vehiculo?.matricula ?: ""
        }
    }


    /**
     * Funcion que inicia la vista de propietarios
     * @author Ivan Ronco Cebadera
     */
    fun onPropietarios() {
        RoutesManager.initMainViewPropietario()
    }
    /**
     * Funcion que inicia la vista de vehiculos
     * @author Ivan Ronco Cebadera
     */
    fun onVehiculos() {
        RoutesManager.initMainViewVehiculo()
    }
    /**
     * Funcion que cierra si el usuario lo confirma
     * @author Ivan Ronco Cebadera
     */
    fun onCloseActionClick(event: Event) {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.apply {
            headerText = "¿Está seguro de que desea salir?"
            contentText = "Se perderán todos los datos no guardados."
        }.showAndWait().ifPresent {
            if (it == ButtonType.OK) {
                RoutesManager.activeStage.close()
            } else {
                event.consume()
            }
        }
    }

    /**
     * Funcion que cambia el tipo de operacion a "añadir" e inicia la vista de detalles
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun onAniadirAction() {
        viewModel.setTipoOperacion(TipoOperacion.AÑADIR)
        RoutesManager.initDetalleViewCita()
    }

    /**
     * Funcion que cambia el tipo de operacion a "Editar" e inicia la vista de detalles
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun onEditarAction() {
        logger.debug { "Iniciando ventana de edicion" }
        if (ningunInformeSeleccionado()) return
        viewModel.setTipoOperacion(TipoOperacion.EDITAR)
        RoutesManager.initDetalleViewCita()
    }

    /**
     * Funcion que comprueba si se ha seleccionado algun informe
     * @author Ivan Ronco Cebadera
     * @return En caso de no se haya seleccionado nada, se devolverá true, si se
     */
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

    /**
     * Funcion que borra de la base de datos si se ha seleccionado
     * @author Ivan Ronco Cebadera
     */
    fun onBorrarAction() {
        if(ningunInformeSeleccionado()) return
        val id = viewModel.state.value.informeReference.idInforme
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Estás a punto de eliminar un informe?"
            headerText = null
            contentText = "¿Seguro que deseas eliminar al informe de id: $id?"
        }.showAndWait().ifPresent{
            if(it == ButtonType.OK){
                viewModel.deleteInforme(id)
                    .onSuccess {
                        Alert(Alert.AlertType.INFORMATION).apply {
                            title = "Operación terminada"
                            headerText = null
                            contentText = "Se ha eliminado al informe de id: $id"
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
                    contentText = "Se cancela la operación de eliminar el informe de id:  $id"
                }.showAndWait()
            }
        }
    }

    /**
     * Funcion que inicia la vista de acerca de
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun onAcercaDeAction(){
        RoutesManager.initAcercaDeView()
    }
}
