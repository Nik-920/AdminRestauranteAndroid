package com.example.adminrestaurante.views.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivitySplashScreenBinding
import com.example.adminrestaurante.utils.Constantes
import com.example.adminrestaurante.views.navegacionscreen.NavegacionActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ocultar el Menu de Notificaciones
        // Ocultar el Tulbar
        supportActionBar?.hide()
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Gif de Portada Principal
        Glide
            .with(this)
            .load(R.drawable.restautant)
            .into(binding.gifLogo)

        cambiarActivity()
    }

    fun cambiarActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, NavegacionActivity::class.java)
            startActivity(intent)
            finish()
        }, Constantes.DURACION_SPLASH_SCREEN)
    }

}