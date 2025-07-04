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
import kotlin.jvm.java

class SplashScreen1Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreen1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreen1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ocultar Tullbar
        supportActionBar?.hide()
        Glide
            .with(this)
            .load(R.drawable.restautant)
            .into(binding.gifLogo)

        cambiarActivityUsu()

    }

    fun cambiarActivityUsu(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, CategoriasActivityUsu::class.java)
            startActivity(intent)
            finish()
        }, Constantes.DURACION_SPLASH_SCREEN)
    }
}