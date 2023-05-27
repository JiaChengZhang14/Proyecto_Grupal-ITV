package com.example.projectofinalitv.services.storage.trabajador

import com.example.projectofinalitv.error.TrabajadorError
import com.example.projectofinalitv.models.Trabajador
import com.example.projectofinalitv.services.storage.base.ExportMultipleDataStorage

interface ITrabajadorStorage: ExportMultipleDataStorage<Trabajador, TrabajadorError> {}