package com.pmgdev.pulse.ui.fitness

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.pmgdev.pulse.network.GoogleFitManager
import com.pmgdev.pulse.network.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class FitnessScreenViewModel @Inject constructor(
    private val session: Session
) : ViewModel() {

    var uiState by mutableStateOf(FitnessScreenState())
        private set
    private var isInitialized = false


    private var currentRealtimeStepsToday = 0

    val fitnessOptions: FitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
        .build()

    /**
     * Inicializa todos los datos: pasos de ayer, pasos de hoy (inicialmente históricos y luego en tiempo real), y calorías.
     */
    fun initializeFitnessData(context: Context) {
        if (isInitialized) return
        isInitialized = true
        GoogleFitManager.getStepsForDay(
            context = context,
            dayOffset = -1L,
            onStepsRead = { yesterdaySteps ->
                uiState = uiState.copy(stepsYesterday = yesterdaySteps)
                Log.d("GoogleFit", "Pasos de ayer: $yesterdaySteps")
            },
            onError = {
                Log.e("GoogleFit", "Error al obtener pasos de ayer", it)
                uiState = uiState.copy(
                    error = it.message,
                    isError = true
                )
            }
        )

        GoogleFitManager.getStepsForDay(
            context = context,
            dayOffset = 0L,
            onStepsRead = { historicalStepsToday ->

                currentRealtimeStepsToday = historicalStepsToday
                uiState = uiState.copy(
                    steps = currentRealtimeStepsToday,
                    success = true,
                    isNotRegister = false,
                )
                Log.d("GoogleFit", "Pasos históricos de hoy: $historicalStepsToday")
                startRealtimeSteps(context)
            },
            onError = {
                Log.e("GoogleFit", "Error al obtener pasos históricos de hoy o al iniciar real-time", it)
                uiState = uiState.copy(
                    error = it.message,
                    isError = true
                )
                startRealtimeSteps(context)
            }
        )

        requestCalories(context)
    }

    /**
     * Comienza a escuchar las actualizaciones de pasos en tiempo real.
     */
    private fun startRealtimeSteps(context: Context) {
        GoogleFitManager.registerStepSensor(
            context = context,
            onStepUpdate = { newStepsDelta ->
                currentRealtimeStepsToday += newStepsDelta
                uiState = uiState.copy(steps = currentRealtimeStepsToday)
                Log.d("GoogleFit", "Actualización de pasos en tiempo real (delta): $newStepsDelta, Total hoy: $currentRealtimeStepsToday")
            },
            onError = {
                Log.e("GoogleFit", "Error al registrar el sensor de pasos en tiempo real", it)
                uiState = uiState.copy(
                    error = it.message,
                    isError = true
                )
            }
        )
    }

    /**
     * Detiene la escucha de actualizaciones de pasos en tiempo real.
     * Se llama cuando el composable abandona la composición.
     */
    fun stopRealtimeSteps(context: Context) {
        GoogleFitManager.unregisterStepSensor(context)
        Log.d("GoogleFit", "Sensor de pasos en tiempo real desregistrado.")
    }

    /**
     *
     * checkGoogleFitsPermissions
     *
     * Chequea si el usuario ha realizado el registro de la cuenta de google.
     *
     */
    fun checkGoogleFitPermissions(context: Context): Boolean {
        val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)
        return GoogleSignIn.hasPermissions(account, fitnessOptions)
    }

    /**
     *
     * requestCalories
     *
     * Recoge las calorias quemadas accediendo al objeto GoogleFitManager.
     * Esto seguirá siendo histórico, ya que el gasto de calorías en tiempo real es más complejo.
     *
     */
    fun requestCalories(context: Context) {
        GoogleFitManager.getCaloriesToday(
            context = context,
            onCaloriesRead = {
                Log.e("GoogleFit", "OK")
                uiState = uiState.copy(calories = it)
            },
            onError = {
                Log.e("GoogleFit", "Error al obtener datos de calorías", it)
                uiState = uiState.copy(
                    error = it.message,
                    isError = true
                )
            }
        )
    }

    /**
     *
     * Este método inicia todo el proceso.
     * Inicia el flujo de los permisos para chequear si se ha dado todos los permisos necesarios.
     *
     * Solicita los pasos y las kcal si todo es correcto, si no inicia de nuevo el flujo de Google SignIn
     *
     */
    fun launchGoogleFitFlow(context: Context, activity: Activity) {
        val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)
        if (GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            // Después de permisos exitosos, inicializa todos los datos de fitness
            initializeFitnessData(context)
        } else {
            GoogleSignIn.requestPermissions(
                activity,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                account,
                fitnessOptions
            )
        }
    }

    /**
     *
     * setNotRegistered
     *
     * Para la interfaz. Si no está logueado se mostrará una ventana y si sí lo está se mostrará otra
     *
     */
    fun setNotRegistered() {
        uiState = uiState.copy(isNotRegister = true,)
    }

    //Constante para identificar la solicitud de los permisos en el ciclo de vida.
    companion object {
        const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1001
    }
}

