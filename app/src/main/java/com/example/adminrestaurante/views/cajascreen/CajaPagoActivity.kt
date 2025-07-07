// CajaPagoActivity.kt
package com.example.adminrestaurante.views.cajascreen

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminrestaurante.databinding.ActivityCajaPagoBinding
import com.example.adminrestaurante.models.DetallePedido
import com.example.adminrestaurante.models.Pedido
import com.example.adminrestaurante.network.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CajaPagoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCajaPagoBinding
    private lateinit var adapter: AdaptadorCaja
    private val allViewModels = mutableListOf<AdaptadorCaja.ViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCajaPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ActionBar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "LISTA DE PEDIDOS"
        }

        // Adapter: al ocultar, elimina de la lista actual
        adapter = AdaptadorCaja { pedido ->
            ocultarPedido(pedido)
        }
        binding.rvCajaPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvCajaPedidos.adapter = adapter

        // Filtrar por mesa
        binding.btnAplicarFiltro.setOnClickListener {
            val mesaText = binding.etFiltroNumeroMesa.text.toString().trim()
            val mesa = mesaText.toIntOrNull()
            if (mesa != null) {
                val filtrados = if (mesaText.isEmpty()) {
                    allViewModels
                } else {
                    allViewModels.filter { it.pedido.numeroMesa == mesa }
                }
                adapter.submitList(filtrados)
            } else {
                Toasty.warning(this, "Ingrese un número de mesa válido", Toasty.LENGTH_SHORT).show()
            }
        }

        cargarPedidos()
    }

    private fun cargarPedidos() {
        CoroutineScope(Dispatchers.IO).launch {
            val pedidosCall = RetrofitClient.webService.obtenerPedidos()
            val detallesCall = RetrofitClient.webService.obtenerDetalles()
            runOnUiThread {
                if (pedidosCall.isSuccessful && detallesCall.isSuccessful) {
                    val pedidos = pedidosCall.body()!!.datos
                    val detalles = detallesCall.body()!!.datos
                    val mapDetalles = detalles.groupBy { it.idPedido }

                    allViewModels.clear()
                    allViewModels += pedidos.map { pedido ->
                        val lista = mapDetalles[ pedido.idPedido ].orEmpty()
                        AdaptadorCaja.ViewModel(pedido, lista)
                    }

                    adapter.submitList(allViewModels.toList())
                } else {
                    Toasty.error(this@CajaPagoActivity, "Error al cargar pedidos", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    /** Elimina de la lista visible (oculta el pedido) */
    private fun ocultarPedido(pedido: Pedido) {
        // Aqui podrías llamar al backend para marcar como pagado...
        // RetrofitClient.webService.ocultarPedido(pedido.idPedido) etc.

        // Solo UI: lo quitamos de allViewModels y refrescamos
        allViewModels.removeAll { it.pedido.idPedido == pedido.idPedido }
        adapter.submitList(allViewModels.toList())
        Toasty.success(this, "Pedido ${pedido.idPedido} ocultado", Toasty.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startActivity(
                Intent(this, com.example.adminrestaurante.views.navegacionscreen.NavegacionActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
