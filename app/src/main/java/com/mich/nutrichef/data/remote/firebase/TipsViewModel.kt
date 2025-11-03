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

}