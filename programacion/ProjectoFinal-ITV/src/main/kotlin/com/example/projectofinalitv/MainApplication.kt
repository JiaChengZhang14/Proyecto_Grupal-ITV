package com.example.projectofinalitv


import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.di.myModule
import com.example.projectofinalitv.models.*
import com.example.projectofinalitv.routes.RoutesManager
import com.example.projectofinalitv.services.database.DatabaseManager
import com.example.projectofinalitv.services.storage.informe.InformeStorageHtml
import com.example.projectofinalitv.services.storage.informe.InformeStorageJson
import com.example.projectofinalitv.services.storage.trabajador.TrabajadorStorageCsv
import javafx.application.Application
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.time.LocalDate
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {  }

class MainApplication : Application(), KoinComponent {

    init {
        logger.debug { "Se inicia la aplicacion y junto a ella koin" }
        startKoin {
            modules(modules = myModule)
        }
        val trabajadorStorage = TrabajadorStorageCsv(ConfigApp())
        trabajadorStorage.exportarCsv(
            listOf(
                Trabajador(
                    1, 3, "Iván", "748294857", "ivan@gmail.com", "iijij", "ierijei", LocalDate.now(),
                    listOf(Especialidad.ELECTRICIDAD), 1,
                    listOf(
                        Informe(
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            IsApto.APTO,
                            9.87,
                            21.45,
                            IsApto.NO_APTO,
                            null,
                            Vehiculo(
                                1, "njijij", "jnjinj", "jnemijem", LocalDate.now(), null , TipoMotor.CUALQUIERA, TipoVehiculo.CUALQUIERA,
                                Propietario(
                                    "njrnoirjv", "jvijenve", "lnk mdk m", "nvkdpoefmv", "mdk mfdkp "
                                )
                            ),
                            1
                        )
                    )
                )
            )
        )

        val informeStorage = InformeStorageJson(ConfigApp())
        informeStorage.exportSingleInformeToJson(
            Informe(
            LocalDateTime.now(),
            LocalDateTime.now(),
            IsApto.APTO,
            9.87,
            21.45,
            IsApto.NO_APTO,
            null,
            Vehiculo(
                1, "njijij", "jnjinj", "jnemijem", LocalDate.now(), null , TipoMotor.CUALQUIERA, TipoVehiculo.CUALQUIERA,
                Propietario(
                    "njrnoirjv", "jvijenve", "lnk mdk m", "nvkdpoefmv", "mdk mfdkp "
                )
            ),
            1
        )
        )
        val informeHtml = InformeStorageHtml(ConfigApp())
        informeHtml.exportSingleInformeToHtml(
            Informe(
                LocalDateTime.now(),
                LocalDateTime.now(),
                IsApto.APTO,
                9.87,
                21.45,
                IsApto.NO_APTO,
                null,
                Vehiculo(
                    1, "njijij", "jnjinj", "jnemijem", LocalDate.now(), null , TipoMotor.CUALQUIERA, TipoVehiculo.CUALQUIERA,
                    Propietario(
                        "njrnoirjv", "jvijenve", "lnk mdk m", "nvkdpoefmv", "mdk mfdkp "
                    )
                ),
                1
            )
        )
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