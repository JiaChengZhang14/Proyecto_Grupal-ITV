package com.example.projectofinalitv.controllers.acercaDe

import java.awt.Desktop
import java.net.URI

class AcercaDeViewController{


    /**
     * Función que habilita el enlace en la vista hacia el github de JiaCheng Zhang
     * @author JiaCheng Zhang & Kevin David Matute
     */
    fun jiaGitHub() {
        val enlace = "https://github.com/JiaChengZhang14"
        val uri = URI(enlace)
        Desktop.getDesktop().browse(uri)
    }


    /**
     * Función que habilita el enlace en la vista hacia el github de Kevin David Matute
     * @author JiaCheng Zhang & Kevin David Matute
     */
    fun kevinGitHub(){
        val enlace = "https://github.com/kevindmatuteo"
        val uri = URI(enlace)
        Desktop.getDesktop().browse(uri)
    }

    /**
     * Función que habilita el enlace en la vista hacia el github de Ivan Ronco Cebadera
     * @author JiaCheng Zhang & Kevin David Matute
     */
    fun ivanGitHub(){
        val enlace = "https://github.com/IvanRoncoCebadera"
        val uri = URI(enlace)
        Desktop.getDesktop().browse(uri)
    }
}