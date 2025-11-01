package com.mich.nutrichef.presentation.navigation


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.firebase.auth.FirebaseAuth
import com.mich.nutrichef.presentation.screen.home.InicioScreen
import com.mich.nutrichef.presentation.screen.home.MainScreen
import com.mich.nutrichef.presentation.screen.login.LoginScreen
import com.mich.nutrichef.presentation.screen.onboarding.OnboardingScreen
import com.mich.nutrichef.presentation.screen.profile.PerfilScreen
import com.mich.nutrichef.presentation.screen.register.RegisterScreen
import com.mich.nutrichef.presentation.screen.tips.TipsNutricionalesScreen

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
            OnboardingScreen(onFinish = { navController.navigate("login") })
        }

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


private fun NavGraphBuilder.mainGraph(rootNavController: NavHostController) {
    composable("main") {
        MainScreen(rootNavController = rootNavController)
    }
}

//private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
//    navigation(startDestination = "inicio", route = "main") {
//        composable("inicio") { InicioScreen() }
//        composable("tipsNutricionales") { TipsNutricionalesScreen() }
//        composable("perfil") { PerfilScreen(navController) }
//    }
//}

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

//private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
//    composable("main") { MainScreen() }

//@Composable
//fun AppNavigation(
//    startDestination: String = "auth"
//) {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = startDestination
//    ) {
//        authGraph(navController)
//        mainGraph(navController)
//    }
//}

///////////////////////////////////////////////////////////////////////////////////////

//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//
//    var isLoading by remember { mutableStateOf(true) }
//    var currentUser by remember { mutableStateOf<FirebaseUser?>(null) }
//
//    // Esperar a que Firebase Auth inicialice
//    DisposableEffect(Unit) {
//        val authListener = FirebaseAuth.AuthStateListener { auth ->
//            currentUser = auth.currentUser
//            isLoading = false
//            Log.d("NAVIGATIONNNN", "Auth initialized - currentUser: $currentUser")
//            Log.d("NAVIGATIONNNN", "Email: ${currentUser?.email}")
//        }
//
//        FirebaseAuth.getInstance().addAuthStateListener(authListener)
//
//        // Trigger inmediato por si ya está listo
//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null && user.email != null) {
//            currentUser = user
//            isLoading = false
//        }
//
//        onDispose {
//            FirebaseAuth.getInstance().removeAuthStateListener(authListener)
//        }
//    }
//
//    // Pantalla de carga mientras Firebase inicializa
//    if (isLoading) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//        return
//    }
//
//    val startDest = if (currentUser != null) "main" else "auth"
//    Log.d("NAVIGATIONNNN", "Starting at: $startDest")
//
//    NavHost(
//        navController = navController,
//        startDestination = startDest
//    ) {
//        authGraph(navController)
//        mainGraph(navController)
//    }
//}
//
