package com.mich.nutrichef.data

data class UserBodyData(
    val weight: Float, // en kg
    val height: Float  // en cm
) {
    /**
     Fórmula
     */
    fun calculateBMI(): Float {
        val heightInMeters = height / 100f
        return weight / (heightInMeters * heightInMeters)
    }

    fun getBMICategory(): String {
        val bmi = calculateBMI()
        return when {
            bmi < 18.5 -> "Bajo peso"
            bmi < 25.0 -> "Peso normal"
            bmi < 30.0 -> "Sobrepeso"
            bmi < 35.0 -> "Obesidad grado I"
            bmi < 40.0 -> "Obesidad grado II"
            else -> "Obesidad grado III"
        }
    }

    fun getBMIColor(): Long {
        val bmi = calculateBMI()
        return when {
            bmi < 18.5 -> 0xFFF59E0B // Amarillo - Bajo peso
            bmi < 25.0 -> 0xFF10B981 // Verde - Normal
            bmi < 30.0 -> 0xFFF97316 // Naranja - Sobrepeso
            else -> 0xFFEF4444       // Rojo - Obesidad
        }
    }

    fun getBMIMessage(): String {
        val bmi = calculateBMI()
        return when {
            bmi < 18.5 -> "Considera consultar con un nutricionista para alcanzar un peso saludable."
            bmi < 25.0 -> "¡Excelente! Mantén tu peso con una alimentación balanceada."
            bmi < 30.0 -> "Te recomendamos platos balanceados para mejorar tu salud."
            else -> "Consulta con un profesional de la salud para un plan personalizado."
        }
    }
}
