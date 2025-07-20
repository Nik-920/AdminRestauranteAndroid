package com.example.adminrestaurante.network
import com.example.adminrestaurante.models.Platillos
import com.example.adminrestaurante.models.RespuestaPedido
import com.example.adminrestaurante.models.Usuario
import com.example.adminrestaurante.network.response.*
import retrofit2.Response
import retrofit2.http.*
interface WebService {

    /** ─── CATEGORÍAS ───────────────────────────────────────────── **/
    @GET("/categorias")
    suspend fun obtenerCategorias(): Response<CategoriaResponse>

    @GET("/platillos/categoria/{idCategoria}")
    suspend fun obtenerPlatillosCategoria(
        @Path("idCategoria") idCategoria: Int
    ): Response<PlatillosResponse>

    @FormUrlEncoded
    @POST("/categorias")
    suspend fun agregarCategoria(
        @Field("nom_categoria") nomCategoria: String
    ): Response<CategoriaResponse>

    @FormUrlEncoded
    @PUT("/categorias/{id}")
    suspend fun actualizarCategoria(
        @Path("id") idCategoria: Int,
        @Field("nom_categoria") nomCategoria: String,
        @Field("img_categoria") imgCategoria: String
    ): Response<CategoriaResponse>

    @DELETE("/categorias/{id}")
    suspend fun borrarCategoria(
        @Path("id") idCategoria: Int
    ): Response<CategoriaResponse>


    /** ─── PLATILLOS ────────────────────────────────────────────── **/

    @GET("/platillos")
    suspend fun obtenerPlatillos(): Response<PlatillosResponse>

    @GET("/platillos/categoria/{idCategoria}")
    suspend fun obtenerPlatillosPorCategoria(
        @Path("idCategoria") idCategoria: Int
    ): Response<PlatillosResponse>

    @POST("/platillos")
    suspend fun agregarPlatillo(
        @Body platillo: Platillos
    ): Response<PlatillosResponse>

    @PUT("/platillos/{id}")
    suspend fun actualizarPlatillo(
        @Path("id") idPlatillo: Int,
        @Body platillo: Platillos
    ): Response<PlatillosResponse>

    @DELETE("/platillos/{id}")
    suspend fun borrarPlatillo(
        @Path("id") idPlatillo: Int
    ): Response<PlatillosResponse>


    /** ─── USUARIOS ─────────────────────────────────────────────── **/

    @GET("/usuarios")
    suspend fun obtenerUsuarios(): Response<UsuarioResponse>

    @POST("/usuarios")
    suspend fun crearUsuario(
        @Body usuario: Usuario
    ): Response<CreateUserResponse>

    @PUT("/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") idUsuario: Int,
        @Body usuario: Usuario
    ): Response<UsuarioResponse>

    @DELETE("/usuarios/{id}")
    suspend fun borrarUsuario(
        @Path("id") idUsuario: Int
    ): Response<UsuarioResponse>


    /** ─── PEDIDOS ──────────────────────────────────────────────── **/

    @GET("/pedidos")
    suspend fun obtenerPedidos(): Response<PedidoResponse>
    @GET("/pedidosPagados")
    suspend fun obtenerPedidosSinPagar(): Response<PedidoResponse>

    @GET("/pedidosinElaborar")
    suspend fun obtenerPedidosSinElaborar(): Response<PedidoResponse>

    @DELETE("/pedidos/{id}")
    suspend fun borrarPedido(
        @Path("id") idPedido: Int
    ): Response<PedidoResponse>

    @PUT("/pedidos/{id}/elaborado")
    suspend fun actualizarElaborado(
        @Path("id") idPedido: Int,
        @Body body: EstadoRequest
    ): Response<GenericResponse>

    @PUT("/pedidos/{id}/pagado")
    suspend fun actualizarPagado(
        @Path("id") idPedido: Int,
        @Body body: EstadoRequest
    ): Response<GenericResponse>


    @FormUrlEncoded
    @POST("/pedidos")
    suspend fun crearPedido(
        @Field("numeroMesa")  numeroMesa: Int,
        @Field("idUsuario")   idUsuario: Int,
        @Field("cuentaTotal") cuentaTotal: Double
    ): Response<RespuestaPedido>

        /** ─── DETALLE PEDIDOS ─── **/
        @FormUrlEncoded
        @POST("/detalles")
        suspend fun crearDetalle(
            @Field("idPedido") idPedido: Int,
            @Field("idPlatillos") idPlatillos: Int,
            @Field("cantidad") cantidad: Int
        ): Response<Any>



    /** ─── DETALLE PEDIDOS ─────────────────────────────────────── **/

    @GET("/detalles")
    suspend fun obtenerDetalles(): Response<DetallePedidoResponse>

    @GET("/detalles/pedido/{idPedido}")
    suspend fun obtenerDetallePorPedido(
        @Path("idPedido") idPedido: Int
    ): Response<DetallePedidoResponse>


    @FormUrlEncoded
    @PUT("/detalles/{id}")
    suspend fun actualizarDetalle(
        @Path("id") idDetalle: Int,
        @Field("cantidad") cantidad: Int
    ): Response<DetallePedidoResponse>

    @DELETE("/detalles/{id}")
    suspend fun borrarDetalle(
        @Path("id") idDetalle: Int
    ): Response<DetallePedidoResponse>
}
