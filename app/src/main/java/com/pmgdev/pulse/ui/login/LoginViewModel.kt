package com.pmgdev.pulse.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * Login View Model
 *
 * Que funciona correctamente con casi todos los controles de errores.
 *
 */

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {
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
            return
        }
        if(hasEmptyFields()){
            return
        }
        auth.signInWithEmailAndPassword(state.email.trim(),state.password).addOnCompleteListener{ test ->
            if(test.isSuccessful){
                Log.d("LOGIN","SIUUUUUUUU")
                goToHome()
            }
            else{
                Log.d("ERROR","NO LOGIN")
            }
        }
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
}