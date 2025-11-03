package com.mich.nutrichef.data.remote.firebase.plato

data class Plato(
    val idPlato: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val imagen: String = "",
    val categoria: String = "",
    val porcionesBase: Int = 0,
    val tiempoPreparacion: Int = 0,
    val nivelDificultad: String = "",
    val ingredientes: List<Ingrediente> = emptyList(),
    val pasos: List<String> = emptyList(),
    val valorNutricional: ValorNutricional = ValorNutricional(),
    val presupuesto: Presupuesto = Presupuesto(),
    val vistas: Int = 0,
    val favoritos: Int = 0,
    val creadorId: String = "",
    val fechaCreacion: String = "",
    val ultimaActualizacion: String = ""
)