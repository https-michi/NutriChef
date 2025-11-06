package com.mich.nutrichef.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mich.nutrichef.data.remote.firebase.AuthViewModel
import com.mich.nutrichef.data.remote.firebase.PlatoViewModel
import com.mich.nutrichef.data.remote.firebase.UserViewModel
import com.mich.nutrichef.presentation.navigation.BottomNavigationBar
import com.mich.nutrichef.presentation.screen.profile.BodyDataScreen
import com.mich.nutrichef.presentation.screen.profile.PerfilScreen
import com.mich.nutrichef.presentation.screen.tips.TipsNutricionalesScreen

@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel = viewModel(),
    platoViewModel: PlatoViewModel = viewModel(),
    onLogout: () -> Unit
) {
    var isProfileComplete by remember { mutableStateOf<Boolean?>(null) }
    var isCheckingProfile by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        authViewModel.isProfileComplete { isComplete ->
            isProfileComplete = isComplete
            isCheckingProfile = false
        }
    }

    when {
        isCheckingProfile -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF5CD6C8))
            }
        }

        isProfileComplete == false -> {
            BodyDataScreen(
                authViewModel = authViewModel,
                onContinue = { weight, height ->
                    authViewModel.completeProfile(weight.toDouble(), height.toDouble()) { result ->
                        if (result.isSuccess) {
                            isProfileComplete = true
                        }
                    }
                }
            )
        }

        else -> {
            MainScreenContent(
                authViewModel = authViewModel,
                userViewModel = userViewModel,
                platoViewModel = platoViewModel,
                onLogout = onLogout
            )
        }
    }
}

@Composable
private fun MainScreenContent(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    platoViewModel: PlatoViewModel,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val platoSeleccionado by platoViewModel.platoSeleccionado.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(platoSeleccionado) {
        if (platoSeleccionado != null) {
            navController.navigate("detalle_plato")
        }
    }

//    val shouldShowBottomBar = currentRoute != "detalle_plato"
    val shouldShowBottomBar = currentRoute !in listOf("detalle_plato", "pasos_plato")


    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(navController)
            }
        }
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
                        },
                        onVerProcesoClick = {
                            navController.navigate("pasos_plato")
                        }
                    )
                }
            }

            composable("pasos_plato") {
                platoSeleccionado?.let { plato ->
                    PasosPlatoScreen(
                        plato = plato,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }

        }
    }
}