package com.mich.nutrichef.presentation.screen.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mich.nutrichef.data.remote.firebase.AuthViewModel
import com.mich.nutrichef.data.remote.firebase.FirebaseAuthService
import com.mich.nutrichef.ui.theme.NutriChefTheme
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    //Autht
//    val firebaseAuthService = remember { FirebaseAuthService() }
//    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isLoading by authViewModel.isLoading.collectAsState()
    //
//    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color(0xFF5CD6C8)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "🍽️", fontSize = 40.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )

            Text(
                text = "Comienza tu viaje saludable", fontSize = 14.sp, color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Nombre completo",
                fontSize = 14.sp,
                color = Color(0xFF2D3748),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("José García", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5CD6C8), unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Correo electrónico",
                fontSize = 14.sp,
                color = Color(0xFF2D3748),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("tu@email.com", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5CD6C8), unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contraseña",
                fontSize = 14.sp,
                color = Color(0xFF2D3748),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5CD6C8), unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Confirmar contraseña",
                fontSize = 14.sp,
                color = Color(0xFF2D3748),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("••••••••", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5CD6C8), unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    when {
                        fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT)
                                .show()
                        }

                        password != confirmPassword -> {
                            Toast.makeText(
                                context,
                                "Las contraseñas no coinciden",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        password.length < 6 -> {
                            Toast.makeText(
                                context,
                                "La contraseña debe tener al menos 6 caracteres",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            authViewModel.register(fullName, email, password) { result ->
                                if (result.isSuccess) {
                                    Toast.makeText(
                                        context,
                                        "Usuario registrado con éxito",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onRegisterSuccess()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error: ${result.exceptionOrNull()?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5CD6C8)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Registrarse",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Ya tienes cuenta? ", color = Color.Gray, fontSize = 14.sp
            )
            TextButton(
                onClick = { onNavigateToLogin() },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.offset(x = (-8).dp)
            ) {
                Text(
                    text = "  Inicia sesión",
                    color = Color(0xFF5CD6C8),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    NutriChefTheme {
        val fakeViewModel: AuthViewModel = viewModel()
        RegisterScreen(
            authViewModel = fakeViewModel,
            onNavigateToLogin = {},
            onRegisterSuccess = {}
        )
    }
}