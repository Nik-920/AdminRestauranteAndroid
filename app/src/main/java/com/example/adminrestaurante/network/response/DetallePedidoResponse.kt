package com.example.adminrestaurante.network.response

import com.example.adminrestaurante.models.DetallePedido
import com.google.gson.annotations.SerializedName

data class DetallePedidoResponse(
    @SerializedName("codigo")   val codigo: String,
    @SerializedName("mensaje")  val mensaje: String,
    @SerializedName("datos")    val datos: ArrayList<DetallePedido>
)
