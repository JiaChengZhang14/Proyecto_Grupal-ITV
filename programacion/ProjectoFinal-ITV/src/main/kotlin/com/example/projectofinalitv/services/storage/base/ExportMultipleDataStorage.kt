package com.example.projectofinalitv.services.storage.base

import com.github.michaelbull.result.Result

fun interface ExportMultipleDataStorage<T, E>{
    fun exportMultipleData(data: List<T>): Result<List<T>, E>
}