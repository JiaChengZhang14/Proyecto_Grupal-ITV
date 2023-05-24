package com.example.projectofinalitv


import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.di.myModule
import com.example.projectofinalitv.models.*
import com.example.projectofinalitv.routes.RoutesManager
import com.example.projectofinalitv.services.database.DatabaseManager
import com.example.projectofinalitv.services.storage.informe.InformeStorage
import javafx.application.Application
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.lang.ProcessHandle.Info
import java.time.LocalDate
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {  }

class MainApplication : Application(), KoinComponent {

    init {
        logger.debug { "Se inicia la aplicacion y junto a ella koin" }
        startKoin {
            modules(modules = myModule)
        }
        val storage = InformeStorage(configApp = ConfigApp())
        storage.exportSingleInformeToHtml(Informe(
            LocalDateTime.now(),
            LocalDateTime.now(),
            IsApto.APTO,
            2.45,
            2.43,
            IsApto.NO_APTO,
            IsApto.APTO,
            Vehiculo(
                345,
                "asdasd",
                "asdasd",
                "asdasd",
                LocalDate.now(),
                LocalDateTime.now(),
                TipoMotor.DIESEL,
                TipoVehiculo.CAMION,
                Propietario(
                    "asdasd",
                    "asdasd",
                    "asdads",
                    "asdasd",
                    "asdasdas"
                )
            ),
            2
        ))
    }

    override fun start(stage: Stage) {
        RoutesManager.apply {
            app = this@MainApplication
        }
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}