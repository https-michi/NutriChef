package com.mich.nutrichef.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.firebase.auth.FirebaseAuth
import com.mich.nutrichef.data.remote.firebase.AuthState
import com.mich.nutrichef.data.remote.firebase.AuthViewModel
import com.mich.nutrichef.presentation.screen.home.MainScreen
import com.mich.nutrichef.presentation.screen.login.LoginScreen
import com.mich.nutrichef.presentation.screen.onboarding.OnboardingScreen
import com.mich.nutrichef.presentation.screen.profile.BodyDataScreen
import com.mich.nutrichef.presentation.screen.register.RegisterScreen

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    val startDest = when (authState) {
        is AuthState.Authenticated -> "main"
        is AuthState.Unauthenticated -> "auth"
        AuthState.Loading -> "auth"
        is AuthState.Error -> "auth"
    }

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        authGraph(navController, authViewModel)
        mainGraph(navController, authViewModel)
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    navigation(startDestination = "onboarding", route = "auth") {
        composable("onboarding") {
            OnboardingScreen(
                onFinish = { navController.navigate("login") }
            )
        }

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
//                    navController.navigate("complete_profile") {
//                        popUpTo("auth") { inclusive = true }
//                    }
                }
            )
        }

//        composable("complete_profile") {
//            BodyDataScreen(
//                authViewModel = authViewModel,
//                onContinue = { weight, height ->
//                    navController.navigate("main") {
//                        popUpTo("complete_profile") { inclusive = true }
//                    }
//                }
//            )
//        }
    }
}

private fun NavGraphBuilder.mainGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    composable("main") {
        MainScreen(
            authViewModel = authViewModel,
            onLogout = {
                authViewModel.logout()
                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}