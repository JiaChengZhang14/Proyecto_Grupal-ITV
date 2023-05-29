package com.example.projectofinalitv.services.storage.informe

import com.example.projectofinalitv.error.InformeError
import com.example.projectofinalitv.models.Informe
import com.example.projectofinalitv.services.storage.base.ExportSingleDataStorage

interface IInformeSingleDataStorage: ExportSingleDataStorage<Informe, InformeError> {}