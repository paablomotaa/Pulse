package com.pmgdev.pulse.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.network.Session
import com.pmgdev.pulse.network.Session.Companion.email
import com.pmgdev.pulse.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Login View Model
 *
 * Que funciona correctamente con casi todos los controles de errores.
 *
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val session: Session
) : ViewModel() {
    var state by mutableStateOf(LoginScreenState())
        private set

    fun onEmailChange(email:String){
        if(email.contains(' ')){
            return
        }
        if(email.isEmpty()){
            state = state.copy(
                email = email,
                isEmailError = true,
                emailErrorText = "Esta vacío"
            )
        }
        if(!isValidEmail(email)){
            state = state.copy(
                email = email,
                isEmailError = true,
                emailErrorText = "No tiene el formato adecuado"
            )
        }
        else{
            state = state.copy(
                email = email,
                isEmailError = false,
                emailErrorText = ""
            )
        }
    }

    fun onPasswordChange(password:String){
        if(password.isEmpty()){
            state = state.copy(
                password = password,
                isPasswordError = true,
                passwordErrorText = "Esta vacío"
            )
        }
        else{
            state = state.copy(
                password = password,
                isPasswordError = false,
                passwordErrorText = ""
            )
        }
    }

    fun onLoginClick(goToHome: () -> Unit) {
        if (validateFields()){
            state = state.copy(
                toastMessage = "Hay campos incorrectos❌"
            )
            return
        }
        if(hasEmptyFields()){
            state = state.copy(toastMessage = "Hay campos vacíos❌")
            return
        }
        auth.signInWithEmailAndPassword(state.email.trim(),state.password.trim()).addOnCompleteListener{ test ->
            if(test.isSuccessful){
                state = state.copy(toastMessage = "Login correcto✅")
                viewModelScope.launch {
                    session.saveUserSession(
                        userEmail = state.email,
                        userPassword = state.password,
                        isUserLoggedIn = true
                    )
                }
                goToHome()
            }
            else{
                state = state.copy(toastMessage = "Hubo un error desconocido. Por favor pruebe más tarde.❌")
            }
        }
        Log.d("log", "User logged: " + auth.currentUser?.email.toString())
    }

    private fun hasEmptyFields():Boolean{
        var hasEmptyFields:Boolean = false

        if(state.email.isEmpty()){
            state = state.copy(
                isEmailError = true,
                emailErrorText = "Este campo no puede estar vacío"
            )
            hasEmptyFields = true
        }
        if(state.password.isEmpty()){
            state = state.copy(
                isPasswordError = true,
                passwordErrorText = "Este campo no puede estar vacío"
            )

            hasEmptyFields = true
        }

        return hasEmptyFields
    }

    private fun validateFields():Boolean{
        return state.isEmailError || state.isPasswordError
    }
    fun clearToastMessage(){
        state = state.copy(
            toastMessage = ""
        )
    }
}