// AdaptadorPlatillo.kt
package com.example.adminrestaurante.views.platilloscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adminrestaurante.R
import com.example.adminrestaurante.models.Platillos

class AdaptadorPlatillo(
    private var listaPlatillos: List<Platillos>,
    private val onClick: OnItemClicked
) : RecyclerView.Adapter<AdaptadorPlatillo.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_platillo, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val platillo = listaPlatillos[position]
        holder.tvNombrePlatillo.text = platillo.nomPlatillo.uppercase()
        holder.tvPrecio.text = "S/ ${platillo.precio}"
        holder.tvCategoria.text = "Categoría: ${platillo.idCategoria}"

        platillo.descripcionPlatillo?.let {
            if (it.isNotEmpty()) {
                holder.tvDescripcion.visibility = View.VISIBLE
                holder.tvDescripcion.text = it
            } else {
                holder.tvDescripcion.visibility = View.GONE
            }
        }

        holder.ivEditar.setOnClickListener { onClick.editarPlatillo(platillo) }
        holder.ivBorrar.setOnClickListener { onClick.borrarPlatillo(platillo.idPlatillos) }
    }

    override fun getItemCount(): Int = listaPlatillos.size

    /**
     * Actualiza la lista de platillos y notifica el cambio.
     */
    fun updateData(nuevos: List<Platillos>) {
        listaPlatillos = nuevos
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombrePlatillo: TextView = itemView.findViewById(R.id.tvNombrePlatillo)
        val tvCategoria: TextView = itemView.findViewById(R.id.tvCategoriaPlatillo)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionPlatillo)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val ivEditar: ImageView = itemView.findViewById(R.id.ivEditar)
        val ivBorrar: ImageView = itemView.findViewById(R.id.ivBorrar)
    }

    interface OnItemClicked {
        fun editarPlatillo(platillo: Platillos)
        fun borrarPlatillo(idPlatillo: Int)
    }
}


