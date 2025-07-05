package com.example.adminrestaurante.views.usuarioscreen

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminrestaurante.databinding.ActivityUsuariosBinding
import com.example.adminrestaurante.models.Usuario
import com.example.adminrestaurante.network.RetrofitClient
import com.example.adminrestaurante.views.navegacionscreen.NavegacionActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuariosActivityAdmin : AppCompatActivity(), AdaptadorUsuarioAdmin.OnItemClicked {

    private lateinit var binding: ActivityUsuariosBinding
    private lateinit var adapter: AdaptadorUsuarioAdmin
    private val listaUsuarios = mutableListOf<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar ActionBar con botón de retroceso
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "ADMINISTRAR USUARIOS"
        }

        // 1) Inicializar RecyclerView con adapter vacío
        adapter = AdaptadorUsuarioAdmin(listaUsuarios, this)
        binding.rvUsuarios.layoutManager = LinearLayoutManager(this)
        binding.rvUsuarios.adapter = adapter

        // 2) Cargar todos los usuarios
        cargarUsuarios()

        // 3) Filtrar
        binding.btnAplicarFiltro.setOnClickListener {
            val nombreFilter = binding.etFiltroNombre.text.toString().trim()
            val apellidoFilter = binding.etFiltroApellido.text.toString().trim()
            val correoFilter = binding.etFiltroCorreo.text.toString().trim()
            adapter.filter(nombreFilter, apellidoFilter, correoFilter)
        }
    }

    private fun cargarUsuarios() {
        CoroutineScope(Dispatchers.IO).launch {
            val resp = RetrofitClient.webService.obtenerUsuarios()
            runOnUiThread {
                if (resp.isSuccessful && resp.body()?.codigo == "200") {
                    listaUsuarios.clear()
                    listaUsuarios.addAll(resp.body()!!.datos)
                    adapter.updateData(listaUsuarios)
                } else {
                    Toasty.error(this@UsuariosActivityAdmin, "Error al cargar usuarios", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun editarUsuario(u: Usuario) {
        // Aquí abre diálogo o pantalla de edición
        Toasty.info(this, "Editar usuario: ${u.nombreUsuario}", Toasty.LENGTH_SHORT).show()
    }

    override fun borrarUsuario(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val resp = RetrofitClient.webService.borrarUsuario(id)
            runOnUiThread {
                if (resp.isSuccessful && resp.body()?.codigo == "200") {
                    Toasty.success(this@UsuariosActivityAdmin, "Usuario eliminado", Toasty.LENGTH_SHORT).show()
                    cargarUsuarios()
                } else {
                    Toasty.error(this@UsuariosActivityAdmin, "Error al eliminar usuario", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Maneja el clic en el botón ← del ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Regresar a NavegacionActivity
            val intent = Intent(this, NavegacionActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
