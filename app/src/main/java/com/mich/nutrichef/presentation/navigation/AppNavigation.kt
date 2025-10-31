package com.mich.nutrichef.presentation.navigation

import com.mich.nutrichef.presentation.screen.home.MainScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.mich.nutrichef.presentation.screen.login.LoginScreen
import com.mich.nutrichef.presentation.screen.onboarding.OnboardingScreen
import com.mich.nutrichef.presentation.screen.register.RegisterScreen

@Composable
fun AppNavigation(
    startDestination: String = "auth"
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController)
        mainGraph(navController)
    }
}


private fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(startDestination = "onboarding", route = "auth") {
        composable("onboarding") {
            OnboardingScreen(onFinish = { navController.navigate("login") })
        }
//        composable("login") {
//            LoginScreen(onLoginSuccess = {
//                navController.navigate("main") {
//                    popUpTo("auth") { inclusive = true }
//                }
//            })
//        }
//        composable("register") {
//            RegisterScreen(onRegisterSuccess = {
//                navController.navigate("main") {
//                    popUpTo("auth") { inclusive = true }
//                }
//            })
//        }
        composable("login") {
            LoginScreen(
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
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable("main") { MainScreen() }
}