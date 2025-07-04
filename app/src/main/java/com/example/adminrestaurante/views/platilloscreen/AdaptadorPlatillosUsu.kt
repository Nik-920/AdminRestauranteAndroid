package com.example.adminrestaurante.views.platilloscreen
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adminrestaurante.R
import com.example.adminrestaurante.models.Platillos
import kotlin.text.isNullOrEmpty

class AdaptadorPlatillosUsu(
    private var listaPlatillos: List<Platillos>,
    private val onClick: OnItemClicked
) : RecyclerView.Adapter<AdaptadorPlatillosUsu.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_platillo_usu, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val platillo = listaPlatillos[position]
        holder.tvNombrePlatillo.text = platillo.nomPlatillo

        if (platillo.descripcionPlatillo.isNullOrEmpty()) {
            holder.tvDescripcionPlatillo.visibility = View.GONE
        } else {
            holder.tvDescripcionPlatillo.visibility = View.VISIBLE
            holder.tvDescripcionPlatillo.text = platillo.descripcionPlatillo
        }

        holder.tvPrecio.text = "S/ ${platillo.precio}"

        holder.ivAddLista.setOnClickListener {
            onClick.agregarPlatilloCuenta(platillo.nomPlatillo, platillo.precio)
        }
    }

    override fun getItemCount(): Int = listaPlatillos.size

    /**
     * Actualiza la lista de platillos y notifica cambios
     */
    fun updateData(nuevos: List<Platillos>) {
        listaPlatillos = nuevos
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombrePlatillo: TextView = itemView.findViewById(R.id.tvNombrePlatillo)
        val tvDescripcionPlatillo: TextView = itemView.findViewById(R.id.tvDescripcionPlatillo)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val ivAddLista: ImageView = itemView.findViewById(R.id.ivAddLista)
    }

    interface OnItemClicked {
        fun agregarPlatilloCuenta(nomPlatillo: String, precioPlatillo: Double)
    }
}
