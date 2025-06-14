package com.example.adminrestaurante.models
import com.google.gson.annotations.SerializedName
data class Platillos(
    @SerializedName("nom_platillo")
    val nomPlatillo: String,
    @SerializedName("descripcion_platillo")
    val descripcionPlatillo: String,
    @SerializedName("precio")
    val precioPlatillo: Double,
    @SerializedName("nom_categoria")
    val nomCategoria: String,
)
