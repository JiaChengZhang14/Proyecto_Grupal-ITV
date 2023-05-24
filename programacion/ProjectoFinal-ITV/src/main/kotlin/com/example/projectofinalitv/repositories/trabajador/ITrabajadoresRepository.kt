package com.example.projectofinalitv.repositories.trabajador

import com.example.projectofinalitv.models.Trabajador

interface ITrabajadoresRepository{
    fun getAll(): List<Trabajador>
}