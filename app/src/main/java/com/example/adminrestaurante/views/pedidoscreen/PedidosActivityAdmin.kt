package com.example.adminrestaurante.views.pedidoscreen

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminrestaurante.databinding.ActivityPedidosBinding
import com.example.adminrestaurante.models.DetallePedido
import com.example.adminrestaurante.network.RetrofitClient
import com.example.adminrestaurante.network.response.EstadoRequest
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PedidosActivityAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityPedidosBinding
    private lateinit var adapter: AdaptadorPedidosAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar ActionBar con botón de retroceso
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "LISTA DE PEDIDOS"
        }

        // RecyclerView
//        adapter = AdaptadorPedidosAdmin { pedido ->
//            Toasty.info(this, "PEDIDO ELABORADO ${pedido.idPedido}", Toasty.LENGTH_SHORT).show()
//        }

        adapter = AdaptadorPedidosAdmin { pedido ->
            marcarElaborado(pedido.idPedido)
        }
        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adapter

        obtenerPedidosSinElaborar()
    }

    private fun obtenerPedidosSinElaborar() {
        CoroutineScope(Dispatchers.IO).launch {
            val pedidosCall = RetrofitClient.webService.obtenerPedidosSinElaborar()
            val detallesCall = RetrofitClient.webService.obtenerDetalles()
            runOnUiThread {
                if (pedidosCall.isSuccessful && detallesCall.isSuccessful) {
                    val pedidos = pedidosCall.body()!!.datos
                    val detalles = detallesCall.body()!!.datos

                    val mapDetalles: Map<Int, List<DetallePedido>> =
                        detalles.groupBy { it.idPedido }

                    val vm = pedidos.map { pedido ->
                        val lista = mapDetalles[pedido.idPedido].orEmpty()
                        AdaptadorPedidosAdmin.ViewModel(pedido, lista)
                    }
                    adapter.submitList(vm)
                } else {
                    Toasty.error(this@PedidosActivityAdmin, "Error al cargar pedidos", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun marcarElaborado(idPedido: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val resp = RetrofitClient.webService.actualizarElaborado(
                idPedido,
                EstadoRequest(pedidoElaborado = true)
            )
            withContext(Dispatchers.Main) {
                if (resp.isSuccessful && resp.body()?.codigo == "200") {
                    Toasty.success(this@PedidosActivityAdmin, "Pedido $idPedido marcado como elaborado", Toasty.LENGTH_SHORT).show()
                    obtenerPedidosSinElaborar() // refresca estado
                } else {
                    Toasty.error(this@PedidosActivityAdmin, "Error al actualizar estado", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Acción al presionar el botón ← del ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Regresar a NavegacionActivity
            val intent = Intent(this, com.example.adminrestaurante.views.navegacionscreen.NavegacionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
