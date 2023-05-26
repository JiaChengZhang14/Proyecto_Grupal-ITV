package com.example.projectofinalitv.controllers.detalles

import com.example.projectofinalitv.routes.RoutesManager
import com.example.projectofinalitv.viewmodel.TipoOperacion
import com.example.projectofinalitv.viewmodel.ViewModel
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DetallesViewController: KoinComponent {

    private val logger = KotlinLogging.logger {  }

    lateinit var comboBoxVehiculo: ComboBox<Any>
    lateinit var comboBoxTrabajador: ComboBox<Any>
    lateinit var labelLuces: TextField
    lateinit var labelInterior: TextField
    lateinit var labelFavorable: TextField
    lateinit var labelContamination: TextField
    lateinit var labelFrenado: TextField
    lateinit var labelFechaFInal: TextField
    lateinit var labelFechaInicio: TextField
    private val viewModel: ViewModel by inject()

    fun initialize(){
        initBindings()
    }

    private fun initBindings() {
        logger.debug{"Iniciando bindings"}
        if (viewModel.state.value.tipoOperacion == TipoOperacion.EDITAR){

            labelFechaInicio.text = viewModel.state.value.informeReference.fechaInicial.toString()
            labelFechaFInal.text = viewModel.state.value.informeReference.fechaFinal.toString()
            labelFavorable.text = viewModel.state.value.informeReference.favorable
            labelFrenado.text = viewModel.state.value.informeReference.frenado
            labelContamination.text = viewModel.state.value.informeReference.contaminacion
            labelInterior.text = viewModel.state.value.informeReference.interior
            labelLuces.text = viewModel.state.value.informeReference.luces
            comboBoxTrabajador.items = FXCollections.observableArrayList(viewModel.state.value.trabajadores)
            comboBoxVehiculo.items  = FXCollections.observableArrayList(viewModel.state.value.vehiculos)
        }
    }


    fun onAceptarAction(actionEvent: ActionEvent) {

    }

    fun onLimpiarAction(actionEvent: ActionEvent) {

    }

    fun onCancelarAction() {
        logger.debug { "Cerrando ventana" }
        RoutesManager.activeStage.close()
    }

    fun onCloseActionClick(event: Event?) {
        logger.debug { "onCloseAction" }

        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.apply {
            headerText = "¿Está seguro de que desea salir?"
            contentText = "Se perderán todos los datos no guardados."

        }.showAndWait().ifPresent {
            if (it == ButtonType.OK) {
                RoutesManager.activeStage.close()
            } else {
                event?.consume()
            }
        }
    }
}