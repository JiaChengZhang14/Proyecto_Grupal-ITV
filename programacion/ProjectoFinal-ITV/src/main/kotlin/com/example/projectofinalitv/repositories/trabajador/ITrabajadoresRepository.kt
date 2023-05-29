package com.example.projectofinalitv.repositories.trabajador

import com.example.projectofinalitv.models.Trabajador

fun interface ITrabajadoresRepository{
    fun getAll(): List<Trabajador>
}