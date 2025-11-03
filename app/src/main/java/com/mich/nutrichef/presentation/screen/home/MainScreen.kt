package com.mich.nutrichef.presentation.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mich.nutrichef.data.remote.firebase.AuthViewModel
import com.mich.nutrichef.data.remote.firebase.PlatoViewModel
import com.mich.nutrichef.data.remote.firebase.UserViewModel
import com.mich.nutrichef.presentation.navigation.BottomNavigationBar
import com.mich.nutrichef.presentation.screen.profile.PerfilScreen
import com.mich.nutrichef.presentation.screen.tips.TipsNutricionalesScreen

//@Composable
//fun MainScreen(onLogout: () -> Unit) {
//    val navController = rememberNavController()
//
//    Scaffold(
//        bottomBar = { BottomNavigationBar(navController) }
//    ) { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = "inicio",
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable("inicio") { InicioScreen() }
//            composable("tipsNutricionales") { TipsNutricionalesScreen() }
//            composable("perfil") {
//                PerfilScreen(onLogout = onLogout)
//            }
//        }
//    }
//}
@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel = viewModel(),
    platoViewModel: PlatoViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val platoSeleccionado by platoViewModel.platoSeleccionado.collectAsState()

    LaunchedEffect(platoSeleccionado) {
        if (platoSeleccionado != null) {
            navController.navigate("detalle_plato")
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("inicio") {
                InicioScreen(
                    authViewModel = authViewModel,
                    userViewModel = userViewModel,
                    platoViewModel = platoViewModel,
                    onNavigateToDetalle = { plato ->
                        platoViewModel.seleccionarPlato(plato)
                    }
                )
            }

            composable("tipsNutricionales") {
                TipsNutricionalesScreen()
            }

            composable("perfil") {
                PerfilScreen(
                    authViewModel = authViewModel,
                    userViewModel = userViewModel,
                    onLogout = onLogout
                )
            }
            composable("detalle_plato") {
                platoSeleccionado?.let { plato ->
                    DetallePlatoScreen(
                        plato = plato,
                        onBackClick = {
                            platoViewModel.limpiarPlatoSeleccionado()
                            navController.popBackStack()
                        },
                        onFavoriteClick = {
                            // TODO: Implementar favoritos
                        }
                    )
                }
            }
        }
    }
}
