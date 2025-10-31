package com.mich.nutrichef

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mich.nutrichef.ui.theme.NutriChefTheme
import androidx.core.content.edit

class MainActivity : ComponentActivity() {

    companion object {
        private const val PREFS_NAME = "nutrichef_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NutriChefTheme {
                // ✅ NUEVO: Variable de estado que controla qué pantalla mostrar
                // POR QUÉ: Compose usa estados para actualizar la UI automáticamente
                var showOnboarding by remember {
                    mutableStateOf(shouldShowOnboarding())
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    if (showOnboarding) {
//                        OnboardingScreen(
//                            onFinish = {
//                                // Cuando termina el onboarding:
//                                saveOnboardingCompleted()  // Guarda que ya se completó
//                                showOnboarding = false      // Cambia a la pantalla principal
//                            }
//                        )
//                    } else {
//                        MainContent()
//                    }
                    BodyDataScreen(
                        onContinue = { weight, height ->
                            saveBodyData(weight, height)
                        }
                    )
                }
            }
        }
    }

    //Lee SharedPreferences para saber si ya vio el onboarding
    private fun shouldShowOnboarding(): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return !prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    private fun saveOnboardingCompleted() {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putBoolean(KEY_ONBOARDING_COMPLETED, true)
            }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveBodyData(weight: Float, height: Float) {
        val bmi = weight / ((height / 100) * (height / 100))

        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putFloat("user_weight", weight)
            .putFloat("user_height", height)

    }

    @Composable
    fun MainContent() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡Bienvenido a NutriChef! 🍽️",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    //@Composable
    //fun Greeting(name: String, modifier: Modifier = Modifier) {
    //    Text(
    //        text = "Hello $name!",
    //        modifier = modifier
    //    )
    //}
    //
    // Preview actualizado (para ver en el editor)
    @Preview(showBackground = true)
    @Composable
    fun MainContentPreview() {
        NutriChefTheme {
            MainContent()
        }
    }
}