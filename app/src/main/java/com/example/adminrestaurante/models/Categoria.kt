package com.example.adminrestaurante.models
import com.google.gson.annotations.SerializedName
data class Categoria(
    @SerializedName("nom_categoria")
    val nomCategoria: String,
    @SerializedName("img_categoria")
    val imagenCategoria: String
)
