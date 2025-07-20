package com.example.adminrestaurante.models

import com.google.gson.annotations.SerializedName

data class Pedido(
    @SerializedName("idPedido")    val idPedido: Int,
    @SerializedName("numeroMesa")  val numeroMesa: Int,
    @SerializedName("idUsuario")   val idUsuario: Int,
    @SerializedName("cuentaTotal") val cuentaTotal: Double,
    @SerializedName("pedidoElaborado")   val pedidoElaborado: Boolean?,
    @SerializedName("pedidoPagado")      val pedidoPagado: Boolean?,
    @SerializedName("fechaPedido") val fechaPedido: String
)
