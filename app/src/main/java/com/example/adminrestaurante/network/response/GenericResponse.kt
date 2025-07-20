package com.example.adminrestaurante.network.response

import com.google.gson.annotations.SerializedName

data class GenericResponse(
    @SerializedName("codigo")  val codigo: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("datos")   val datos: List<Any> = emptyList()
)