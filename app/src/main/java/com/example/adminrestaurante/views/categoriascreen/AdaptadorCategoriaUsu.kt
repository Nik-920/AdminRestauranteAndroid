package com.example.adminrestaurante.views.categoriascreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminrestaurante.R
import com.example.adminrestaurante.models.Categoria
import com.example.adminrestaurante.utils.Constantes

class AdaptadorCategoriaUsu(
    private val context: Context,
    private val listaCategorias: List<Categoria>,
    private val onClick: OnItemClicked
) : RecyclerView.Adapter<AdaptadorCategoriaUsu.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_categoria_usu, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoria = listaCategorias[position]

        // Concatenar con slash para formar URL correcta
        val urlImagen = "${Constantes.PATH_IMG_CATEGORIAS}/${categoria.imgCategoria.orEmpty()}"
        Glide.with(context)
            .load(urlImagen)
            .centerInside()
            .placeholder(R.drawable.icon_falta_foto)
            .into(holder.ivCategoria)

        holder.tvNomCategoria.text = categoria.nomCategoria.uppercase()
        holder.tvNomCategoria.setTextColor(context.getColor(R.color.black))

        holder.cvCategoria.setOnClickListener {
            onClick.verPlatillosCategoria(categoria.idCategoria)
        }
    }

    override fun getItemCount(): Int = listaCategorias.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCategoria: CardView = itemView.findViewById(R.id.cvCategoria)
        val ivCategoria: ImageView = itemView.findViewById(R.id.ivCategoria)
        val tvNomCategoria: TextView = itemView.findViewById(R.id.tvNomCategoria)
    }

    interface OnItemClicked {
        fun verPlatillosCategoria(idCategoria: Int)
    }
}
