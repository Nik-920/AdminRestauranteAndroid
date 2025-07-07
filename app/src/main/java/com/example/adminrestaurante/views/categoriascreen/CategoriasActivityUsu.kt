package com.example.adminrestaurante.views.categoriascreen

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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
    private lateinit var utils: Utils
    private val listaCategorias = ArrayList<Categoria>()

    // Usamos un launcher para capturar cuando regresamos de CuentasActivity
    private val startCuentaLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Siempre re-lee el total al volver
            utils.validarCuentaTotal()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasUsuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Instanciar Utils con el TextView de cuenta total
        utils = Utils(this, binding.tvCuentaTotal)

        // 2) Leer idUsuario del Intent y guardarlo
        intent.getIntExtra("idUsuario", 0).takeIf { it > 0 }?.let {
            utils.setIdUsuario(it)
        }

        // 3) Configurar ActionBar
        val titulo = SpannableString("D'ZUASOS : RESTAURANTE")
        titulo.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, titulo.length, 0
        )
        supportActionBar?.apply {
            setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this@CategoriasActivityUsu, R.color.amor_secreto))
            )
            title = titulo
            //setDisplayHomeAsUpEnabled(true)
        }

        // 4) RecyclerView
        adaptador = AdaptadorCategoriaUsu(this, listaCategorias, this)
        binding.rvCategorias.layoutManager = GridLayoutManager(this, 2)
        binding.rvCategorias.adapter = adaptador

        // 5) Cargar categorías
        obtenerCategoriaUsu()

        // 6) Mostrar el total actual
        utils.validarCuentaTotal()

        // 7) Lanzar CuentasActivity con resultado
        binding.tvCuentaTotal.setOnClickListener {
            val intent = Intent(this, CuentasActivity::class.java).apply {
                putExtra("activity", "cat")
                putExtra("idUsuario", utils.getIdUsuario())
            }
            startCuentaLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Aseguramos refrescar el total al regresar de PlatillosActivityUsu
        utils.validarCuentaTotal()
    }

    private fun obtenerCategoriaUsu() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerCategorias()
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    listaCategorias.clear()
                    listaCategorias.addAll(call.body()!!.datos)
                    adaptador.notifyDataSetChanged()
                } else {
                    Toasty.error(
                        this@CategoriasActivityUsu,
                        "Error al obtener categorías",
                        Toasty.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun verPlatillosCategoria(idCategoria: Int) {
        // Ahora usamos launcher al abrir Platillos para que onResume() detecte el regreso
        val intent = Intent(this, PlatillosActivityUsu::class.java).apply {
            putExtra("idCategoria", idCategoria)
            putExtra("idUsuario", utils.getIdUsuario())
        }
        startActivity(intent)
    }
}
