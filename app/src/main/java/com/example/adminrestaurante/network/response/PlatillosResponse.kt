package com.example.adminrestaurante.network.response

import com.example.adminrestaurante.models.Platillos
import com.google.gson.annotations.SerializedName

data class PlatillosResponse(
    @SerializedName("codigo")
    val codigo: String,
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("datos")
    val datos: ArrayList<Platillos>
)
