package com.example.adminrestaurante.views.registerscreen


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminrestaurante.R
import com.example.adminrestaurante.models.Usuario
import com.example.adminrestaurante.network.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellidoP: EditText
    private lateinit var etApellidoM: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        etNombre = findViewById(R.id.etNombre)
        etApellidoP = findViewById(R.id.etApellidoP)
        etApellidoM = findViewById(R.id.etApellidoM)
        etTelefono = findViewById(R.id.etTelefono)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.buttonEnter)

        btnRegister.setOnClickListener {
            if (validarCampos()) {
                val usuario = Usuario(
                    idUsuario = 0, // El backend lo asignará
                    nombreUsuario = etNombre.text.toString().trim(),
                    apellidoP = etApellidoP.text.toString().trim(),
                    apellidoM = etApellidoM.text.toString().trim(),
                    telefono = etTelefono.text.toString().trim(),
                    gmail = etEmail.text.toString().trim(),
                    pasword = etPassword.text.toString().trim(),
                )

                registrarUsuario(usuario)
            } else {
                Toasty.error(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario(usuario: Usuario) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.crearUsuario(usuario)
            runOnUiThread {
                if (call.isSuccessful) {
                    val respuesta = call.body()
                    if (respuesta?.codigo == "200") {
                        Toasty.success(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        finish() // o redirige a LoginActivity
                    } else {
                        Toasty.error(this@RegisterActivity, "Error: ${respuesta?.mensaje ?: "Desconocido"}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toasty.error(this@RegisterActivity, "Error del servidor", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validarCampos(): Boolean {
        return etNombre.text!!.isNotEmpty() &&
                etApellidoP.text!!.isNotEmpty() &&
                etApellidoM.text!!.isNotEmpty() &&
                etTelefono.text!!.isNotEmpty() &&
                etEmail.text!!.isNotEmpty() &&
                etPassword.text!!.isNotEmpty()
    }
}
