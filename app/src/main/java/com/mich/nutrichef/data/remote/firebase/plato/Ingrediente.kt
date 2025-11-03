package com.mich.nutrichef.data.remote.firebase.plato

data class Ingrediente(
    val nombre: String = "",
    val cantidadBase: Double = 0.0,
    val cantidadTotal: Double = 0.0,
    val unidad: String = "",
    val costoProducto: Double = 0.0,
    val costoReceta: Double = 0.0
)