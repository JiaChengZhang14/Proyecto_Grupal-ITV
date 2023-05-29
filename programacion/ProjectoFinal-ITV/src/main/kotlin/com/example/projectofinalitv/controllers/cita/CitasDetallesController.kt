package com.example.projectofinalitv.controllers.cita

import com.example.projectofinalitv.error.InformeError
import com.example.projectofinalitv.mapper.*
import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.models.IsApto
import com.example.projectofinalitv.models.Trabajador
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.routes.RoutesManager
import com.example.projectofinalitv.utils.conseguirIntervaloInicialYFinal
import com.example.projectofinalitv.utils.toLocalDateTime
import com.example.projectofinalitv.viewmodel.InformeReference
import com.example.projectofinalitv.viewmodel.TipoOperacion
import com.example.projectofinalitv.viewmodel.ViewModel
import com.github.michaelbull.result.*
import javafx.collections.FXCollections
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger { }

class DetallesViewController : KoinComponent {

    private val viewModel: ViewModel by inject()

    @FXML
    private lateinit var botonExportarAHTMl: MenuItem

    @FXML
    private lateinit var botonExportarAJSON: MenuItem

    @FXML
    private lateinit var botonCancelar: Button

    @FXML
    private lateinit var botonLimpiar: Button

    @FXML
    private lateinit var botonAceptar: Button

    @FXML
    private lateinit var intervalos: ComboBox<String>

    @FXML
    private lateinit var fecha: DatePicker

    @FXML
    private lateinit var comboBoxVehiculo: ComboBox<Vehiculo>

    @FXML
    private lateinit var comboBoxTrabajador: ComboBox<Trabajador>

    @FXML
    private lateinit var comboLuces: ComboBox<String>

    @FXML
    private lateinit var comboInterior: ComboBox<String>

    @FXML
    private lateinit var comboFavorable: ComboBox<String>

    @FXML
    private lateinit var labelContaminacion: TextField

    @FXML
    private lateinit var labelFrenado: TextField

    @FXML
    private lateinit var labelIdInforme: TextField

    @FXML
    fun initialize() {
        initEvents()

        initBindings()
    }

    /**
     * Funcion que inicia los eventos de la vista de detalles de cita
     * @author JiaCheng Zhang & Kevin David Matute
     */
    private fun initEvents() {
        logger.debug { "Iniciando eventos" }

        botonAceptar.setOnAction {
            onAceptarAction()
            RoutesManager.activeStage.close()
        }

        botonCancelar.setOnAction {
            onCloseActionClick(it)
        }

        botonLimpiar.setOnAction {
            onLimpiarAction()
        }

        botonExportarAJSON.setOnAction {
            viewModel.exportarCitaAJSON(onAceptarAction())
            RoutesManager.activeStage.close()
        }

        botonExportarAHTMl.setOnAction {
            viewModel.exportarCitaAHTML(onAceptarAction())
            RoutesManager.activeStage.close()
        }
    }

    /**
     * Funcion que inicia los bindings de la vista de detalles de cita
     * @author JiaCheng Zhang & Kevin David Matute
     */
    private fun initBindings() {
        logger.debug { "Iniciando bindings" }

        intervalos.items = FXCollections.observableArrayList(viewModel.state.value.intervalos)
        intervalos.selectionModel.select(null)
        comboBoxTrabajador.items = FXCollections.observableArrayList(viewModel.state.value.trabajadores)
        comboBoxVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos)

        val comboBoxApto = IsApto.values().filter { it.string != "" }.map { it.string }.toList()
        comboFavorable.items = FXCollections.observableArrayList(comboBoxApto)
        comboFavorable.selectionModel.select(null)
        comboInterior.items = FXCollections.observableArrayList(comboBoxApto)
        comboInterior.selectionModel.select(null)
        comboLuces.items = FXCollections.observableArrayList(comboBoxApto)
        comboLuces.selectionModel.select(null)

