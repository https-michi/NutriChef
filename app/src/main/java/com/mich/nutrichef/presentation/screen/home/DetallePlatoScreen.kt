package com.mich.nutrichef.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mich.nutrichef.data.remote.firebase.plato.Plato

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePlatoScreen(
    plato: Plato,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean = false
) {
    var showIngredientes by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF2D3748)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color(0xFFEF4444) else Color(0xFF2D3748)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: Compartir */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF5CD6C8)
                        )
                    ) {
                        Text("Guardar")
                    }

                    Button(
                        onClick = { /* TODO: Empezar a cocinar */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5CD6C8)
                        )
                    ) {
                        Text("Ver proceso")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Imagen principal
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(plato.imagen),
                        contentDescription = plato.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Información básica
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(20.dp)
                ) {
                    // Badge de categoría
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFDCFCE7),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = plato.categoria,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF065F46)
                        )
                    }

                    Text(
                        text = plato.nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = plato.descripcion,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Iconos de info rápida
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        InfoChip(
                            icon = "⏱️",
                            text = "${plato.tiempoPreparacion} min"
                        )
                        InfoChip(
                            icon = "👨‍🍳",
                            text = plato.nivelDificultad
                        )
                        InfoChip(
                            icon = "🍽️",
                            text = "${plato.porcionesBase} porción"
                        )
                    }
                }
            }

            // Información nutricional
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Información nutricional",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            NutricionCard(
                                valor = "${plato.valorNutricional.calorias.toInt()}",
                                unidad = "Calorías",
                                color = Color(0xFF10B981)
                            )
                            NutricionCard(
                                valor = "${plato.valorNutricional.proteinas.toInt()}g",
                                unidad = "Proteínas",
                                color = Color(0xFFEF4444)
                            )
                            NutricionCard(
                                valor = "${plato.valorNutricional.carbohidratos.toInt()}g",
                                unidad = "Carbohidratos",
                                color = Color(0xFFF59E0B)
                            )
                            NutricionCard(
                                valor = "${plato.valorNutricional.grasas.toInt()}g",
                                unidad = "Grasas",
                                color = Color(0xFF3B82F6)
                            )
                        }
                    }
                }
            }

            // Tabs: Ingredientes / Preparación
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    TextButton(
                        onClick = { showIngredientes = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (showIngredientes) Color(0xFF5CD6C8) else Color.Gray
                        )
                    ) {
                        Text(
                            text = "Ingredientes",
                            fontWeight = if (showIngredientes) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    TextButton(
                        onClick = { showIngredientes = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (!showIngredientes) Color(0xFF5CD6C8) else Color.Gray
                        )
                    ) {
                        Text(
                            text = "Preparación",
                            fontWeight = if (!showIngredientes) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFE5E7EB)
                )
            }

            // Contenido de tabs
            if (showIngredientes) {
                items(plato.ingredientes) { ingrediente ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF5CD6C8))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${ingrediente.cantidadBase.toInt()}${ingrediente.unidad} de ${ingrediente.nombre}",
                            fontSize = 14.sp,
                            color = Color(0xFF2D3748)
                        )
                    }
                }
            } else {
                items(plato.pasos.withIndex().toList()) { (index, paso) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF5CD6C8),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = paso,
                            fontSize = 14.sp,
                            color = Color(0xFF2D3748),
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Espacio al final
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun InfoChip(icon: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = icon, fontSize = 16.sp)
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color(0xFF6B7280)
        )
    }
}

@Composable
fun NutricionCard(valor: String, unidad: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = valor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = unidad,
            fontSize = 11.sp,
            color = Color(0xFF9CA3AF)
        )
    }
}