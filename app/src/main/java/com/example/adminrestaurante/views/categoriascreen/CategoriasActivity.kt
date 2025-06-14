package com.example.adminrestaurante.views.categoriascreen

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityCategoriasBinding
import android.graphics.drawable.ColorDrawable
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adminrestaurante.models.Categoria
import com.example.adminrestaurante.network.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriasActivity : AppCompatActivity(),AdaptadorCategoria.OnItemClicked {

    private lateinit var binding: ActivityCategoriasBinding
    lateinit var listaCategorias: ArrayList<Categoria>
    lateinit var adaptadorCategoria: AdaptadorCategoria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cambiar el color de la barra de estado (forma recomendada)
        //window.statusBarColor = ContextCompat.getColor(this, R.color.gris_oscuro)

        // Configurar el ActionBar correctamente
        supportActionBar?.apply {
            // Añadir un botón de retroceso
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@CategoriasActivity, R.color.valle_dorado)))
            title = "ADMINISTRAR CATEGORÍAS"
        }

        // Configurar el RecyclerView y el adaptador
        obtenerCategorias()

        // Configurar el botón de agregar
        binding.fab.setOnClickListener {
            formularioAdd()
        }
    }

    // MOSTRAR EL FORMULARIO PARA AGREGAR UNA CATEGORIA EN EL SERVIDOR
    private fun formularioAdd() {
        val builder = AlertDialog.Builder(this)
        val inflate = this.layoutInflater
        var vista = inflate.inflate(R.layout.alert_dialog_add_categoria, null)
        builder.setView(vista)

        var etNomCategoria = vista.findViewById<EditText>(R.id.etNomCategoria) as EditText

        // Aceptar el formulario
        builder.setPositiveButton("ACEPTAR") { dialog, id ->
            //Toast.makeText(this@CategoriasActivity, "ACEPTAR", Toast.LENGTH_SHORT).show()
            if (!(etNomCategoria.text.toString().trim().isNullOrEmpty())){
                agregarCategoria(etNomCategoria.text.toString().trim())
            } else {
                Toasty.error(this@CategoriasActivity, "Debe ingresar un nombre de categoría", Toast.LENGTH_SHORT).show()
            }
        }
        // Cancelar el formulario
        builder.setNegativeButton("CANCELAR") { dialog, id ->
            //Toast.makeText(this@CategoriasActivity, "CANCELAR", Toast.LENGTH_SHORT).show()
        }
        // Crear el AlertDialog y mostrarlo en pantalla
        builder.create()
        builder.setCancelable(false)
        builder.show()
    }


    private fun agregarCategoria(nomCategoria: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.agregarCategoria(nomCategoria)
            runOnUiThread {
                if (call.isSuccessful){
                    if (call.body()!!.codigo == "200"){
                        Toasty.success(this@CategoriasActivity, "Categoría agregada correctamente", Toast.LENGTH_SHORT).show()
                        obtenerCategorias()
                    } else {
                        Toasty.error(this@CategoriasActivity, "Error en el servidor Y BODY", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toasty.error(this@CategoriasActivity, "Error en el servidor de la consulta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /*
    *  OBTENER LAS CATEGORIAS DEL SERVIDOR
    *  Y MOSTRARLAS EN EL RECYCLERVIEW
    *  CON UN ADAPTER Y UN VIEW HOLDER PARA CADA ITEM
    *  DE LA LISTA DE CATEGORIAS OBTENIDA DEL SERVIDOR Y MOSTRARLAS EN EL RECYCLERVIEW
    * */
    private fun obtenerCategorias(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerCategorias()
            runOnUiThread {
                if (call.isSuccessful){
                    if (call.body()!!.codigo == "200"){
                        listaCategorias = call.body()!!.datos
                        setupRecyclerView()
                    } else {
                        Toasty.error(this@CategoriasActivity, "Error en el servidor Y BODY", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toasty.error(this@CategoriasActivity, "Error en el servidor de la consulta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /*
    *  CONFIGURAR EL RECYCLERVIEW Y EL ADAPTER
    *  PARA MOSTRAR LAS CATEGORIAS EN EL RECYCLERVIEW */
    private fun setupRecyclerView() {
        // Configurar el RecyclerView y el adaptador
        // ...
        val layoutManager = GridLayoutManager(this,2)
        binding.rvPlatillos.layoutManager = layoutManager
        adaptadorCategoria = AdaptadorCategoria(this, listaCategorias, this)
        binding.rvPlatillos.adapter = adaptadorCategoria
    }

    /*
    *  GESTIONAR EL BOTON DE RETROCESO DEL ACTION BAR
    *  PARA VOLVER A LA PANTALLA ANTERIOR */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    /*
    *  GESTIONAR EL CLICK EN UN ITEM DEL RECYCLERVIEW
    *  PARA MOSTRAR UN MENSAJE CON EL NOMBRE DE LA CATEGORIA*/
    override fun borrarCategoria(nomCategoria: String) {
        //Toast.makeText(this, "Borrando $nomCategoria", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.borrarCategoria(nomCategoria)
            runOnUiThread {
                if (call.isSuccessful){
                    if (call.body()!!.codigo == "200"){
                        Toasty.success(this@CategoriasActivity, "Categoría borrada correctamente, actualizando...", Toast.LENGTH_SHORT).show()
                        obtenerCategorias()
                    } else {
                            Toasty.error(this@CategoriasActivity, "Error en el servidor Y BODY", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toasty.error(this@CategoriasActivity, "Error en el servidor de la consulta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}