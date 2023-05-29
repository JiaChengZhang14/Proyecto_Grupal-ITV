package com.example.projectofinalitv.routes

import com.example.projectofinalitv.controllers.cita.CitasViewController
import com.example.projectofinalitv.controllers.cita.DetallesViewController
import com.example.projectofinalitv.controllers.propietario.PropietarioDetallesController
import com.example.projectofinalitv.controllers.propietario.PropietarioViewController
import com.example.projectofinalitv.controllers.vehiculo.VehiculosDetallesController
import com.example.projectofinalitv.controllers.vehiculo.VehiculosViewController
import com.example.projectofinalitv.utils.getResource
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {  }

object RoutesManager {
    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage
    val activeStage get() = _activeStage
    lateinit var app: Application

    init {
        logger.debug { "Se inicia el routes manager" }
        Locale.setDefault(Locale("es", "ES"))
    }

    private enum class Vistas(val url: String){
        MAIN("views/citas-main-view.fxml"),
        MAIN_DETALLES("views/citas-detalle-view.fxml"),
        ACERCA_DE("views/acerca-de-view.fxml"),
        VEHICULO("views/vehiculos-main-view.fxml"),
        VEHICULO_DETALLES("views/vehiculos-detalles-view.fxml"),
        PROPIETARIO("views/propietarios-main-view.fxml"),
        PROPIETARIO_DETALLES("views/propietarios-detalles-view.fxml")
    }

    /**
     * función que abre la vista principal de la app, la de citas
     * @author IvanRoncoCebadera
     * @param stage la stage donde se habrirá la escena de la vista principal de la app
     */
    fun initMainStage(stage: Stage){
        logger.debug { "Se inicia la vista principal de la aplicación" }
        val fxmlLoader = FXMLLoader(getResource(Vistas.MAIN.url))
        val scene = Scene(fxmlLoader.load(), 950.0, 450.0)
        stage.apply {
            title = "Gestión de citas"
            isResizable = false
            setOnCloseRequest {
                fxmlLoader.getController<CitasViewController>().onCloseActionClick(it)
            }
            stage.scene = scene
        }

        mainStage = stage
        _activeStage = stage

        mainStage.show()
    }

    /**
     * Funcion que abre la vista de detalles
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun initDetalleViewCita() {
        logger.debug { "Se inicia la vista de los detalles de la cita" }
        val stage = Stage()
        val fxmlLoader = FXMLLoader(getResource(Vistas.MAIN_DETALLES.url))
        val scene = Scene(fxmlLoader.load(), 260.0, 400.0)
        stage.apply {
            title = "Detalles de cita"
            isResizable = false
            setOnCloseRequest {
                fxmlLoader.getController<PropietarioDetallesController>().onCloseActionClick(it)
            }
            stage.scene = scene
        }

        stage.initOwner(activeStage)
        stage.initModality(Modality.WINDOW_MODAL)

        stage.setOnCloseRequest {
            fxmlLoader.getController<DetallesViewController>().onCloseActionClick(it)
        }
        stage.scene = scene
        _activeStage = stage

        activeStage.show()
    }
    /**
     * Funcion que abre la vista de acerca de
     * @author JiaCheng Zhang, Kevin David Matute
     */
    fun initAcercaDeView() {
        logger.debug { "Iniciando la vista de acerca de" }
        val stage = Stage()
        val fxmlLoader = FXMLLoader(getResource(Vistas.ACERCA_DE.url))
        val scene = Scene(fxmlLoader.load(), 600.0, 409.0)
        stage.apply {
            title = "Gestión de propietarios"
            isResizable = false

            stage.scene = scene
        }

        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        _activeStage = stage

        activeStage.show()
    }

    /**
     * función que abre la vista principal de vehículos
     * @author IvanRoncoCebadera
     */
    fun initMainViewVehiculo(){
        logger.debug { "Se inicia la vista principal de vehiculos" }
        val stage = Stage()
        val fxmlLoader = FXMLLoader(getResource(Vistas.VEHICULO.url))
        val scene = Scene(fxmlLoader.load(), 800.0, 500.0)
        stage.apply {
            title = "Gestión de vehículos"
            isResizable = false
            setOnCloseRequest {
                fxmlLoader.getController<VehiculosViewController>().onCloseActionClick(it)
            }
            stage.scene = scene
        }

        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        _activeStage = stage

        activeStage.show()
    }

    /**
     * función que abre la vista detalles de vehículos
     * @author IvanRoncoCebadera
     */
    fun initDetalleViewVehiculo(){
        logger.debug { "Se inicia la vista detalles de vehiculos de la aplicación" }
        val stage = Stage()
        val fxmlLoader = FXMLLoader(getResource(Vistas.VEHICULO_DETALLES.url))
        val scene = Scene(fxmlLoader.load(), 350.0, 550.0)
        stage.apply {
            title = "Gestión de vehículos"
            isResizable = false
            setOnCloseRequest {
                fxmlLoader.getController<VehiculosDetallesController>().onClickCancelarOperacion(it)
            }
            stage.scene = scene
        }

        stage.initOwner(activeStage)
        stage.initModality(Modality.WINDOW_MODAL)

        _activeStage = stage

        activeStage.show()
    }

    /**
     * función que abre la vista de gestión de propietarios
     * @author IvanRoncoCebadera
     */
    fun initMainViewPropietario() {
        logger.debug { "Se inicia la vista de gestión de propietario de la aplicación" }
        val stage = Stage()
        val fxmlLoader = FXMLLoader(getResource(Vistas.PROPIETARIO.url))
        val scene = Scene(fxmlLoader.load(), 800.0, 400.0)
        stage.apply {
            title = "Gestión de propietarios."
            isResizable = false
            setOnCloseRequest {
                fxmlLoader.getController<PropietarioViewController>().onCloseActionClick(it)
            }
            stage.scene = scene
        }

        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        _activeStage = stage

        activeStage.show()
    }

    /**
     * función que abre la vista de detalles de propietarios
     * @author IvanRoncoCebadera
     */
    fun initDetalleViewPropietario() {
        logger.debug { "Se inicia la vista de detalles de propietario de la aplicación" }
        val stage = Stage()
        val fxmlLoader = FXMLLoader(getResource(Vistas.PROPIETARIO_DETALLES.url))
        val scene = Scene(fxmlLoader.load(), 300.0, 360.0)
        stage.apply {
            title = "Gestión de propietarios"
            isResizable = false
            setOnCloseRequest {
                fxmlLoader.getController<PropietarioDetallesController>().onCloseActionClick(it)
            }
            stage.scene = scene
        }

        stage.initOwner(activeStage)
        stage.initModality(Modality.WINDOW_MODAL)

        _activeStage = stage

        activeStage.show()
    }
}