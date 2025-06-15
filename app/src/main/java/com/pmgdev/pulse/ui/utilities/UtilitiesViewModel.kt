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

/**
 *
 * ViewModel de la pestaña Utilities
 *
 * ViewModel para el cálculo de la dieta con Gemini.
 *
 * Actualización 2.0.0
 * Añadido Gemini 13.0.1
 *
 */

@HiltViewModel
class UtilitiesViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(UtilitiesState())
        private set

    //La APIKey la he quitado de aquí por seguridad y se queda guardada en local.properties
    //Objeto cliente para conectar con el modelo de IA utilizando la APIKEY
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY

    )
    //CONTROL DE CAMPOS
    fun onAlturaChange(altura: String) {
        uiState = uiState.copy(altura = altura)
    }

    fun onPesoChange(peso: String) {
        uiState = uiState.copy(peso = peso)
    }

    fun onObjetivoChange(objetivo: String) {
        uiState = uiState.copy(objetivo = objetivo)
    }

    /**
     *
     * Metodo getPersonalizedDietAdvice
     *
     * Utilizando el cliente para contactar con el modelo, hacemos la consulta a Gemini.
     * Comprobamos si lo que nos devuelve no es error y se le muestra al usuario.
     */
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
                    errorMessage = "Error al obtener el consejo de dieta."
                )
                e.printStackTrace()
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }
}

