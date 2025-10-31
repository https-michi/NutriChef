package com.mich.nutrichef

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyDataScreen(
    onContinue: (weight: Float, height: Float) -> Unit
) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val primaryGreen = Color(0xFF10B981)
    val lightGreen = Color(0xFF86EFAC)
    val backgroundColor = Color(0xFFF0FDF4)
    val cardBackground = Color(0xFFF7F7F7)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo con gradiente
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(lightGreen, primaryGreen)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "NutriPlato",
                    modifier = Modifier.size(50.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título
            Text(
                text = "¡Bienvenido a NutriPlato!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtítulo
            Text(
                text = "Para personalizar tu experiencia, ingresa tus datos corporales",
                fontSize = 16.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Campo de Peso
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Peso (kg)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = weight,
                    onValueChange = {
                        weight = it
                        showError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    placeholder = { Text("70", color = Color(0xFF9CA3AF)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedContainerColor = cardBackground,
                        unfocusedContainerColor = cardBackground,
                        errorBorderColor = Color(0xFFEF4444)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = showError && weight.isBlank()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de Altura
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Altura (cm)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = height,
                    onValueChange = {
                        height = it
                        showError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    placeholder = { Text("175", color = Color(0xFF9CA3AF)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGreen,
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedContainerColor = cardBackground,
                        unfocusedContainerColor = cardBackground,
                        errorBorderColor = Color(0xFFEF4444)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = showError && height.isBlank()
                )
            }

            if (showError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Por favor completa todos los campos",
                    color = Color(0xFFEF4444),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Card de información
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFDCFCE7)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💡",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Esta información nos ayudará a calcular tu IMC y recomendarte platos adecuados",
                        fontSize = 14.sp,
                        color = Color(0xFF065F46),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Continuar
            Button(
                onClick = {
                    if (weight.isNotBlank() && height.isNotBlank()) {
                        try {
                            val weightFloat = weight.replace(",", ".").toFloat()
                            val heightFloat = height.replace(",", ".").toFloat()

                            if (weightFloat > 0 && heightFloat > 0) {
                                onContinue(weightFloat, heightFloat)
                            } else {
                                showError = true
                            }
                        } catch (e: NumberFormatException) {
                            showError = true
                        }
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryGreen,
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFD1D5DB),
                    disabledContentColor = Color(0xFF9CA3AF)
                )
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BodyDataScreenPreview() {
    BodyDataScreen(
        onContinue = { _, _ -> }
    )
}
