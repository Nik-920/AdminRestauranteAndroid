package com.example.adminrestaurante.views.platilloscreen
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityPlatillosUsuBinding
import com.example.adminrestaurante.network.RetrofitClient
import com.example.adminrestaurante.utils.Utils
import com.example.adminrestaurante.views.cuentascreen.CuentasActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.apply
import kotlin.jvm.java
import kotlin.text.uppercase

class PlatillosActivityUsu : AppCompatActivity(), AdaptadorPlatillosUsu.OnItemClicked {
    private lateinit var binding: ActivityPlatillosUsuBinding
    private lateinit var adaptadorPlatillos: AdaptadorPlatillosUsu

    private val startActivityIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Utils(this, binding.tvCuentaTotal).validarCuentaTotal()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatillosUsuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idCategoria = intent.getIntExtra("idCategoria", -1)
        val nomCategoria = intent.getStringExtra("nomCategoria") ?: ""

        // Configurar ActionBar
        val titulo = SpannableString("D'ZUASOS MENU: ${nomCategoria.uppercase()}")
        titulo.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)), 0, titulo.length, 0)
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@PlatillosActivityUsu, R.color.amor_secreto)))
            title = titulo
            setDisplayHomeAsUpEnabled(true)
        }

        // Inicializar RecyclerView con adapter vacío
        binding.rvPlatillos.layoutManager = LinearLayoutManager(this)
        adaptadorPlatillos = AdaptadorPlatillosUsu(emptyList(), this)
        binding.rvPlatillos.adapter = adaptadorPlatillos

        // Cargar platillos de la categoría y cuenta actual
        obtenerPlatillosCategoria(idCategoria)

        // Mostrar cuenta
        Utils(this, binding.tvCuentaTotal).validarCuentaTotal()
        binding.tvCuentaTotal.setOnClickListener {
            val intent = Intent(this, CuentasActivity::class.java)
            intent.putExtra("activity", "plat")
            intent.putExtra("idUsuario", Utils(this, null).getIdUsuario())
            startActivityIntent.launch(intent)
        }
    }

    private fun obtenerPlatillosCategoria(idCategoria: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerPlatillosCategoria(idCategoria)
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    val lista = call.body()!!.datos
                    adaptadorPlatillos.updateData(lista)
                } else {
                    Toasty.error(this@PlatillosActivityUsu, "No hay platillos para mostrar", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun agregarPlatilloCuenta(nomPlatillo: String, precioPlatillo: Double) {
        Utils(this, binding.tvCuentaTotal).agregarPlatilloCuenta(nomPlatillo, precioPlatillo)
        Toasty.success(this, "Se agregó $nomPlatillo a la cuenta", Toasty.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
