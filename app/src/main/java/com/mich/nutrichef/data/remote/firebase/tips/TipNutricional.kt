package com.mich.nutrichef.data.remote.firebase.tips

data class TipNutricional(
    val idTip: String = "",
    val titulo: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val iconoNombre: String = "",
    val orden: Int = 0
)
