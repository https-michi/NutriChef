package com.mich.nutrichef.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.mich.nutrichef.data.remote.firebase.AuthState
import com.mich.nutrichef.data.remote.firebase.AuthViewModel
import com.mich.nutrichef.data.remote.firebase.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onLogout: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val userProfile by userViewModel.userProfile.collectAsState()

    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var imc by remember { mutableStateOf<Float?>(null) }
    var isEditingPeso by remember { mutableStateOf(false) }
    var isEditingAltura by remember { mutableStateOf(false) }

    LaunchedEffect(userProfile) {
        peso = userProfile.peso?.toString() ?: "0"
        altura = userProfile.altura?.toString() ?: "0"

        if (userProfile.peso != null && userProfile.altura != null &&
            userProfile.peso!! > 0 && userProfile.altura!! > 0
        ) {
            calcularIMC(peso, altura) { resultado ->
                imc = resultado
            }
        } else {
            imc = null
        }
    }

    val mintGreen = Color(0xFF5CDAB8)
    val softGray = Color(0xFFF5F5F5)
    val darkGray = Color(0xFF666666)

    Scaffold(
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(softGray)
                .padding(padding)
        ) {
            when (authState) {
                is AuthState.Unauthenticated -> {
                    LaunchedEffect(Unit) {
                        onLogout()
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = mintGreen)
                    }
                }

                is AuthState.Authenticated -> {
                    val user = (authState as AuthState.Authenticated).user

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = mintGreen
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val nombre = userProfile.nombre ?: "Usuario"
                                    Text(
                                        text = nombre.firstOrNull()?.uppercase() ?: "U",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = userProfile.nombre ?: "Usuario",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Text(
                                    text = user.email ?: "",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Información Física",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkGray
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Peso",
                                            fontSize = 14.sp,
                                            color = darkGray.copy(alpha = 0.7f)
                                        )
                                        if (isEditingPeso) {
                                            OutlinedTextField(
                                                value = peso,
                                                onValueChange = { peso = it },
                                                suffix = { Text("kg") },
                                                modifier = Modifier.fillMaxWidth(),
                                                singleLine = true,
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Number
                                                ),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = mintGreen,
                                                    focusedLabelColor = mintGreen
                                                )
                                            )
                                        } else {
                                            val pesoValor = peso.toDoubleOrNull() ?: 0.0
                                            Text(
                                                text = if (pesoValor > 0) "$peso kg" else "Por completar",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = if (pesoValor > 0) darkGray else darkGray.copy(
                                                    alpha = 0.5f
                                                )
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = {
                                            if (isEditingPeso) {
                                                val pesoDouble = peso.toDoubleOrNull()
                                                if (pesoDouble != null && pesoDouble > 0) {
                                                    userViewModel.updatePeso(pesoDouble)
                                                    calcularIMC(peso, altura) { resultado ->
                                                        imc = resultado
                                                    }
                                                }
                                            }
                                            isEditingPeso = !isEditingPeso
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isEditingPeso) Icons.Filled.Check else Icons.Outlined.Edit,
                                            contentDescription = if (isEditingPeso) "Guardar" else "Editar",
                                            tint = mintGreen
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Altura",
                                            fontSize = 14.sp,
                                            color = darkGray.copy(alpha = 0.7f)
                                        )
                                        if (isEditingAltura) {
                                            OutlinedTextField(
                                                value = altura,
                                                onValueChange = { altura = it },
                                                suffix = { Text("cm") },
                                                modifier = Modifier.fillMaxWidth(),
                                                singleLine = true,
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Number
                                                ),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = mintGreen,
                                                    focusedLabelColor = mintGreen
                                                )
                                            )
                                        } else {
                                            val alturaValor = altura.toDoubleOrNull() ?: 0.0
                                            Text(
                                                text = if (alturaValor > 0) "$altura cm" else "Por completar",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = if (alturaValor > 0) darkGray else darkGray.copy(
                                                    alpha = 0.5f
                                                )
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = {
                                            if (isEditingAltura) {
                                                val alturaDouble = altura.toDoubleOrNull()
                                                if (alturaDouble != null && alturaDouble > 0) {
                                                    userViewModel.updateAltura(alturaDouble)
                                                    // Recalcular IMC
                                                    calcularIMC(peso, altura) { resultado ->
                                                        imc = resultado
                                                    }
                                                }
                                            }
                                            isEditingAltura = !isEditingAltura
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isEditingAltura) Icons.Filled.Check else Icons.Outlined.Edit,
                                            contentDescription = if (isEditingAltura) "Guardar" else "Editar",
                                            tint = mintGreen
                                        )
                                    }
                                }

                                if (imc != null) {
                                    Spacer(modifier = Modifier.height(16.dp))

                                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = getIMCColor(imc!!).copy(alpha = 0.1f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "Tu IMC",
                                                fontSize = 14.sp,
                                                color = darkGray.copy(alpha = 0.7f)
                                            )
                                            Text(
                                                text = String.format("%.1f", imc),
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = getIMCColor(imc!!)
                                            )
                                        }

                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = getIMCColor(imc!!)
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = getIMCCategory(imc!!),
                                                modifier = Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 6.dp
                                                ),
                                                color = Color.White,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = getIMCDescription(imc!!),
                                        fontSize = 12.sp,
                                        color = darkGray.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Gráfico de IMC",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = darkGray,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "Rangos de IMC",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkGray,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    IMCRangeBar(
                                        category = "Bajo peso",
                                        range = "< 18.5",
                                        color = Color(0xFF2196F3),
                                        isCurrentRange = imc != null && imc!! < 18.5
                                    )

                                    IMCRangeBar(
                                        category = "Normal",
                                        range = "18.5 - 24.9",
                                        color = Color(0xFF4CAF50),
                                        isCurrentRange = imc != null && imc!! >= 18.5 && imc!! < 25
                                    )

                                    IMCRangeBar(
                                        category = "Sobrepeso",
                                        range = "25.0 - 29.9",
                                        color = Color(0xFFFFA726),
                                        isCurrentRange = imc != null && imc!! >= 25 && imc!! < 30
                                    )

                                    IMCRangeBar(
                                        category = "Obesidad",
                                        range = "≥ 30.0",
                                        color = Color(0xFFE53935),
                                        isCurrentRange = imc != null && imc!! >= 30
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                authViewModel.logout()
                                onLogout()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFEBEE)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Logout,
                                contentDescription = null,
                                tint = Color(0xFFE53935)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Cerrar Sesión",
                                color = Color(0xFFE53935),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                AuthState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = mintGreen)
                    }
                }

                is AuthState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Error: ${(authState as AuthState.Error).message}",
                                color = Color.Red
                            )
                            Button(
                                onClick = onLogout,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = mintGreen
                                )
                            ) {
                                Text("Volver")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IMCRangeBar(
    category: String,
    range: String,
    color: Color,
    isCurrentRange: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isCurrentRange) color.copy(alpha = 0.15f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color = color, shape = CircleShape)
            )

            Column {
                Text(
                    text = category,
                    fontSize = 14.sp,
                    fontWeight = if (isCurrentRange) FontWeight.Bold else FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Text(
                    text = range,
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }

        if (isCurrentRange) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = color
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Tu rango",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun calcularIMC(peso: String, altura: String, onResult: (Float?) -> Unit) {
    val pesoFloat = peso.toFloatOrNull()
    val alturaFloat = altura.toFloatOrNull()

    if (pesoFloat != null && alturaFloat != null && alturaFloat > 0 && pesoFloat > 0) {
        val alturaMetros = alturaFloat / 100
        val imcCalculado = pesoFloat / (alturaMetros * alturaMetros)
        onResult(imcCalculado)
    } else {
        onResult(null)
    }
}

fun getIMCCategory(imc: Float): String {
    return when {
        imc < 18.5 -> "Bajo peso"
        imc < 25 -> "Normal"
        imc < 30 -> "Sobrepeso"
        else -> "Obesidad"
    }
}

fun getIMCColor(imc: Float): Color {
    return when {
        imc < 18.5 -> Color(0xFF2196F3)
        imc < 25 -> Color(0xFF4CAF50)
        imc < 30 -> Color(0xFFFFA726)
        else -> Color(0xFFE53935)
    }
}

fun getIMCDescription(imc: Float): String {
    return when {
        imc < 18.5 -> "Tu peso está por debajo del rango saludable. Considera consultar con un nutricionista."
        imc < 25 -> "¡Excelente! Tu peso está en el rango saludable."
        imc < 30 -> "Tu peso está ligeramente por encima del rango saludable."
        else -> "Te recomendamos consultar con un profesional de la salud."
    }
}