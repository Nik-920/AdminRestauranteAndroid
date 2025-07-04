package com.example.adminrestaurante.models

import com.google.gson.annotations.SerializedName

data class Pedido(
    @SerializedName("idPedido")    val idPedido: Int,
    @SerializedName("numeroMesa")  val numeroMesa: Int,
    @SerializedName("idUsuario")   val idUsuario: Int,
    @SerializedName("fechaPedido") val fechaPedido: String  // o Date si parseas
)
