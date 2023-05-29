package com.example.projectofinalitv.services.storage.base

import com.github.michaelbull.result.Result

fun interface ExportSingleDataStorage<T, E> {
    fun exportSingleData(data: T): Result<T, E>
}