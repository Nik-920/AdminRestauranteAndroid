package com.example.adminrestaurante.views.cuentascreen

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.adminrestaurante.R
import com.example.adminrestaurante.databinding.ActivityCuentasBinding
import com.example.adminrestaurante.network.RetrofitClient
import com.example.adminrestaurante.utils.Utils
import com.example.adminrestaurante.views.categoriascreen.CategoriasActivityUsu
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CuentasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuentasBinding
    private lateinit var utils: Utils
    private var actividadPrevia: String? = null
    private var nameToId: Map<String, Int> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuentasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Inicializa Utils
        utils = Utils(this, binding.tvCuentaTotal)

        // 2) Si vienen extras, guarda sólo si idUsuario>0
        val idUsuario = intent.getIntExtra("idUsuario", 0)
        if (idUsuario > 0) {
            utils.setIdUsuario(idUsuario)
        }
        actividadPrevia = intent.getStringExtra("activity")

        // 3) Configurar ActionBar con flecha de retroceso
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            val titulo = SpannableString("CUENTA")
            titulo.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this@CuentasActivity, R.color.black)),
                0, titulo.length, 0
            )
            setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this@CuentasActivity, R.color.secondary_variant_color))
            )
            title = titulo
        }

        // 4) Spinner de mesas (1..9)
        val mesas = (1..9).toList()
        ArrayAdapter(this, android.R.layout.simple_spinner_item, mesas).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerMesa.adapter = adapter
        }
        // Selección anterior
        val savedMesa = utils.getNumeroMesa()
        binding.spinnerMesa.setSelection(mesas.indexOf(savedMesa))
        binding.spinnerMesa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: android.view.View?, pos: Int, id: Long) {
                utils.setNumeroMesa(mesas[pos])
            }
            override fun onNothingSelected(p: AdapterView<*>) {}
        }

        // 5) Mostrar total y tabla
        utils.validarCuentaTotal()
        llenarTabla()

        // 6) Cargar map de nombres→idPlatillo para enviar detalles
        cargarMapaPlatillos()

        // 7) Enviar pedido
        binding.tvCuentaTotal.setOnClickListener {
            enviarPedidoCompleto()
        }
    }

    // Construye las filas de la tabla
    private fun llenarTabla() {
        binding.tlCuenta.removeAllViews()
        utils.obtenerPlatillos()
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { texto ->
                TableRow(this).apply {
                    addView(TextView(this@CuentasActivity).apply {
                        setPadding(12, 12, 12, 12)
                        textSize = 18f
                        text = texto
                    })
                    binding.tlCuenta.addView(this)
                }
            }
    }

    // Pre-carga el mapa de nombrePlatillo → idPlatillo
    private fun cargarMapaPlatillos() {
        CoroutineScope(Dispatchers.IO).launch {
            val resp = RetrofitClient.webService.obtenerPlatillos()
            if (resp.isSuccessful && resp.body()?.codigo == "200") {
                nameToId = resp.body()!!.datos.associate { it.nomPlatillo to it.idPlatillos }
            }
        }
    }

    // Agrupa, crea cabecera y detalles, e inserta todo en la BD
    private fun enviarPedidoCompleto() {
        val mesa      = utils.getNumeroMesa()
        val usuarioId = utils.getIdUsuario()
        val total     = utils.obtenerTotalCuenta()

        // 1) Agrupar por nombre → cantidad
        val agrupado = utils.obtenerPlatillos()
            .split(",")
            .map { it.trim().substringBefore("-") }
            .filter(String::isNotEmpty)
            .groupingBy { it }
            .eachCount()

        // 2) Traducir a pares (idPlatillo, cantidad)
        val detalles = agrupado.mapNotNull { (nombre, cnt) ->
            nameToId[nombre]?.let { idPlat -> idPlat to cnt }
        }

        CoroutineScope(Dispatchers.IO).launch {
            // 3) Insertar cabecera con total
            val respCab = RetrofitClient.webService.crearPedido(mesa, usuarioId, total)
            if (respCab.isSuccessful && respCab.body()?.codigo == "200") {
                val idPedido = respCab.body()!!.datos.idPedido
                // 4) Insertar cada detalle
                var ok = true
                detalles.forEach { (idPlat, cantidad) ->
                    val respDet = RetrofitClient.webService.crearDetalle(idPedido, idPlat, cantidad)
                    if (!respDet.isSuccessful || respDet.code() != 200) {
                        ok = false
                    }
                }
                withContext(Dispatchers.Main) {
                    if (ok) {
                        Toasty.success(
                            this@CuentasActivity,
                            "Pedido #$idPedido enviado a mesa $mesa",
                            Toasty.LENGTH_SHORT
                        ).show()
                        utils.limpiarCuenta()
                        setResult(RESULT_OK, Intent())
                        finish()
                    } else {
                        Toasty.error(
                            this@CuentasActivity,
                            "Error al guardar detalles del pedido",
                            Toasty.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toasty.error(
                        this@CuentasActivity,
                        "Error al crear pedido",
                        Toasty.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // Manejo de flecha de retroceso en ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Regresar a CategoriasActivityUsu, pasando idUsuario y reiniciando
            val intent = Intent(this, CategoriasActivityUsu::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("idUsuario", utils.getIdUsuario())
            }
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
