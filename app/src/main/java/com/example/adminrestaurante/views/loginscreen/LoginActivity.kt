package com.example.adminrestaurante.views.loginscreen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminrestaurante.R
import com.example.adminrestaurante.network.RetrofitClient
import com.example.adminrestaurante.views.registerscreen.RegisterActivity
import com.example.adminrestaurante.views.splashscreen.SplashScreen1Activity
import com.example.adminrestaurante.views.splashscreen.SplashScreenActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.buttonEnter)
        btnRegister = findViewById(R.id.txtRegistrar)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUsuario(email, password)
            } else {
                Toasty.warning(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUsuario(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.webService.obtenerUsuarios()
            runOnUiThread {
                if (response.isSuccessful && response.body() != null) {
                    val usuarios = response.body()!!.datos

                    // 1) Si es el ADMIN hardcodeado
                    if (email.equals("Admin@gmail.com", ignoreCase = true) && password == "123456789") {
                        Toasty.success(this@LoginActivity, "Bienvenido ADMIN", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, SplashScreenActivity::class.java))
                        finish()
                        return@runOnUiThread
                    }

                    // 2) Si es un usuario “normal” que existe en la BD
                    val user = usuarios.find { it.gmail.equals(email, true) && it.pasword == password }
                    if (user != null) {
                        Toasty.success(this@LoginActivity, "Bienvenido, ${user.nombreUsuario}", Toast.LENGTH_SHORT).show()
                        // Le pasamos su idUsuario para propagarlo a las pantallas de usuario
                        val intent = Intent(this@LoginActivity, SplashScreen1Activity::class.java)
                            .putExtra("idUsuario", user.idUsuario)
                        startActivity(intent)
                        finish()
                    } else {
                        Toasty.error(this@LoginActivity, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toasty.error(this@LoginActivity, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
