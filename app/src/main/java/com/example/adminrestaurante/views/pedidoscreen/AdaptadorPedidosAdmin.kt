package com.example.adminrestaurante.views.pedidoscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adminrestaurante.databinding.ItemRvPedidoBinding
import com.example.adminrestaurante.models.Pedido
import com.example.adminrestaurante.models.DetallePedido
import java.text.SimpleDateFormat
import java.util.*

class AdaptadorPedidosAdmin(
    private val onClickElaborado: (Pedido)->Unit
) : ListAdapter<AdaptadorPedidosAdmin.ViewModel, AdaptadorPedidosAdmin.VH>(DIFF) {

    data class ViewModel(
        val pedido: Pedido,
        val detalles: List<DetallePedido>
    )

    inner class VH(private val b: ItemRvPedidoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(vm: ViewModel) {
            // Número de mesa
            b.tvNumeroMesa.text = "Mesa: ${vm.pedido.numeroMesa}"

            // Convertir fecha ISO 8601 Z a formato local deseado
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC") // viene en UTC
            }
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("America/Lima") // zona horaria local
            }

            val parsedDate = try {
                isoFormat.parse(vm.pedido.fechaPedido)
            } catch (e: Exception) {
                null
            }
            b.tvFechaPedido.text = parsedDate?.let { outputFormat.format(it) }
                ?: vm.pedido.fechaPedido

            // Total
            b.tvCuentaTotal.text = "Total: S/ %.2f".format(vm.pedido.cuentaTotal)

            // Detalles
            val resumen = vm.detalles.joinToString(", ") {
                "${it.cantidad}×${it.nomPlatillo}"
            }
            b.tvDetallePlatillos.text = vm.detalles.joinToString(", ") {
                "${it.cantidad}×${it.nomPlatillo}"
            }

            // Acción de que ya se preparo su pedido esta para servir QUIERO IMPLEMENTAR ESTO
            b.ivPedidoElaborado.setOnClickListener { onClickElaborado(vm.pedido) }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemRvPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF = object: DiffUtil.ItemCallback<ViewModel>() {
            override fun areItemsTheSame(a: ViewModel, b: ViewModel) =
                a.pedido.idPedido == b.pedido.idPedido
            override fun areContentsTheSame(a: ViewModel, b: ViewModel) =
                a==b
        }
    }
}
