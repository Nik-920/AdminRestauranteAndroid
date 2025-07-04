package com.example.adminrestaurante.utils

import android.content.Context
import android.widget.TextView
import kotlin.apply
import kotlin.text.isEmpty
import kotlin.text.toDouble

class Utils(private val context: Context, private val tvCuentaTotal: TextView) {
    private val prefs = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)

    fun agregarPlatilloCuenta(nom: String, precio: Double) {
        val prevList = prefs.getString("platillos", "") ?: ""
        val newList = if (prevList.isEmpty()) "$nom-$precio" else "$prevList, $nom-$precio"
        val total = prefs.getString("total", "0")!!.toDouble().plus(precio)
        prefs.edit().apply {
            putString("platillos", newList)
            putString("total", total.toString())
            apply()
        }
        validarCuentaTotal()
    }

    fun obtenerPlatillos(): String = prefs.getString("platillos", "") ?: ""
    fun obtenerTotalCuenta(): Double = prefs.getString("total", "0")!!.toDouble()

    fun validarCuentaTotal() {
        val total = obtenerTotalCuenta()
        tvCuentaTotal.text = if (total > 0)
            "VER MI CUENTA: S/($total)"
        else
            "PAGAR CUENTA: S/($total)"
    }

    fun limpiarCuenta() {
        prefs.edit().clear().apply()
        validarCuentaTotal()
    }
}
