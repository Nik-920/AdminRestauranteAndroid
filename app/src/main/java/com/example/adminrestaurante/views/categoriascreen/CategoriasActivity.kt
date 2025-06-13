package com.example.adminrestaurante.views.categoriascreen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityCategoriasBinding
import com.example.adminrestaurante.databinding.ActivityNavegacionBinding

class CategoriasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}