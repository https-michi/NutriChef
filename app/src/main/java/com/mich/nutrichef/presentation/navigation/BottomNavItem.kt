package com.mich.nutrichef.presentation.navigation

sealed class BottomNavItem(val route: String, val title: String) {
    object Inicio : BottomNavItem("inicio", "Inicio")
    object TipsNutricionales : BottomNavItem("tipsNutricionales", "Tips")
    object Perfil : BottomNavItem("perfil", "Perfil")
}
