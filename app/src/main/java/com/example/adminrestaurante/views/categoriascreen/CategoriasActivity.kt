package com.example.adminrestaurante.views.categoriascreen

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityCategoriasBinding
import com.example.adminrestaurante.network.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriasActivity : AppCompatActivity(), AdaptadorCategoria.OnItemClicked {

    private lateinit var binding: ActivityCategoriasBinding
    private lateinit var adaptadorCategoria: AdaptadorCategoria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del ActionBar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@CategoriasActivity, R.color.green)))
            title = "ADMINISTRAR CATEGORÍAS"
        }

        // 1) Inicializar RecyclerView con adapter vacío
        binding.rvCategorias.layoutManager = GridLayoutManager(this, 2)
        adaptadorCategoria = AdaptadorCategoria(this, emptyList(), this)
        binding.rvCategorias.adapter = adaptadorCategoria

        // Cargar datos desde el servidor
        obtenerCategorias()

        // Botón flotante para agregar
        binding.fab.setOnClickListener { formularioAdd() }
    }

    private fun formularioAdd() {
        val builder = AlertDialog.Builder(this)
        val vista = layoutInflater.inflate(R.layout.alert_dialog_add_categoria, null)
        builder.setView(vista)

        val etNomCategoria = vista.findViewById<EditText>(R.id.etNomCategoria)
        builder.setPositiveButton("ACEPTAR") { _, _ ->
            val nombre = etNomCategoria.text.toString().trim()
            if (nombre.isNotEmpty()) {
                agregarCategoria(nombre)
            } else {
                Toasty.error(this, "Debe ingresar un nombre de categoría", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("CANCELAR", null)
        builder.setCancelable(false)
        builder.show()
    }

    private fun agregarCategoria(nomCategoria: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.agregarCategoria(nomCategoria)
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    Toasty.success(this@CategoriasActivity, "Categoría agregada correctamente", Toast.LENGTH_SHORT).show()
                    obtenerCategorias()
                } else {
                    Toasty.error(this@CategoriasActivity, "Error al agregar categoría", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtenerCategorias() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerCategorias()
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    // 2) Refrescar el adapter en lugar de reinstanciarlo
                    val categorias = call.body()!!.datos
                    adaptadorCategoria.updateData(categorias)
                } else {
                    Toasty.error(this@CategoriasActivity, "Error al obtener categorías", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun borrarCategoria(idCategoria: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.borrarCategoria(idCategoria)
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    Toasty.success(this@CategoriasActivity, "Categoría eliminada correctamente", Toast.LENGTH_SHORT).show()
                    obtenerCategorias()
                } else {
                    Toasty.error(this@CategoriasActivity, "Error al eliminar categoría", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
