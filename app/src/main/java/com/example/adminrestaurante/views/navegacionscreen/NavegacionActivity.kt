package com.example.adminrestaurante.views.navegacionscreen

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityNavegacionBinding
import com.example.adminrestaurante.views.categoriascreen.CategoriasActivity
import com.example.adminrestaurante.views.platilloscreen.PlatillosActivity

class NavegacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavegacionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavegacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        // Cambiar el color de la barra de estado (forma recomendada)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        binding.cvCategorias.setOnClickListener {
            var intent = Intent(this, CategoriasActivity::class.java)
            startActivity(intent)
        }

        binding.cvPlatillos.setOnClickListener {
            var intent = Intent(this, PlatillosActivity::class.java)
            startActivity(intent)
        }
    }
}