package com.example.adminrestaurante.utils
import android.content.Context
import android.widget.TextView
class Utils(private val context: Context, private val tvCuentaTotal: TextView?) {
    private val prefs = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)

    /** Agrega un platillo a la cuenta y actualiza total **/
    fun agregarPlatilloCuenta(nom: String, precio: Double) {
        val prevList = prefs.getString("platillos", "") ?: ""
        val newList = if (prevList.isBlank()) "$nom-$precio" else "$prevList, $nom-$precio"
        val total = prefs.getString("total", "0")!!.toDouble() + precio

        prefs.edit()
            .putString("platillos", newList)
            .putString("total", total.toString())
            .apply()

        validarCuentaTotal()
    }

    /** Recupera la lista de platillos guardada **/
    fun obtenerPlatillos(): String =
        prefs.getString("platillos", "") ?: ""

    /** Recupera el total acumulado **/
    fun obtenerTotalCuenta(): Double =
        prefs.getString("total", "0")!!.toDouble()

    /** Muestra el texto de “Enviar Pedido” con el total **/
    fun validarCuentaTotal() {
        tvCuentaTotal?.text = "Enviar Pedido: S/(${String.format("%.2f", obtenerTotalCuenta())})"
    }

    /** Limpia solo los datos de platillos y total; **no toca** mesa o usuario **/
    fun limpiarCuenta() {
        prefs.edit()
            .remove("platillos")
            .remove("total")
            .apply()
        validarCuentaTotal()
    }

    // — Manejo de número de mesa —
    fun setNumeroMesa(mesa: Int) {
        prefs.edit().putInt("numeroMesa", mesa).apply()
    }
    fun getNumeroMesa(): Int =
        prefs.getInt("numeroMesa", 1)

    // — Manejo de idUsuario —
    fun setIdUsuario(id: Int) {
        if (id > 0) {
            prefs.edit().putInt("idUsuario", id).apply()
        }
    }
    fun getIdUsuario(): Int =
        prefs.getInt("idUsuario", 1)  // por defecto 1 si no se estableció

    // en Utils.kt

    /** Reemplaza por completo la lista de platillos y recalcula el total */
    fun setPlatillosYTotal(platillos: List<Pair<String, Double>>) {
        val nuevosStrings = platillos.joinToString(", ") { "${it.first}-${it.second}" }
        val total = platillos.sumOf { it.second }
        prefs.edit()
            .putString("platillos", nuevosStrings)
            .putString("total", total.toString())
            .apply()
        validarCuentaTotal()
    }

}