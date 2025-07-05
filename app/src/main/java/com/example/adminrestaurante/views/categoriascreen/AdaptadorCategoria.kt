package com.example.adminrestaurante.views.categoriascreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminrestaurante.R
import com.example.adminrestaurante.models.Categoria
import com.example.adminrestaurante.utils.Constantes

class AdaptadorCategoria(
    private val context: Context,
    private var listaCategorias: List<Categoria>,
    private val onClick: OnItemClicked
) : RecyclerView.Adapter<AdaptadorCategoria.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_categoria, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoria = listaCategorias[position]

        // Carga de imagen con Glide, limitando tamaño para evitar bloqueos
        val imagen = categoria.imgCategoria
        if (!imagen.isNullOrEmpty()) {
            Glide.with(context)
                .load("${Constantes.PATH_IMG_CATEGORIAS}/$imagen")
                .override(184, 160)
                .centerCrop()
                .placeholder(R.drawable.icon_falta_foto)
                .into(holder.ivCategoria)
        } else {
            holder.ivCategoria.setImageResource(R.drawable.icon_falta_foto)
        }

        holder.tvNomCategoria.text = categoria.nomCategoria.uppercase()

        holder.ibtnBorrar.setOnClickListener {
            onClick.borrarCategoria(categoria.idCategoria)
        }
    }

    override fun getItemCount(): Int = listaCategorias.size

    fun updateData(nuevasCategorias: List<Categoria>) {
        listaCategorias = nuevasCategorias
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCategoria: ImageView = itemView.findViewById(R.id.ivCategoria)
        val ibtnBorrar: ImageButton = itemView.findViewById(R.id.ibtnBorrar)
        val tvNomCategoria: TextView = itemView.findViewById(R.id.tvNomCategoria)
    }

    interface OnItemClicked {
        fun borrarCategoria(idCategoria: Int)
    }
}
