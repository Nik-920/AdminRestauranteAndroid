package com.example.adminrestaurante.models

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("idUsuario")
    val idUsuario: Int,
    @SerializedName("nombreUsuario")
    val nombreUsuario: String,
    @SerializedName("apellidoP")
    val apellidoP: String,
    @SerializedName("apellidoM")
    val apellidoM: String,
    @SerializedName("telefono")
    val telefono: String,
    @SerializedName("gmail")
    val gmail: String,
    @SerializedName("pasword")
    val pasword: String
)
