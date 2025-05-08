package com.pmgdev.pulse.ui.utilities

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.pmgdev.pulse.BuildConfig


@HiltViewModel
class UtilitiesViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(UtilitiesState())
        private set

    init {
        Log.d("APIKEY22222222","🔑 API_KEY usada: ${BuildConfig.API_KEY}")
    }

    //La APIKey la he quitado de aquí por seguridad y se queda guardada en local.properties
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY

    )

    fun onAlturaChange(altura: String) {
        uiState = uiState.copy(altura = altura)
    }

    fun onPesoChange(peso: String) {
        uiState = uiState.copy(peso = peso)
    }

    fun onObjetivoChange(objetivo: String) {
        uiState = uiState.copy(objetivo = objetivo)
    }

    fun getPersonalizedDietAdvice() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null, dietAdvice = null)

            val altura = uiState.altura.toDoubleOrNull()
            val peso = uiState.peso.toDoubleOrNull()

            if (altura == null || peso == null) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Por favor, introduce valores válidos para altura y peso."
                )
                return@launch
            }

            if (uiState.objetivo.isBlank()) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Por favor, describe brevemente tu objetivo o pregunta."
                )
                return@launch
            }
            //Consulta al modelo de IA generativo
            val prompt = """
                Eres un asistente virtual especializado en nutrición y dietas saludables.
                Un usuario te pide consejo sobre su dieta.
                Sus datos son:
                - Altura: ${uiState.altura} cm
                - Peso: ${uiState.peso} kg
                - Objetivo/Pregunta: "${uiState.objetivo}"

                Por favor, proporciona un consejo de dieta claro, conciso, práctico y accionable para ayudar al usuario a mejorar su dieta según sus datos y objetivo.
                El consejo debe ser fácil de entender y aplicar en la vida diaria.
                No incluyas ningún tipo de descargo de responsabilidad médica o nutricional en tu respuesta, la aplicación se encargará de mostrarlo por separado.
                Limita tu respuesta a un máximo de 3-4 frases cortas.
            """.trimIndent()

            try {
                val response = generativeModel.generateContent(prompt)
                uiState = uiState.copy(dietAdvice = response.text)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    errorMessage = "Error al generar el consejo: ${e.localizedMessage}"
                )
                e.printStackTrace()
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }
}

