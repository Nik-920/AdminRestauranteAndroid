package com.example.adminrestaurante.views.platilloscreen

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityPlatillosBinding
import com.example.adminrestaurante.models.Platillos
import com.example.adminrestaurante.network.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlatillosActivity : AppCompatActivity(), AdaptadorPlatillo.OnItemClicked {
    private lateinit var binding: ActivityPlatillosBinding
    private lateinit var adaptadorPlatillo: AdaptadorPlatillo
    private lateinit var listaSpinnerCategorias: List<Pair<Int, String>>
    private var isEditando = false
    private var platilloSeleccionado: Platillos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatillosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@PlatillosActivity, R.color.green)))
            title = "ADMINISTRAR PLATILLOS"
        }

        // Inicializar RecyclerView y adaptador con lista vacía
        adaptadorPlatillo = AdaptadorPlatillo(mutableListOf(), this)
        binding.rvPlatillos.layoutManager = LinearLayoutManager(this)
        binding.rvPlatillos.adapter = adaptadorPlatillo

        obtenerListaCategorias()
        obtenerPlatillos()

        binding.fab.setOnClickListener { formAddUpdate() }
    }

    private fun obtenerListaCategorias() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerCategorias()
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    listaSpinnerCategorias = call.body()!!.datos.map { it.idCategoria to it.nomCategoria }
                } else {
                    Toasty.error(this@PlatillosActivity, "Error al cargar categorías", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun formAddUpdate() {
        val builder = AlertDialog.Builder(this)
        val vista = layoutInflater.inflate(R.layout.alert_dialog_add_update_platillo, null)
        builder.setView(vista)

        val etNombre = vista.findViewById<EditText>(R.id.etNomPlatillo)
        val etDesc = vista.findViewById<EditText>(R.id.etDescripcionPlatillo)
        val etPrecio = vista.findViewById<EditText>(R.id.etPrecio)
        val spiCat = vista.findViewById<Spinner>(R.id.spiCategorias)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            listaSpinnerCategorias.map { it.second })
        spiCat.adapter = adapter

        platilloSeleccionado?.let {
            isEditando = true
            etNombre.setText(it.nomPlatillo)
            etNombre.isEnabled = false
            etDesc.setText(it.descripcionPlatillo)
            etPrecio.setText(it.precio.toString())
            spiCat.setSelection(listaSpinnerCategorias.indexOfFirst { pair -> pair.first == it.idCategoria })
        }

        builder.setPositiveButton("ACEPTAR") { _, _ ->
            val nombre = etNombre.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val idCat = listaSpinnerCategorias[spiCat.selectedItemPosition].first

            if (nombre.isNotEmpty() && precio > 0) {
                val nuevo = Platillos(
                    idPlatillos = platilloSeleccionado?.idPlatillos ?: 0,
                    nomPlatillo = nombre,
                    descripcionPlatillo = desc,
                    precio = precio,
                    idCategoria = idCat
                )
                if (isEditando) actualizarPlatillo(nuevo) else crearPlatillo(nuevo)
                platilloSeleccionado = null
            } else {
                Toasty.error(this, "Campos incompletos", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("CANCELAR", null)
        builder.setCancelable(false)
        builder.show()
    }

    private fun crearPlatillo(p: Platillos) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.agregarPlatillo(p)
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    Toasty.success(this@PlatillosActivity, "Platillo creado", Toast.LENGTH_SHORT).show()
                    obtenerPlatillos()
                } else {
                    Toasty.error(this@PlatillosActivity, "Error al crear platillo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun actualizarPlatillo(p: Platillos) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.actualizarPlatillo(p.idPlatillos, p)
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    Toasty.success(this@PlatillosActivity, "Platillo actualizado", Toast.LENGTH_SHORT).show()
                    obtenerPlatillos()
                } else {
                    Toasty.error(this@PlatillosActivity, "Error al actualizar platillo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtenerPlatillos() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerPlatillos()
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    val datos = call.body()!!.datos
                    adaptadorPlatillo.updateData(datos)
                } else {
                    Toasty.error(this@PlatillosActivity, "Error al cargar platillos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun editarPlatillo(platillo: Platillos) {
        platilloSeleccionado = platillo
        formAddUpdate()
    }

    override fun borrarPlatillo(idPlatillo: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.borrarPlatillo(idPlatillo)
            runOnUiThread {
                if (call.isSuccessful && call.body()?.codigo == "200") {
                    Toasty.success(this@PlatillosActivity, "Platillo eliminado", Toast.LENGTH_SHORT).show()
                    obtenerPlatillos()
                } else {
                    Toasty.error(this@PlatillosActivity, "Error al eliminar platillo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}