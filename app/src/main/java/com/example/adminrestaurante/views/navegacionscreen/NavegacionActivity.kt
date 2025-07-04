package com.example.adminrestaurante.views.navegacionscreen
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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