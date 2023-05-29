package com.example.projectofinalitv.services.storage.informe

import com.example.projectofinalitv.error.InformeError
import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.services.storage.base.ExportMultipleDataStorage
import com.example.projectofinalitv.services.storage.base.ExportSingleDataStorage

interface IInformeMultipleDataStorage: ExportMultipleDataStorage<Informe, InformeError>, ExportSingleDataStorage<Informe, InformeError> {}