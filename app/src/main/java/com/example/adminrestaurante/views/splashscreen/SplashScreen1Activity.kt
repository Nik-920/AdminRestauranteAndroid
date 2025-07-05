package com.example.adminrestaurante.views.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivitySplashScreen1Binding
import com.example.adminrestaurante.utils.Constantes
import com.example.adminrestaurante.views.categoriascreen.CategoriasActivityUsu

class SplashScreen1Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreen1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreen1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        Glide.with(this)
            .load(R.drawable.restautant)
            .into(binding.gifLogo)

        cambiarActivityUsu()
    }

    private fun cambiarActivityUsu() {
        // 1) recupera el id que vino desde LoginActivity
        val idUsuario = intent.getIntExtra("idUsuario", 0)

        Handler(Looper.getMainLooper()).postDelayed({
            // 2) al crear el Intent hacia CategoriasActivityUsu, lo re-empaquetas
            Intent(this, CategoriasActivityUsu::class.java).also {
                it.putExtra("idUsuario", idUsuario)
                startActivity(it)
            }
            finish()
        }, Constantes.DURACION_SPLASH_SCREEN)
    }
}
