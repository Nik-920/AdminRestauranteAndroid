package com.example.adminrestaurante.models

data class RespuestaPedido(
    val codigo: String,
    val mensaje: String,
    val datos: PedidoInsertado
)
