package com.mich.nutrichef.presentation.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mich.nutrichef.data.remote.firebase.plato.Plato

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasosPlatoScreen(
    plato: Plato,
    onBackClick: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    val pasos = plato.pasos ?: listOf()

    Scaffold(
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(60.dp))

                    Text(
                        text = "Preparación",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )

                    Text(
                        text = plato.nombre,
                        fontSize = 18.sp,
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Barra de progreso
                    Column(modifier = Modifier.padding(bottom = 20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Progreso: ${currentStep + 1}/${pasos.size}",
                                fontSize = 13.sp,
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${((currentStep + 1) * 100 / pasos.size)}%",
                                fontSize = 13.sp,
                                color = Color(0xFF047857),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE5E7EB))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth((currentStep + 1).toFloat() / pasos.size)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF5CD6C8))
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Paso ${currentStep + 1}",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = pasos.getOrNull(currentStep) ?: "Sin pasos disponibles",
                                fontSize = 18.sp,
                                lineHeight = 26.sp,
                                color = Color(0xFF111827),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(
                                    onClick = { if (currentStep > 0) currentStep-- },
                                    enabled = currentStep > 0,
                                    modifier = Modifier.weight(1f),
                                    border = BorderStroke(1.5.dp, Color(0xFF5CD6C8)),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF047857),
                                        disabledContentColor = Color(0xFF9CA3AF)
                                    )
                                ) {
                                    Text("Volver", fontWeight = FontWeight.SemiBold)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Button(
                                    onClick = {
                                        if (currentStep < pasos.lastIndex) currentStep++
                                    },
                                    enabled = currentStep < pasos.lastIndex,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF5CD6C8),
                                        contentColor = Color.White,
                                        disabledContainerColor = Color(0xFFE5E7EB),
                                        disabledContentColor = Color(0xFF9CA3AF)
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 4.dp,
                                        pressedElevation = 6.dp
                                    )
                                ) {
                                    Text("Siguiente", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "Progreso general",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                itemsIndexed(pasos) { index, paso ->
                    val isActive = index == currentStep
                    val isCompleted = index < currentStep
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isActive -> Color(0xFFDCFCE7)
                                isCompleted -> Color(0xFFE0F2FE)
                                else -> Color(0xFFF9FAFB)
                            }
                        ),
                        elevation = CardDefaults.cardElevation(if (isActive) 3.dp else 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isActive -> Color(0xFF047857)
                                            isCompleted -> Color(0xFF0284C7)
                                            else -> Color(0xFFD1D5DB)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = paso,
                                fontSize = 15.sp,
                                color = when {
                                    isActive -> Color(0xFF065F46)
                                    isCompleted -> Color(0xFF075985)
                                    else -> Color(0xFF374151)
                                },
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBackClick) {
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.95f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF01050E)
                        )
                    }
                }
            }
        }
    }
}