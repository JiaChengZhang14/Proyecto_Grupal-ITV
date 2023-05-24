package com.example.projectofinalitv.repositories.base

interface CrudRepository<T, ID> {
    fun getAll(): List<T>
    fun getById(id: ID): T?
    fun save(entity: T): T
    fun deleteById(id: ID): Boolean
    fun deleteAll(): Boolean
}