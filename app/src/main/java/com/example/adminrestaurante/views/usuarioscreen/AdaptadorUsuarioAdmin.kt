package com.example.adminrestaurante.views.usuarioscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adminrestaurante.R
import com.example.adminrestaurante.models.Usuario

class AdaptadorUsuarioAdmin(
    private var lista: List<Usuario>,
    private val onClick: OnItemClicked
) : RecyclerView.Adapter<AdaptadorUsuarioAdmin.ViewHolder>() {

    private var listaCompleta: List<Usuario> = lista.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_usuario, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val u = lista[pos]
        holder.tvNombre.text = "${u.nombreUsuario} ${u.apellidoP} ${u.apellidoM}"
        holder.tvCorreo.text = u.gmail
        holder.tvTelefono.text = "Tel: ${u.telefono}"

        holder.ivEditar.setOnClickListener { onClick.editarUsuario(u) }
        holder.ivEliminar.setOnClickListener { onClick.borrarUsuario(u.idUsuario) }
    }

    override fun getItemCount(): Int = lista.size

    fun updateData(nuevos: List<Usuario>) {
        listaCompleta = nuevos.toList()
        lista = listaCompleta
        notifyDataSetChanged()
    }

    /**
     * Filtra por nombre, apellido y correo (si no están vacíos)
     */
    fun filter(nombre: String, apellido: String, correo: String) {
        lista = listaCompleta.filter { u ->
            (nombre.isEmpty() || u.nombreUsuario.contains(nombre, ignoreCase = true)) &&
                    (apellido.isEmpty() || u.apellidoP.contains(apellido, ignoreCase = true)
                            || u.apellidoM.contains(apellido, ignoreCase = true)) &&
                    (correo.isEmpty() || u.gmail.contains(correo, ignoreCase = true))
        }
        notifyDataSetChanged()
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val tvNombre: TextView = item.findViewById(R.id.tvUsuarioNombre)
        val tvCorreo: TextView = item.findViewById(R.id.tvUsuarioCorreo)
        val tvTelefono: TextView = item.findViewById(R.id.tvUsuarioTelefono)
        val ivEditar: ImageView = item.findViewById(R.id.ivEditar)
        val ivEliminar: ImageView = item.findViewById(R.id.ivEliminar)
    }

    interface OnItemClicked {
        fun editarUsuario(u: Usuario)
        fun borrarUsuario(idUsuario: Int)
    }
}
