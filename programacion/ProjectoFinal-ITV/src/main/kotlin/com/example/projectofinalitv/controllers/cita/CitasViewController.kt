package com.example.projectofinalitv.controllers.cita

import com.example.projectofinalitv.routes.RoutesManager
import com.example.projectofinalitv.viewmodel.ViewModel
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CitasViewController: KoinComponent {

    private val viewModel: ViewModel by inject()

    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        RoutesManager.initMainVehiculoStage()
    }

    @FXML
    fun initialize(){

    }

    fun onCloseActionClick(it: Event) {
        TODO()
    }
}