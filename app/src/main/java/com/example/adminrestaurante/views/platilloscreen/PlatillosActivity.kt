package com.example.adminrestaurante.views.platilloscreen

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityCategoriasBinding
import com.example.adminrestaurante.databinding.ActivityNavegacionBinding
import com.example.adminrestaurante.databinding.ActivityPlatillosBinding
import com.example.adminrestaurante.views.categoriascreen.CategoriasActivity

class PlatillosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlatillosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatillosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cambiar el color de la barra de estado no funciona
        window.statusBarColor = ContextCompat.getColor(this, R.color.gris_oscuro)

        // Configurar el ActionBar correctamente
        supportActionBar?.apply {
            // Añadir un botón de retroceso
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@PlatillosActivity, R.color.valle_dorado)))
            title = "ADMINISTRAR PLATILLOS"
        }
    }

    // Configurar el botón de retroceso A HOME
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}