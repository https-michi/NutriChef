package com.mich.nutrichef.presentation.screen.tips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mich.nutrichef.data.remote.firebase.FirebaseTipsService
import com.mich.nutrichef.data.remote.firebase.TipsViewModel
import com.mich.nutrichef.data.remote.firebase.tips.TipNutricional


@Composable
fun TipsNutricionalesScreen(
    viewModel: TipsViewModel = viewModel()
) {
    val tips by viewModel.tips.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarTips()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
//        // Botón temporal para poblar datos (eliminar después de usar)
//        Button(
//            onClick = { viewModel.poblarDatosIniciales() },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp)
//        ) {
//            Text("Poblar Tips Iniciales")
//        }

        Text(
            text = "Tips Nutricionales",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Consejos para una vida saludable",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF9E9E9E),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (tips.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay tips disponibles",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF9E9E9E)
                )
            }
        } else {
            tips.forEach { tip ->
                TipCard(tip = tip)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TipCard(tip: TipNutricional) {
    val icon = when (tip.iconoNombre) {
        "water" -> Icons.Filled.Face
        "restaurant" -> Icons.Default.ShoppingCart
        "calendar" -> Icons.Default.DateRange
        "palette" -> Icons.Default.AccountBox
        else -> Icons.Default.Info
    }

    val colorCategoria = when (tip.categoria) {
        "Hidratación" -> Color(0xFFFFCC80)
        "Nutrición" -> Color(0xFFFFCC80)
        "Organización" -> Color(0xFFFFCC80)
        else -> Color(0xFFFFCC80)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE0F2F1), shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF00897B),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tip.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121),
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = colorCategoria
                    ) {
                        Text(
                            text = tip.categoria,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF424242),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = tip.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575),
                    lineHeight = 20.sp
                )
            }
        }
    }
}


//// Función auxiliar para poblar datos iniciales (ejecutar una sola vez)
//suspend fun poblarTipsIniciales() {
//    val service = FirebaseTipsService()
//    val tipsIniciales = listOf(
//        TipNutricional(
//            titulo = "Hidratación constante",
//            categoria = "Hidratación",
//            descripcion = "Bebe al menos 8 vasos de agua al día para mantener tu cuerpo hidratado y favorecer la digestión.",
//            iconoNombre = "water",
//            orden = 1
//        ),
//        TipNutricional(
//            titulo = "Come más proteína",
//            categoria = "Nutrición",
//            descripcion = "Incluye proteína en cada comida para mantener la masa muscular y sentirte satisfecho por más tiempo.",
//            iconoNombre = "restaurant",
//            orden = 2
//        ),
//        TipNutricional(
//            titulo = "Planifica tus comidas",
//            categoria = "Organización",
//            descripcion = "Preparar tus comidas con anticipación te ayuda a mantener una alimentación saludable y ahorrar tiempo.",
//            iconoNombre = "calendar",
//            orden = 3
//        ),
//        TipNutricional(
//            titulo = "Variedad de colores",
//            categoria = "Nutrición",
//            descripcion = "Come frutas y verduras de diferentes colores para obtener una amplia gama de nutrientes.",
//            iconoNombre = "palette",
//            orden = 4
//        )
//    )

//    tipsIniciales.forEach { tip ->
//        service.agregarTip(tip)
//    }
