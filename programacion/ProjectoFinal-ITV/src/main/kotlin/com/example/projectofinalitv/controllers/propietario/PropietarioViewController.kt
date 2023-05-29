package com.example.projectofinalitv.controllers.propietario

import com.example.projectofinalitv.models.Propietario
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

private val logger = KotlinLogging.logger {  }

class PropietarioViewController: KoinComponent {

    private val viewModel: ViewModel by inject()

    @FXML
    private lateinit var botonBorrar: Button

    @FXML
    private lateinit var botonEditar: Button

    @FXML
    private lateinit var botonAñadir: Button

    @FXML
    private lateinit var email: TextField

    @FXML
    private lateinit var telefono: TextField

    @FXML
    private lateinit var apellidos: TextField

    @FXML
    private lateinit var nombre: TextField

    @FXML
    private lateinit var dni: TextField

    @FXML
    private lateinit var columnaTelefono: TableColumn<Propietario, String>

    @FXML
    private lateinit var columnaNombre: TableColumn<Propietario, String>

    @FXML
    private lateinit var columnaDni: TableColumn<Propietario, String>

    @FXML
    private lateinit var tablaPropietario: TableView<Propietario>

    @FXML
    private lateinit var textFieldNombre: TextField

    @FXML
    private lateinit var textFieldMatricula: TextField


    @FXML
    private fun initialize(){
        initBinds()

        initEvents()

        initStyle()
    }

    /**
     * función donde proporcionamos estilo a los elementos de la ventana principal de gestión de propietarios
     * @author IvanRoncoCebadera
     */
    private fun initStyle() {
        logger.debug { "Iniciamos estilos ha aplicar a nuestro ventana de gestión de propietarios" }

        val styleOpacity = "-fx-opacity: 1"
        dni.style = styleOpacity
        nombre.style = styleOpacity
        apellidos.style = styleOpacity
        telefono.style = styleOpacity
        email.style = styleOpacity
    }

    /**
     * función donde creamos los distintos eventos que tendrá la ventana de gestión de propietarios
     * @author IvanRoncoCebadera
     */
    private fun initEvents() {
        logger.debug { "Asociamos todos los eventos necesarios a los elementos de la vista." }

        eventosDeLaTabla()


        botonAñadir.setOnAction {
            onClickAniadirPropietario()
        }

        botonEditar.setOnAction {
            onClickEditarIntroducirPropietario()
        }

        botonBorrar.setOnAction {
            onClickEliminarIntroducirPropietario()
        }
    }

    /**
     * función que crea los enlaces de los campos de la ventana de gestión de propietarios, con los datos del SharedState
     * @author IvanRoncoCebadera
     */
    private fun initBinds() {
        logger.debug { "Iniciamos todos los bindings que sean necesarios en la ventana de gestión de propietarios" }

        tablaPropietario.items = FXCollections.observableArrayList(viewModel.state.value.propietarios)
        tablaPropietario.selectionModel.selectionMode = SelectionMode.SINGLE

        columnaDni.cellValueFactory = PropertyValueFactory("dni")
        columnaNombre.cellValueFactory = PropertyValueFactory("nombre")
        columnaTelefono.cellValueFactory = PropertyValueFactory("telefono")

        viewModel.state.addListener { _, oldstate, newstate ->
            updateVehiculoDataCases(oldstate, newstate)
            updateTabla(oldstate, newstate)
        }
    }

    /**
     * función que actualiza la tabla de propietarios, en función del nuevo estado registrado, si este a cambiado, se actualiza, si no, no
     * @author IvanRoncoCebadera
     * @param oldstate es el estado antes del cambio
     * @param newstate es el estado después del cambio
     */
    private fun updateTabla(
        oldstate: SharedState,
        newstate: SharedState
    ) {
        if(oldstate.propietarios != newstate.propietarios){
            logger.debug { "Se actualizán los datos de la tabla" }
            tablaPropietario.selectionModel.clearSelection()
            tablaPropietario.items = FXCollections.observableArrayList(viewModel.state.value.propietarios)
        }
    }

    /**
     * función que actualiza los campos de visualización de contenidos sobre propieatrios, en función del nuevo estado registrado, si este a cambiado, se actualiza, si no, no
     * @author IvanRoncoCebadera
     * @param oldstate es el estado antes del cambio
     * @param newstate es el estado después del cambio
     */
    private fun updateVehiculoDataCases(
        oldstate: SharedState,
        newstate: SharedState
    ) {
        if (oldstate.propietarioReference != newstate.propietarioReference) {
            logger.debug { "Actualizamos los datos del propietario que se muestra por pantalla" }
            dni.text = newstate.propietarioReference.dni
            nombre.text = newstate.propietarioReference.nombre
            apellidos.text = newstate.propietarioReference.apellidos
            telefono.text = newstate.propietarioReference.telefono
            email.text = newstate.propietarioReference.email
        }
    }

