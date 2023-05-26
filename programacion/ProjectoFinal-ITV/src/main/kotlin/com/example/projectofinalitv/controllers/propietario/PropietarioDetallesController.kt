package com.example.projectofinalitv.controllers.propietario

import com.example.projectofinalitv.error.PropietarioError
import com.example.projectofinalitv.error.VehiculoError
import com.example.projectofinalitv.mapper.getTipoMotor
import com.example.projectofinalitv.mapper.getTipoVehiculo
import com.example.projectofinalitv.mapper.toPropietario
import com.example.projectofinalitv.mapper.toVehiculo
import com.example.projectofinalitv.models.Propietario
import com.example.projectofinalitv.models.Vehiculo
import com.example.projectofinalitv.routes.RoutesManager
import com.example.projectofinalitv.utils.toLocalDateTimeFromFechaHora
import com.example.projectofinalitv.viewmodel.PropietarioReference
import com.example.projectofinalitv.viewmodel.TipoOperacion
import com.example.projectofinalitv.viewmodel.VehiculoReference
import com.example.projectofinalitv.viewmodel.ViewModel
import com.github.michaelbull.result.*
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.TextField
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

private val logger = KotlinLogging.logger {  }

class PropietarioDetallesController: KoinComponent {

    private val viewModel: ViewModel by inject()

    @FXML
    private lateinit var botonCancelar: Button

    @FXML
    private lateinit var botonLimpiar: Button

    @FXML
    private lateinit var botonAceptar: Button

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
    fun initialize(){

        initBinds()

        initEvents()
    }

    /**
     * función donde creamos los distintos eventos que tendrá la ventana de detalles de propietario
     * @author IvanRoncoCebadera
     */
    private fun initEvents() {
        logger.debug { "Asociamos todos los eventos necesarios a los elementos de la vista." }

        botonLimpiar.setOnAction {
            onClickLimpiarAction()
        }

        botonCancelar.setOnAction {
            onCloseActionClick(it)
        }

        botonAceptar.setOnAction {
            onClickAceptarOperacion()
        }
    }

    /**
     * función que crea los enlaces de los campos de la ventana de detalles de propietario, con los datos del SharedState
     * @author IvanRoncoCebadera
     */
    private fun initBinds() {
        logger.debug { "Iniciamos todos los bindings que sean necesarios en la aplicación" }
        if (viewModel.state.value.tipoOperacion == TipoOperacion.EDITAR) {
            dni.text = viewModel.state.value.propietarioReference.dni
            nombre.text = viewModel.state.value.propietarioReference.nombre
            apellidos.text = viewModel.state.value.propietarioReference.apellidos
            telefono.text = viewModel.state.value.propietarioReference.telefono
            email.text = viewModel.state.value.propietarioReference.email

            dni.isEditable = false
        }else{
            dni.isEditable = true
        }
    }

    /**
     * función que pasa por el proceso de guardar o de editar según dicte el tipo de operación a ejecutar, cierra la ventana tras terminar la operación, siempre y cuando no se halla cancelado o halla fallado
     * @author IvánRoncoCebadera
     */
    private fun onClickAceptarOperacion() {
        if(viewModel.state.value.tipoOperacion == TipoOperacion.AÑADIR){
            generatedPropietario()
                .andThen { propietario ->
                    viewModel.savePropietario(propietario.toPropietario())
                }
                .onSuccess {
                    Alert(Alert.AlertType.CONFIRMATION).apply {
                        title = "Propietario creado"
                        headerText = "El propietario creado se ha guardado correctamente"
                        contentText = "El propietario ${it.dni} fue guardado"
                    }.showAndWait()
                }
                .onFailure {
                    Alert(Alert.AlertType.ERROR).apply {
                        title = "Propietario no creado"
                        headerText = "El propietario no pudo ser creado"
                        contentText = it.message
                    }.showAndWait()
                    return
                }
        }else{
            generatedPropietario()
                .andThen {propietario ->
                    viewModel.updatePropietario(propietario.toPropietario())
                }
                .onSuccess {
                    Alert(Alert.AlertType.CONFIRMATION).apply {
                        title = "Propietario editado"
                        headerText = "El propietario editado se ha guardado correctamente"
                        contentText = "El propietario ${it.dni} fue editado correctamente"
                    }.showAndWait()
                }
                .onFailure {
                    Alert(Alert.AlertType.ERROR).apply {
                        title = "Propietario no editado"
                        headerText = "El propietario no pudo ser editado"
                        contentText = it.message
                    }.showAndWait()
                    return
                }
        }
        RoutesManager.activeStage.close()
    }

    /**
     * función que valida todos los datos seleccionados y que crea el PropietarioReference correspondiente a esos datos
     * @author IvanRoncoCebadera
     * @return el PropietarioReference si todo a salido bien, o en caso de que algún campo no sea válido, el error indicando que campo es erroneo
     */
    private fun generatedPropietario(): Result<PropietarioReference, PropietarioError> {
        logger.debug { "Generamos un propietario según los campos que tenemos, tras validar todo" }

        val regexDni = Regex("[0-9]{8}[A-Z]")
        require(dni.text.matches(regexDni)){
            return Err(PropietarioError.DniNoValido(dni.text))
        }
        require(nombre.text.isNotEmpty()){
            return Err(PropietarioError.NombreNoValido(nombre.text))
        }
        require(apellidos.text.isNotEmpty()){
            return Err(PropietarioError.ApellidoNoValido(apellidos.text))
        }
        val regexTelefono = Regex("[0-9]{9}")
        require(telefono.text.matches(regexTelefono)){
            return Err(PropietarioError.TelefonoNoValido(telefono.text))
        }
        val regexEmail = Regex(".*[@gmail.com]")
        require(email.text.matches(regexEmail)){
            return Err(PropietarioError.EmailNoValido(email.text))
        }

        return Ok(
            PropietarioReference(
                dni = dni.text,
                nombre = nombre.text,
                apellidos = apellidos.text,
                telefono = telefono.text,
                email = email.text
        )
        )
    }

    /**
     * función que limpia todos los campos de propietario dejandolos vacios
     * @author IvanRoncoCebadera
     */
    private fun onClickLimpiarAction() {
        logger.debug { "Se inicia la acción de limpiar todos los campos de los datos de propietario" }
        dni.text = ""
        nombre.text = ""
        apellidos.text = ""
        email.text = ""
        telefono.text = ""
    }

    /**
     * función que nos abré una ventana para avisarnos de si queremos salir de la ventana de gestión de propietarios
     * @author IvanRoncoCebadera
     * @param event el evento que provoca que se llame a esta función, lo usaremos para cancelar la acción de cerrar la ventana
     */
    fun onCloseActionClick(event: Event) {
        val operacion = viewModel.state.value.tipoOperacion.toString().lowercase()
        logger.debug { "Se trata de salir de la ventana de detalles de propietarios" }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Quieres salir de la operación $operacion?"
            headerText = "¿Seguro que desea proseguir?"
            contentText = "Estás apunto de salir de la operación $operacion sobre propietarios."
        }.showAndWait().ifPresent {
            if(it == ButtonType.OK){
                RoutesManager.activeStage.close()
            }else{
                event.consume()
            }
        }
    }
}