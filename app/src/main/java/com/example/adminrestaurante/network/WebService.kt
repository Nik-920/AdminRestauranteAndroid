package com.example.adminrestaurante.network

import com.example.adminrestaurante.network.response.CategoriaResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface WebService {
    @GET("/categorias")
    suspend fun obtenerCategorias(): Response<CategoriaResponse>

    @FormUrlEncoded
    @POST("/categorias/add")
    suspend fun agregarCategoria(
        @Field("nom_categoria") nomCategoria: String
    ): Response<CategoriaResponse>
}