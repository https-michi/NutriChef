package com.mich.nutrichef

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.mich.nutrichef.data.UserBodyData

class BodyDataActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isBodyDataCompleted()) {
            // Si ya tiene datos, ir directo a la pantalla principal
            // navigateToHome()
            // finish()
            // return
        }

        setContent {
            MaterialTheme {
                Surface {
                    BodyDataScreen(
                        onContinue = { weight, height ->
                            handleBodyDataSubmit(weight, height)
                        }
                    )
                }
            }
        }
    }

    private fun handleBodyDataSubmit(weight: Float, height: Float) {

        val bodyData = UserBodyData(weight, height)

        // Calcular IMC
        val bmi = bodyData.calculateBMI()
        val category = bodyData.getBMICategory()
        val message = bodyData.getBMIMessage()

        // Guardar en SharedPreferences
        saveBodyData(weight, height, bmi)

        // Mostrar resultado al usuario
        Toast.makeText(
            this,
            "IMC: %.1f - %s".format(bmi, category),
            Toast.LENGTH_LONG
        ).show()

        // Colocal la siguiente pantalla -home
        // navigateToHome()

        finish()
    }

    private fun saveBodyData(weight: Float, height: Float, bmi: Float) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putFloat(KEY_WEIGHT, weight)
            putFloat(KEY_HEIGHT, height)
            putFloat(KEY_BMI, bmi)
            putBoolean(KEY_BODY_DATA_COMPLETED, true)
            putLong(KEY_LAST_UPDATE, System.currentTimeMillis())
            apply()
        }
    }

    private fun isBodyDataCompleted(): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BODY_DATA_COMPLETED, false)
    }

    companion object {
        private const val PREFS_NAME = "NutriPlatoPrefs"
        private const val KEY_WEIGHT = "user_weight"
        private const val KEY_HEIGHT = "user_height"
        private const val KEY_BMI = "user_bmi"
        private const val KEY_BODY_DATA_COMPLETED = "body_data_completed"
        private const val KEY_LAST_UPDATE = "body_data_last_update"

        //fun para obtener los datos dentro de la apop
        fun getSavedBodyData(context: Context): UserBodyData? {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

            if (!prefs.getBoolean(KEY_BODY_DATA_COMPLETED, false)) {
                return null
            }

            val weight = prefs.getFloat(KEY_WEIGHT, 0f)
            val height = prefs.getFloat(KEY_HEIGHT, 0f)

            return if (weight > 0 && height > 0) {
                UserBodyData(weight, height)
            } else {
                null
            }
        }
        //fun lanzar el el activity desde el login

        fun start(context: Context) {
            val intent = Intent(context, BodyDataActivity::class.java)
            context.startActivity(intent)
        }
    }
}