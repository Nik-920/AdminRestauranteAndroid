package com.example.adminrestaurante.models

import com.google.gson.annotations.SerializedName

data class DetallePedido(
    @SerializedName("idDetalle")
    val idDetalle: Int,
    @SerializedName("idPedido")
    val idPedido: Int,
    @SerializedName("idPlatillos")
    val idPlatillos: Int,

    // Esta línea es la que te faltaba:
    @SerializedName("nom_platillo")
    val nomPlatillo: String,

    @SerializedName("cantidad")
    val cantidad: Int
)
