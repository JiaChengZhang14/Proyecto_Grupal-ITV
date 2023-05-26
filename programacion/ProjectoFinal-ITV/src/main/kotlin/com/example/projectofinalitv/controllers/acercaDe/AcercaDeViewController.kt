package com.example.projectofinalitv.controllers.acercaDe

import com.example.projectofinalitv.viewmodel.ViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.Desktop
import java.net.URI

class AcercaDeViewController: KoinComponent {
    private val viewModel: ViewModel by inject()

    fun jiaGitHub() {
        val enlace = "https://github.com/JiaChengZhang14"
        val uri = URI(enlace)
        Desktop.getDesktop().browse(uri)
    }

    fun kevinGitHub(){
        val enlace = "https://github.com/kevindmatuteo"
        val uri = URI(enlace)
        Desktop.getDesktop().browse(uri)
    }

    fun ivanGitHub(){
        val enlace = "https://github.com/IvanRoncoCebadera"
        val uri = URI(enlace)
        Desktop.getDesktop().browse(uri)
    }

}