package com.example.adminrestaurante.network.response

import com.example.adminrestaurante.models.Pedido
import com.google.gson.annotations.SerializedName

data class PedidoResponse(
    @SerializedName("codigo")   val codigo: String,
    @SerializedName("mensaje")  val mensaje: String,
    @SerializedName("datos")    val datos: ArrayList<Pedido>
)
