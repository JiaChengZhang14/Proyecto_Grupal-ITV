package com.example.projectofinalitv.controllers.propietario

import com.example.projectofinalitv.viewmodel.ViewModel
import javafx.event.Event
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PropietarioViewController: KoinComponent {

    private val viewModel: ViewModel by inject()

    fun onCloseActionClick(it: Event) {
        TODO()
    }
}