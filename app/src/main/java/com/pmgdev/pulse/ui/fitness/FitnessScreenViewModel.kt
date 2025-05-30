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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FitnessScreenViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(FitnessScreenState())
        private set

    val fitnessOptions: FitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
        .build()


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
     * requestSteps
     *
     * Recoge los pasos dados accediendo al objeto GoogleFitManager.
     *
     *
     */
    fun requestSteps(context: Context) {
        GoogleFitManager.getStepsToday(
            context = context,
            onStepsRead = {
                Log.e("GoogleFit", "OK")
                uiState = uiState.copy(

                    steps = it,
                    success = true,
                    isNotRegister = false,
                )
            },
            onError = {
                Log.e("GoogleFit", "Error al obtener datos", it)
                uiState = uiState.copy(
                    error = it.message,
                    isError = true
                )
            }
        )
    }
    /**
     *
     * requestCalories
     *
     * Recoge las calorias quemadas accediendo al objeto GoogleFitManager.
     *
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
                Log.e("GoogleFit", "Error al obtener datos", it)
                uiState = uiState.copy(
                    error = it.message,
                    isError = true
                )
            }
        )
    }

    /**
     *
     *
     * Este metodo inicia todo el proceso.
     * Inicia el flujo de los permisos para chequear si se ha dado todos los permisos necesarios.
     *
     * Solicita los pasos y las kcal si todo es correcto, si no inicia de nuevo el flujo de Google SignIn
     *
     */
    fun launchGoogleFitFlow(context: Context, activity: Activity) {
        val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)
        if (GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            requestSteps(context)
            requestCalories(context)
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
     * Para la interfaz. Si no esta logueado se mostrará una ventana y si si lo esta se mostrara otra
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

