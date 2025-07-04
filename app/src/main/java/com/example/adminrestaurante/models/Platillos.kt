package com.example.adminrestaurante.models
import com.google.gson.annotations.SerializedName
data class Platillos(
    @SerializedName("idPlatillos")
    val idPlatillos: Int,
    @SerializedName("nom_platillo")
    var nomPlatillo: String,
    @SerializedName("descripcion_platillo")
    var descripcionPlatillo: String?,
    @SerializedName("precio")
    var precio: Double,
    @SerializedName("idCategoria")
    var idCategoria: Int
)