    /**
     * función que cambia el tipo de operación a EDITAR y abre la ventana de detalles de propietarios
     * @author IvanRoncoCebadera
     */
    private fun onClickEditarIntroducirPropietario() {
        logger.debug { "Se habré la ventana modal, para la creación de un nuevo propietario" }
        if (ningunPropietarioSeleccionado()) return
        viewModel.setTipoOperacion(TipoOperacion.EDITAR)
        RoutesManager.initDetalleViewPropietario()
    }

    /**
     * función que cambia el tipo de operación a AÑADIR y abre la ventana de detalles de propietarios
     * @author IvanRoncoCebadera
     */
    private fun onClickAniadirPropietario() {
        logger.debug { "Se habré la ventana modal, para la edición de un propietario" }
        viewModel.setTipoOperacion(TipoOperacion.AÑADIR)
        RoutesManager.initDetalleViewPropietario()
    }

    /**
     * función que nos muestra una ventana pidiendo confirmación o denegación a la hora de eliminar un propietario seleccionado
     * @author IvanRoncoCebadera
     */
    private fun onClickEliminarIntroducirPropietario() {
        logger.debug { "Se inicia la acción de intertar eliminar un propietario" }
        if (ningunPropietarioSeleccionado()) return
        val id = viewModel.state.value.propietarioReference.dni
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Estás a punto de eliminar un propietario?"
            headerText = null
            contentText = "¿Seguro que deseas eliminar al propietario de dni: $id?"
        }.showAndWait().ifPresent{
            if(it == ButtonType.OK){
                viewModel.deletePropietario(id)
                    .onSuccess {
                        Alert(Alert.AlertType.INFORMATION).apply {
                            title = "Operación terminada"
                            headerText = null
                            contentText = "Se ha eliminado al propietario de dni: $id"
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
                    contentText = "Se cancela la operación de eliminar el propietario de dni: $id"
                }.showAndWait()
            }
        }
    }

    /**
     * función que comprueba si se ha seleccionado algún propietario
     * @author IvanRoncoCebadera
     * @return devuelve true, si no has seleccionado ningún propietario, false en caso contrario
     */
    private fun ningunPropietarioSeleccionado(): Boolean {
        logger.debug { "Se comprueba si hay algún propietario seleccionado" }
        if (tablaPropietario.selectionModel.selectedItem == null) {
            Alert(Alert.AlertType.ERROR).apply {
                title = "Error al ejecutar acción"
                headerText = "No has seleccionado ningún propietario!!!"
                contentText = null
            }.showAndWait()
            return true
        }
        return false
    }

    /**
     * función donde creamos los distintos eventos que tendrá la tabla de propietarios
     * @author IvanRoncoCebadera
     */
    private fun eventosDeLaTabla() {
        logger.debug { "Iniciamos los eventos que afectán directamente a la tabla" }

        tablaPropietario.selectionModel.selectedItemProperty().addListener { _, _, propietario ->
            propietario?.let { viewModel.onSelectedUpdatePropietarioReference(propietario) }
        }

        textFieldMatricula.setOnKeyReleased {
            onKeyReleaseFilterTableData()
        }

        textFieldNombre.setOnKeyReleased {
            onKeyReleaseFilterTableData()
        }
    }

    /**
     * función que actualiza los datos de la tabla de propietarios según el filtro elegido, tras dejar de pulsar una tecla
     * @author IvanRoncoCebadera
     */
    private fun onKeyReleaseFilterTableData() {
        logger.debug { "se filtran los datos de la tabla" }
        tablaPropietario.items = FXCollections.observableArrayList(
            viewModel.filtrarTablaPropietarios(
                textFieldMatricula.text,
                textFieldNombre.text,
            )
        )
    }

    /**
     * función que nos abré una ventana para avisarnos de si queremos salir de la ventana de gestión de propietarios
     * @author IvanRoncoCebadera
     * @param event el evento que provoca que se llame a esta función, lo usaremos para cancelar la acción de cerrar de la ventana
     */
    fun onCloseActionClick(event: Event) {
        logger.debug { "Se trata de salir de la ventana de gestión de propietario" }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Quieres salir de la gestión de propietarios?"
            headerText = "¿Seguro que desea proseguir?"
            contentText = "Estás apunto de salir de la ventana de gestión de los propietarios."
        }.showAndWait().ifPresent {
            if(it != ButtonType.OK){
                event.consume()
            }
        }
    }
}