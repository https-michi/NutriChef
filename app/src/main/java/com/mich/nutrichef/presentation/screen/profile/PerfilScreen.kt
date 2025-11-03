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
import com.mich.nutrichef.data.remote.firebase.AuthState
import com.mich.nutrichef.data.remote.firebase.AuthViewModel
import com.mich.nutrichef.data.remote.firebase.UserViewModel

@Composable
fun PerfilScreen(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onLogout: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val userName by userViewModel.userName.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (authState) {
            is AuthState.Unauthenticated -> {
                LaunchedEffect(Unit) {
                    onLogout()
                }
                CircularProgressIndicator()
            }

            is AuthState.Authenticated -> {
                val user = (authState as AuthState.Authenticated).user

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Bienvenido, $userName",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Button(onClick = onLogout) {
                        Text("Cerrar Sesión")
                    }

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Debug Info:", style = MaterialTheme.typography.labelLarge)
                            Text("UID: ${user.uid}")
                            Text("Email: ${user.email}")
                            Text("Nombre: $userName")
                        }
                    }
                }
            }

            AuthState.Loading -> {
                CircularProgressIndicator()
            }

            is AuthState.Error -> {
                Text("Error: ${(authState as AuthState.Error).message}")
            }
        }
    }
}