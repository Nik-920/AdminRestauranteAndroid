package com.example.adminrestaurante.views.categoriascreen
import com.example.adminrestaurante.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminrestaurante.models.Categoria
import com.example.adminrestaurante.utils.Constantes

class AdaptadorCategoria(
    val context: Context,
    val listaCategorias: ArrayList<Categoria>,
    val onClick: OnItemClicked
): RecyclerView.Adapter<AdaptadorCategoria.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdaptadorCategoria.ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_categoria, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoria = listaCategorias[position]

        Glide
            .with(context)
            //.load("${Constantes.PATH_IMG_CATEGORIAS}${categoria.imagenCategoria}")
            .load("${Constantes.PATH_IMG_CATEGORIAS}/${categoria.imagenCategoria}")
            .centerInside()
            .placeholder(R.drawable.icon_falta_foto)
            .into(holder.ivCategoria)

        holder.tvNomCategoria.text = categoria.nomCategoria.uppercase()

        holder.ibtnBorrar.setOnClickListener {
            onClick.borrarCategoria(categoria.nomCategoria)
        }
    }

    override fun getItemCount(): Int {
       return listaCategorias.size
    }

    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        // Me genera error en estas lineas de codigo porque
        val ivCategoria = itemview.findViewById<ImageView>(R.id.ivCategoria) as ImageView
        val ibtnBorrar = itemview.findViewById<ImageView>(R.id.ibtnBorrar) as ImageView
        val tvNomCategoria = itemview.findViewById<TextView>(R.id.tvNomCategoria) as TextView
    }

    interface OnItemClicked{
        fun borrarCategoria(nomCategoria: String)
    }

}