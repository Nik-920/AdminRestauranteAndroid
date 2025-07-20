package com.example.adminrestaurante.network.response

import com.google.gson.annotations.SerializedName

data class EstadoRequest(
    @SerializedName("pedidoElaborado") val pedidoElaborado: Boolean? = null,
    @SerializedName("pedidoPagado")   val pedidoPagado: Boolean?   = null
)