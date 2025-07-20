package com.example.adminrestaurante.network.response

import com.google.gson.annotations.SerializedName

data class CreatedUserId(
    @SerializedName("idUsuario") val idUsuario: Int
)
