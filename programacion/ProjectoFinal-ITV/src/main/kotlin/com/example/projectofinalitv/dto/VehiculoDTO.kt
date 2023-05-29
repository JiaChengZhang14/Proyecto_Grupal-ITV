package com.example.projectofinalitv.dto

import com.google.gson.annotations.SerializedName

data class VehiculoDTO(
    @SerializedName("id_vehiculo")
    val id: String,
    @SerializedName("matricula")
    val matricula: String,
    @SerializedName("marca")
    val marca: String,
    @SerializedName("modelo")
    val modelo: String,
    @SerializedName("fecha_matriculacion")
    val fechaMatriculacion: String,
    @SerializedName("fecha_ultima_revision")
    val fechaUltimaRevision: String,
    @SerializedName("tipo_motor")
    val tipoMotor: String,
    @SerializedName("tipo_vehiculo")
    val tipoVehiculo: String,
    @SerializedName("propietario")
    val propietario: PropietarioDTO
)