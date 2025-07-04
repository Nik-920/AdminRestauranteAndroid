package com.example.adminrestaurante.network.response
import com.example.adminrestaurante.models.Usuario
import com.google.gson.annotations.SerializedName

data class UsuarioResponse(
    @SerializedName("codigo")   val codigo: String,
    @SerializedName("mensaje")  val mensaje: String,
    @SerializedName("datos")    val datos: ArrayList<Usuario>
)

//val datos: List<Usuario> = emptyList() // o Usuario si solo devuelve uno