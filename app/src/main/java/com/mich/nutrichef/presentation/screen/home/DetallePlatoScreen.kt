package com.mich.nutrichef.presentation.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
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
    onVerProcesoClick: () -> Unit,
    isFavorite: Boolean = false
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

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

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-30).dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = plato.nombre,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2D3748)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFDCFCE7),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    Text(
                                        text = plato.categoria,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF065F46)
                                    )
                                }

                                Text(
                                    text = plato.descripcion,
                                    fontSize = 14.sp,
                                    color = Color(0xFF6B7280),
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    InfoChip("⏱️", "${plato.tiempoPreparacion} min")
                                    InfoChip("👨‍🍳", plato.nivelDificultad)
                                    InfoChip("🍽️", "${plato.porcionesBase} porción")
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
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
                                    "${plato.valorNutricional.calorias.toInt()}",
                                    "Calorías",
                                    Color(0xFF10B981)
                                )
                                NutricionCard(
                                    "${plato.valorNutricional.proteinas.toInt()}g",
                                    "Proteínas",
                                    Color(0xFFEF4444)
                                )
                                NutricionCard(
                                    "${plato.valorNutricional.carbohidratos.toInt()}g",
                                    "Carbohidratos",
                                    Color(0xFFF59E0B)
                                )
                                NutricionCard(
                                    "${plato.valorNutricional.grasas.toInt()}g",
                                    "Grasas",
                                    Color(0xFF3B82F6)
                                )
                            }
                        }
                    }
                }

                item {
                    var porciones by remember { mutableStateOf(plato.porcionesBase) }
                    val costoBase = plato.presupuesto.costoTotal ?: 0.0
                    val precioPorPorcion =
                        if (plato.porcionesBase > 0) costoBase / plato.porcionesBase else 0.0
                    val precioTotal = precioPorPorcion * porciones

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Control de porciones",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF374151)
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { if (porciones > 1) porciones-- },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFF3F4F6))
                                    ) {
                                        Icon(
                                            Icons.Default.Remove,
                                            contentDescription = "Disminuir",
                                            tint = Color(0xFF4B5563)
                                        )
                                    }

                                    Text(
                                        text = porciones.toString(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827),
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    IconButton(
                                        onClick = { porciones++ },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFDCFCE7))
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "Aumentar",
                                            tint = Color(0xFF047857)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Precio por porción: S/. ${"%.2f".format(precioPorPorcion)}",
                                fontSize = 13.sp,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = "Costo total estimado: S/. ${"%.2f".format(precioTotal)}",
                                fontSize = 13.sp,
                                color = Color(0xFF047857),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Ingredientes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color(0xFFE5E7EB)
                    )
                }

                items(plato.ingredientes) { ingrediente ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF9FAFB))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ingrediente.nombre,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFF2D3748)
                        )
                        Text(
                            text = "${ingrediente.cantidadBase.toInt()} ${ingrediente.unidad}",
                            fontSize = 13.sp,
                            color = Color(0xFF4A5568)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* TODO: Guardar */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            border = BorderStroke(1.5.dp, Color(0xFF5CD6C8)),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF047857)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Guardar",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Guardar", fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = onVerProcesoClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5CD6C8),
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Ver proceso",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ver proceso", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween
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

                IconButton(onClick = onFavoriteClick) {
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.95f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color(0xFFEF4444) else Color(0xFF2D3748)
                        )
                    }
                }
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
        Text(icon, fontSize = 16.sp)
        Text(text, fontSize = 13.sp, color = Color(0xFF6B7280))
    }
}

@Composable
fun NutricionCard(valor: String, unidad: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
        Text(unidad, fontSize = 11.sp, color = Color(0xFF9CA3AF))
    }
}
