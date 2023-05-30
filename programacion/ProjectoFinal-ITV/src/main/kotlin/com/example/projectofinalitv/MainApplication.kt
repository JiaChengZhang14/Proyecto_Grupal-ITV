package com.example.projectofinalitv


import com.example.projectofinalitv.di.myModule
import com.example.projectofinalitv.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.context.startKoin

private val logger = KotlinLogging.logger {  }

class MainApplication : Application() {

    init {
        logger.debug { "Se inicia la aplicacion y junto a ella koin" }
        startKoin {
            modules(modules = myModule)
        }
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