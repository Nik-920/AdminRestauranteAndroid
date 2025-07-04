package com.example.adminrestaurante.models

import com.google.gson.annotations.SerializedName

data class DetallePedido(
    @SerializedName("idDetalle")   val idDetalle: Int,
    @SerializedName("idPedido")    val idPedido: Int,
    @SerializedName("idPlatillos") val idPlatillos: Int,
    @SerializedName("cantidad")    val cantidad: Int
)
