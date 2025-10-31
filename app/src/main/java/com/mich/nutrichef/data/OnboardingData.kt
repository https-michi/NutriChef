package com.mich.nutrichef.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.CheckCircle
import com.mich.nutrichef.model.OnboardingPage

object OnboardingData {
    val pages = listOf(
        OnboardingPage(
            titulo = "Descubre el valor nutricional",
            descripcion = "Conoce las calorías, proteínas y nutrientes de tus platos favoritos",
            icon = Icons.Default.Star
        ),
        OnboardingPage(
            titulo = "Recetas saludables",
            descripcion = "Aprende a preparar platos deliciosos y nutritivos paso a paso",
            icon = Icons.Default.Favorite
        ),
        OnboardingPage(
            titulo = "Come mejor, vive mejor",
            descripcion = "Mantén hábitos alimenticios saludables y alcanza tus objetivos",
            icon = Icons.Default.CheckCircle
        )
    )
}