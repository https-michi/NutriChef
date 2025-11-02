package com.mich.nutrichef.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.firebase.auth.FirebaseAuth
import com.mich.nutrichef.presentation.screen.home.MainScreen
import com.mich.nutrichef.presentation.screen.login.LoginScreen
import com.mich.nutrichef.presentation.screen.onboarding.OnboardingScreen
import com.mich.nutrichef.presentation.screen.profile.BodyDataScreen
import com.mich.nutrichef.presentation.screen.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser

    Log.d("NAVIGATIONNNN", "currentUser: $currentUser")

    val startDest = if (currentUser != null) "main" else "auth"

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        authGraph(navController)
        mainGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(startDestination = "onboarding", route = "auth") {
        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onRegisterSuccess = {
                    navController.navigate("complete_profile") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("complete_profile") {
            BodyDataScreen(
                onContinue = { weight, height ->
                    navController.navigate("main") {
                        popUpTo("complete_profile") { inclusive = true }
                    }
                }
            )
        }

//        composable("register") {
//            RegisterScreen(
//                onNavigateToLogin = {
//                    navController.navigate("login")
//                },
//                onRegisterSuccess = {
//                    navController.navigate("main") {
//                        popUpTo("auth") { inclusive = true }
//                    }
//                }
//            )
//        }
    }
}

private fun NavGraphBuilder.mainGraph(navController: NavController) {
    composable("main") {
        MainScreen(
            onLogout = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}