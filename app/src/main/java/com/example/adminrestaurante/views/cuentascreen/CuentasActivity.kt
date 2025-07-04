package com.example.adminrestaurante.views.cuentascreen

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityCuentasBinding
import com.example.adminrestaurante.utils.Utils
import es.dmoral.toasty.Toasty

class CuentasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCuentasBinding
    private lateinit var utils: Utils
    private var actividadPrevia: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuentasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar utils con el TextView correcto
        utils = Utils(this, binding.tvCuentaTotal)

        // Leer de qué pantalla vienes (cat o plat)
        actividadPrevia = intent.getStringExtra("activity")

        // Configurar ActionBar
        val titulo = SpannableString("CUENTA")
        titulo.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0, titulo.length, 0
        )
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@CuentasActivity, R.color.amor_secreto)))
            title = titulo
        }

        // Mostrar la cuenta total
        utils.validarCuentaTotal()

        // Mostrar platillos en la tabla
        mostrarCuenta(utils.obtenerPlatillos().split(","))

        // Al hacer clic en pagar/ver cuenta
        binding.tvCuentaTotal.setOnClickListener {
            Toasty.success(this, "Gracias por su compra", Toasty.LENGTH_SHORT).show()
            utils.limpiarCuenta()
            val intent = Intent()
            when (actividadPrevia) {
                "cat", "plat" -> setResult(RESULT_OK, intent)
            }
            finish()
        }
    }

    private fun mostrarCuenta(lista: List<String>) {
        lista.forEach { item ->
            val fila = TableRow(this)
            val tvPlatillo = TextView(this).apply {
                setPadding(12, 12, 12, 12)
                textSize = 20f
                text = item
            }
            fila.addView(tvPlatillo)
            binding.tlCuenta.addView(fila)
        }
    }
}
