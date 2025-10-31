package com.mich.nutrichef.data.remote.firebase

import java.util.Date

data class UserProfile(
    val idUsuario: String,
    val nombre: String,
    val email: String,
    val foto: String = "",
    val edad: Int? = null,
    val peso: Double? = null,
    val altura: Double? = null,
    val platoMasVisto: String? = null,
    val cantPlatosVistos: Int = 0,
    val favoritos: List<String> = emptyList(),
    val fechaRegistro: Date = Date(),
    val ultimaConexion: Date = Date()
)
