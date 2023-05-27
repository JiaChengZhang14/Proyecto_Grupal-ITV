package com.example.projectofinalitv.repositories.informe

import com.example.projectofinalitv.models.Informe

interface IInformeRepository {
    fun getInforme(idInforme: Long): Informe?
    fun saveInforme(informe: Informe): Informe
    fun deleteInforme(idInforme: Long): Boolean
    fun deleteAllInformes(): Boolean
}