package com.example.adminrestaurante.views.navegacionscreen
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.adminrestaurante.databinding.ActivityNavegacionBinding
import com.example.adminrestaurante.views.categoriascreen.CategoriasActivity
import com.example.adminrestaurante.views.pedidoscreen.PedidosActivityAdmin
import com.example.adminrestaurante.views.platilloscreen.PlatillosActivity
import com.example.adminrestaurante.views.usuarioscreen.UsuariosActivityAdmin

class NavegacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavegacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavegacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.cvCategorias.setOnClickListener {
            startActivity(Intent(this, CategoriasActivity::class.java))
        }
        binding.cvPlatillos.setOnClickListener {
            startActivity(Intent(this, PlatillosActivity::class.java))
        }
        binding.cvPedidos.setOnClickListener {
            startActivity(Intent(this, PedidosActivityAdmin::class.java))
        }
        binding.cvUsuarios.setOnClickListener {
            startActivity(Intent(this, UsuariosActivityAdmin::class.java))
        }
    }
}
