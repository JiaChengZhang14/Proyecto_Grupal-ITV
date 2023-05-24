package com.example.projectofinalitv.di

import com.example.projectofinalitv.config.ConfigApp
import com.example.projectofinalitv.repositories.propietario.IPropietarioRepository
import com.example.projectofinalitv.repositories.propietario.PropietarioRepositoryImpl
import com.example.projectofinalitv.repositories.trabajador.ITrabajadoresRepository
import com.example.projectofinalitv.repositories.trabajador.TrabajadoresRepositoryImpl
import com.example.projectofinalitv.repositories.vehiculo.IVehiculosRepository
import com.example.projectofinalitv.repositories.vehiculo.VehiculosRepositoryImpl
import com.example.projectofinalitv.services.database.DatabaseManager
import com.example.projectofinalitv.viewmodel.ViewModel
import org.koin.dsl.module

val myModule = module {
    //El configApp
    single { ConfigApp() }

    //El databaseManager
    single { DatabaseManager(get()) }

    // El repositorio de vehículos
    single<IVehiculosRepository>{ VehiculosRepositoryImpl(get()) }

    //El repositorio de trabajadores
    single<ITrabajadoresRepository>{ TrabajadoresRepositoryImpl(get()) }

    //El repositorio de propietarios
    single<IPropietarioRepository>{ PropietarioRepositoryImpl(get()) }

    //El viewModel
    single { ViewModel(get(), get(), get()) }
}