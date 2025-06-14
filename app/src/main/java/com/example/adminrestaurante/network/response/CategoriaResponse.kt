package com.example.adminrestaurante.network.response
import com.example.adminrestaurante.models.Categoria
import com.google.gson.annotations.SerializedName
data class CategoriaResponse(
    @SerializedName("codigo")
    val codigo: String,
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("datos")
    val datos: ArrayList<Categoria>
)
