package com.example.adminrestaurante.views.categoriascreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityCategoriasBinding
import android.graphics.drawable.ColorDrawable
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adminrestaurante.models.Categoria
import com.example.adminrestaurante.network.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriasActivity : AppCompatActivity(),AdaptadorCategoria.OnItemClicked {

    private lateinit var binding: ActivityCategoriasBinding
    lateinit var listaCategorias: ArrayList<Categoria>
    lateinit var adaptadorCategoria: AdaptadorCategoria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cambiar el color de la barra de estado (forma recomendada)
        window.statusBarColor = ContextCompat.getColor(this, R.color.gris_oscuro)

        // Configurar el ActionBar correctamente
        supportActionBar?.apply {
            // Añadir un botón de retroceso
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@CategoriasActivity, R.color.valle_dorado)))
            title = "ADMINISTRAR CATEGORÍAS"
        }
        obtenerCategorias()
    }

    private fun obtenerCategorias(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerCategorias()
            runOnUiThread {
                if (call.isSuccessful){
                    if (call.body()!!.codigo == "200"){
                        listaCategorias = call.body()!!.datos
                        setupRecyclerView()
                    } else {
                        Toasty.error(this@CategoriasActivity, "Error en el servidor Y BODY", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toasty.error(this@CategoriasActivity, "Error en el servidor de la consulta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        // Configurar el RecyclerView y el adaptador
        // ...
        val layoutManager = GridLayoutManager(this,2)
        binding.rvPlatillos.layoutManager = layoutManager
        adaptadorCategoria = AdaptadorCategoria(this, listaCategorias, this)
        binding.rvPlatillos.adapter = adaptadorCategoria
    }

    // Configurar el botón de retroceso A HOME
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun borrarCategoria(nomCategoria: String) {
        Toast.makeText(this, "Borrando $nomCategoria", Toast.LENGTH_SHORT).show()
    }

}