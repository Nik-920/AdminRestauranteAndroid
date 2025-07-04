package com.example.adminrestaurante.views.categoriascreen

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityCategoriasUsuBinding
import com.example.adminrestaurante.models.Categoria
import com.example.adminrestaurante.network.RetrofitClient
import com.example.adminrestaurante.utils.Utils
import com.example.adminrestaurante.views.cuentascreen.CuentasActivity
import com.example.adminrestaurante.views.platilloscreen.PlatillosActivityUsu
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriasActivityUsu : AppCompatActivity(), AdaptadorCategoriaUsu.OnItemClicked {

    private lateinit var binding: ActivityCategoriasUsuBinding
    private lateinit var adaptador: AdaptadorCategoriaUsu
    private val listaCategorias = ArrayList<Categoria>()

    private val startActivityIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Actualizar cuenta al regresar porque el usuario pudo haber realizado cambios
            Utils(this, binding.tvCuentaTotal).validarCuentaTotal()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasUsuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ActionBar estilizado
        val titulo = SpannableString("D'ZUASOS : RESTAURANTE")
        titulo.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, titulo.length, 0
        )
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@CategoriasActivityUsu, R.color.amor_secreto)))
            title = titulo
        }

        // RecyclerView
        adaptador = AdaptadorCategoriaUsu(this, listaCategorias, this)
        binding.rvCategorias.layoutManager = GridLayoutManager(this, 2)
        binding.rvCategorias.adapter = adaptador

        // Cargar datos
        obtenerCategoriaUsu()

        // Mostrar cuenta
        Utils(this, binding.tvCuentaTotal).validarCuentaTotal()
        binding.tvCuentaTotal.setOnClickListener {
            val intent = Intent(this, CuentasActivity::class.java)
            intent.putExtra("activity", "cat")
            startActivityIntent.launch(intent)
        }
    }

    private fun obtenerCategoriaUsu() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerCategorias()
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    val nuevas = call.body()!!.datos
                    listaCategorias.clear()
                    listaCategorias.addAll(nuevas)
                    adaptador.notifyDataSetChanged()
                } else {
                    Toasty.error(
                        this@CategoriasActivityUsu,
                        "Error al obtener categorías",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    override fun verPlatillosCategoria(idCategoria: Int) {
        val intent = Intent(this, PlatillosActivityUsu::class.java)
        intent.putExtra("idCategoria", idCategoria)
        startActivity(intent)
    }
}
