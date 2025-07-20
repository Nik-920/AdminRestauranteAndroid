package com.example.adminrestaurante.network.response

import com.google.gson.annotations.SerializedName

data class CreateUserResponse(
    @SerializedName("codigo")  val codigo: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("datos")   val datos: CreatedUserId
)

