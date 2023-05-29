package com.example.projectofinalitv.dto

import com.google.gson.annotations.SerializedName

data class InformeDTO(
    @SerializedName("id_informe")
    val idInforme: String,
    @SerializedName("fecha_inicial")
    val fechaInicio: String,
    @SerializedName("fecha_final")
    val fechaFinal : String,
    @SerializedName("favorable")
    val favorable: String? = null,
    @SerializedName("frenado")
    val frenado: String? = null,
    @SerializedName("contaminacion")
    val contaminacion: String? = null,
    @SerializedName("interior")
    val interior: String? = null,
    @SerializedName("luces")
    val luces: String? = null,
    @SerializedName("trabajador_id")
    val trabajador: String,
    @SerializedName("vehiculo")
    val vehiculo: VehiculoDTO
)