        if (viewModel.state.value.tipoOperacion == TipoOperacion.EDITAR) {

            val informeReference = viewModel.state.value.informeReference

            comboFavorable.selectionModel.select(viewModel.state.value.informeReference.favorable.string)
            comboInterior.selectionModel.select(viewModel.state.value.informeReference.interior.string)
            comboLuces.selectionModel.select(viewModel.state.value.informeReference.luces.string)

            comboBoxTrabajador.selectionModel.select(viewModel.state.value.trabajadores.first { it.idTrabajador == informeReference.trabajadorId })
            comboBoxVehiculo.selectionModel.select(informeReference.vehiculo)

            conseguirElFrenadoYLaContaminacion(informeReference)

            val fechaCita = viewModel.state.value.informeReference.fechaInicio

            val intervalosInforme = conseguirIntervaloInicialYFinal(fechaCita)

            fecha.value = fechaCita?.let {
                it.toLocalDate()
            }
            intervalos.selectionModel.select(
                if (intervalosInforme.first.first != -1) {
                    "${intervalosInforme.first.first}:${if (intervalosInforme.first.second.toString() == "0") "00" else intervalosInforme.first.second}:00-${intervalosInforme.second.first}:${if (intervalosInforme.second.second.toString() == "0") "00" else intervalosInforme.second.second}:00"
                } else {
                    ""
                }
            )
        } else {
            labelIdInforme.text = ""
            labelFrenado.text = ""
            labelContaminacion.text = ""
        }
    }

    /**
     * funcion que recupera el frenado y la contaminacion
     * @param   informeReference Es el informe del cual se quiere recuperar el frenado y la contaminacion
     * @author Ivan Ronco Cebadera
     */
    private fun conseguirElFrenadoYLaContaminacion(informeReference: InformeReference) {
        labelIdInforme.text = informeReference.idInforme.toString()
        val frenado = viewModel.state.value.informeReference.frenado
        labelFrenado.text =
            if ((frenado == null) || (frenado.toString() == "") || (frenado.toString() == "0.0")) "" else frenado.toString()
        val contaminacion = viewModel.state.value.informeReference.contaminacion
        labelContaminacion.text =
            if ((contaminacion == null) || (contaminacion.toString() == "") || (contaminacion.toString() == "0.0")) "" else contaminacion.toString()
    }


    /**
     *Funcion que se ejecuta tras presionar el boton de aceptar
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun onAceptarAction(): Informe? {
        var informeCopy: Informe? = null
        if (viewModel.state.value.tipoOperacion == TipoOperacion.AÑADIR) {
            generatedInforme()
                .andThen { informe ->
                    informeCopy = informe.toInforme()
                    viewModel.createInforme(informeCopy!!)
                }
                .onSuccess {
                    Alert(Alert.AlertType.CONFIRMATION).apply {
                        title = "Informe creado"
                        headerText = "El informe creado se ha guardado correctamente"
                        contentText = "El informe ${it.idInforme} fue guardado"
                    }.showAndWait()
                }
                .onFailure {
                    Alert(Alert.AlertType.ERROR).apply {
                        title = "Informe no creado"
                        headerText = "El informe no pudo ser creado"
                        contentText = it.message
                    }.showAndWait()
                    return null
                }
        } else {
            generatedInforme()
                .andThen { informe ->
                    informeCopy = informe.toInforme()
                    viewModel.updateInforme(informeCopy!!)
                }
                .onSuccess {
                    Alert(Alert.AlertType.CONFIRMATION).apply {
                        title = "Informe editado"
                        headerText = "El informe editado se ha guardado correctamente"
                        contentText = "El informe ${it.idInforme} fue editado correctamente"
                    }.showAndWait()
                }
                .onFailure {
                    Alert(Alert.AlertType.ERROR).apply {
                        title = "Informe no editado"
                        headerText = "El informe no pudo ser editado"
                        contentText = it.message
                    }.showAndWait()
                    return null
                }
        }
        return informeCopy
    }

    /**
     * Funcion que genera un informe validando los campos
     * @author JiaCheng Zhang, Kevin David Matute
     * @return En caso de que se validen los cammpos, se devolvera un informe creado y en caso negativo, se devolverá un error controlado
     */
    private fun generatedInforme(): Result<InformeReference, InformeError> {
        logger.debug { "Generamos un informe según los campos que tenemos, tras validar todo" }

        require(fecha.value != null) {
            return Err(InformeError.FechaNoSeleccionada("No has seleccionado ningún valor para la fecha."))
        }
        require(comboBoxTrabajador.selectionModel.selectedItem != null) {
            return Err(InformeError.TrabajadorNoSeleccionado("No has seleccionado ningún trabajador."))
        }
        val decimalRegex = Regex("[0-9]*.[0-9]*")
        if (labelFrenado.text.isNotEmpty() && !labelFrenado.text.matches(decimalRegex)) {
            return Err(InformeError.FrenadoNoValido("El frenado '${labelFrenado.text}' debe ser un número."))
        }
        if (labelFrenado.text.isNotEmpty() && labelFrenado.text.toDouble() !in (0.0..10.0)) {
            return Err(
                InformeError.FrenadoNoValido(
                    "El frenado '${
                        String.format(
                            "%.2f",
                            labelFrenado.text.toDouble()
                        )
                    }' debe estar entre 0.0 y 10.0."
                )
            )
        }
        if (labelContaminacion.text.isNotEmpty() && !labelContaminacion.text.matches(decimalRegex)) {
            return Err(InformeError.FrenadoNoValido("La contaminacion '${labelContaminacion.text}' debe ser un número."))
        }
        if (labelContaminacion.text.isNotEmpty() && labelContaminacion.text.toDouble() !in (20.0..50.0)) {
            return Err(
                InformeError.ContaminacionNoValida(
                    "La contaminacion '${
                        String.format(
                            "%.2f",
                            labelContaminacion.text.toDouble()
                        )
                    }' debe estar entre 20.0 y 50.0."
                )
            )
        }
        require(comboBoxVehiculo.selectionModel.selectedItem != null) {
            return Err(InformeError.VehiculoNoSeleccionado("No has seleccionado ningún vehículo."))
        }
        require(intervalos.items.filter { it != "" }.map { it.toString() }
            .contains(intervalos.selectionModel.selectedItem.toString())) {
            return Err(InformeError.IntervaloNoValido("El intervalo '${intervalos.selectionModel.selectedItem}' no es uno de los posibles intervalos a seleccionar."))
        }

        val parIntervalos = intervalos.selectionModel.selectedItem.toString().split("-")
        val fechaIncio = "${fecha.value} ${parIntervalos[0]}".toLocalDateTime()
        val fechaFinal = "${fecha.value} ${parIntervalos[1]}".toLocalDateTime()

        return Ok(
            InformeReference(
                idInforme = if (labelIdInforme.text == "") Informe.INFORME_ID else labelIdInforme.text.toLong(),
                fechaInicio = fechaIncio,
                fechaFinal = fechaFinal,
                favorable = if (comboFavorable.selectionModel.selectedItem == null) IsApto.NOT_CHOOSEN else comboFavorable.selectionModel.selectedItem.getApto(),
                frenado = if (labelFrenado.text == "") null else labelFrenado.text.toDouble(),
                contaminacion = if (labelContaminacion.text == "") null else labelContaminacion.text.toDouble(),
                interior = if (comboInterior.selectionModel.selectedItem == null) IsApto.NOT_CHOOSEN else comboInterior.selectionModel.selectedItem.getApto(),
                luces = if (comboLuces.selectionModel.selectedItem == null) IsApto.NOT_CHOOSEN else comboLuces.selectionModel.selectedItem.getApto(),
                vehiculo = comboBoxVehiculo.selectionModel.selectedItem,
                trabajadorId = comboBoxTrabajador.selectionModel.selectedItem.idTrabajador
            )
        )
    }

    /**
     * Funcion que pone todos los campos de la vista en blanco
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun onLimpiarAction() {
        labelIdInforme.text = ""
        labelFrenado.text = ""
        labelContaminacion.text = ""
        fecha.value = null
        intervalos.selectionModel.select(null)
        comboBoxTrabajador.selectionModel.select(null)
        comboBoxVehiculo.selectionModel.select(null)
        comboLuces.selectionModel.select(null)
        comboInterior.selectionModel.select(null)
        comboFavorable.selectionModel.select(null)
    }

    /**
     * Funcion que saca una ventana cuando se intenta salir de esta parte de la vista
     *  @author JiaCheng Zhang, Kevin David Matute
     */
    fun onCloseActionClick(event: Event) {
        logger.debug { "onCloseAction" }

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
}