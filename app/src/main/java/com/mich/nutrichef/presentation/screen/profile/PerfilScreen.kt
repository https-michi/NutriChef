package com.mich.nutrichef.presentation.screen.profile

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavController


@Composable
fun PerfilScreen(
    navController: NavController,
    rootNavController: NavController
) {
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    var isLoading by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val authListener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
            isLoading = false
            Log.d("PERFILLLL", "Auth listener - currentUser: $currentUser")
            Log.d("PERFILLLL", "Auth listener - email: ${currentUser?.email}")
            Log.d("PERFILLLL", "Auth listener - displayName: ${currentUser?.displayName}")
        }

        FirebaseAuth.getInstance().addAuthStateListener(authListener)
        currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null && currentUser?.email != null) {
            isLoading = false
        }

        onDispose {
            FirebaseAuth.getInstance().removeAuthStateListener(authListener)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            currentUser == null -> {
                LaunchedEffect(Unit) {
                    rootNavController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            else -> {
                val nombre = currentUser?.displayName?.takeIf { it.isNotBlank() }
                    ?: currentUser?.email
                    ?: "Usuario"

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Bienvenido, $nombre",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            rootNavController.navigate("auth") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Text("Cerrar Sesión")
                    }

                    Card(modifier = Modifier.padding(16.dp)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Debug Info:", style = MaterialTheme.typography.labelLarge)
                            Text("UID: ${currentUser?.uid}")
                            Text("Email: ${currentUser?.email}")
                            Text("DisplayName: ${currentUser?.displayName}")
                        }
                    }
                }
            }
        }
    }
}