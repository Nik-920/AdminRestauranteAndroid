// AdaptadorCaja.kt
package com.example.adminrestaurante.views.cajascreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adminrestaurante.databinding.ItemRvCajapagoBinding
import com.example.adminrestaurante.models.DetallePedido
import com.example.adminrestaurante.models.Pedido
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AdaptadorCaja(
    private val onClickHide: (Pedido) -> Unit
) : ListAdapter<AdaptadorCaja.ViewModel, AdaptadorCaja.VH>(DIFF) {

    data class ViewModel(
        val pedido: Pedido,
        val detalles: List<DetallePedido>
    )

    inner class VH(private val b: ItemRvCajapagoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(vm: ViewModel) {
            val p = vm.pedido
            b.tvNumeroMesaCaja.text = "Mesa: ${p.numeroMesa}"

            // Fecha UTC -> Lima
            val iso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
            val out = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .apply { timeZone = TimeZone.getTimeZone("America/Lima") }
            val date = runCatching { iso.parse(p.fechaPedido) }.getOrNull()
            b.tvFechaPedidoCaja.text = date?.let { out.format(it) } ?: p.fechaPedido

            b.tvCuentaTotalCaja.text = "Total: S/ %.2f".format(p.cuentaTotal)
            b.tvDetallePlatillosCaja.text = vm.detalles.joinToString(", ") {
                "${it.cantidad}×${it.nomPlatillo}"
            }

            // Al pulsar icono, ocultamos
            b.ivVerDetallesCaja.setOnClickListener { onClickHide(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRvCajapagoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ViewModel>() {
            override fun areItemsTheSame(a: ViewModel, b: ViewModel) =
                a.pedido.idPedido == b.pedido.idPedido
            override fun areContentsTheSame(a: ViewModel, b: ViewModel) = a == b
        }
    }
}
