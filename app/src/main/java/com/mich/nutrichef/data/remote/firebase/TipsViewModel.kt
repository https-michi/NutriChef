package com.mich.nutrichef.data.remote.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mich.nutrichef.data.remote.firebase.tips.TipNutricional
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TipsViewModel : ViewModel() {
    private val service = FirebaseTipsService()

    private val _tips = MutableStateFlow<List<TipNutricional>>(emptyList())
    val tips: StateFlow<List<TipNutricional>> = _tips.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun cargarTips() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resultado = service.obtenerTips()
                _tips.value = resultado
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
//    fun poblarDatosIniciales() {
//        viewModelScope.launch {
//            poblarTipsIniciales()
////            cargarTips()
//        }
//    }
    // Función auxiliar para poblar datos iniciales (ejecutar una sola vez)
    suspend fun poblarTipsIniciales() {
        val service = FirebaseTipsService()
        val tipsIniciales = listOf(
            TipNutricional(
                titulo = "Hidratación constante",
                categoria = "Hidratación",
                descripcion = "Bebe al menos 8 vasos de agua al día para mantener tu cuerpo hidratado y favorecer la digestión.",
                iconoNombre = "water",
                orden = 1
            ),
            TipNutricional(
                titulo = "Come más proteína",
                categoria = "Nutrición",
                descripcion = "Incluye proteína en cada comida para mantener la masa muscular y sentirte satisfecho por más tiempo.",
                iconoNombre = "restaurant",
                orden = 2
            ),
            TipNutricional(
                titulo = "Planifica tus comidas",
                categoria = "Organización",
                descripcion = "Preparar tus comidas con anticipación te ayuda a mantener una alimentación saludable y ahorrar tiempo.",
                iconoNombre = "calendar",
                orden = 3
            ),
            TipNutricional(
                titulo = "Variedad de colores",
                categoria = "Nutrición",
                descripcion = "Come frutas y verduras de diferentes colores para obtener una amplia gama de nutrientes.",
                iconoNombre = "palette",
                orden = 4
            )
        )

        tipsIniciales.forEach { tip ->
            service.agregarTip(tip)
        }
    }

}