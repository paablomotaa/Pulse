package com.pmgdev.pulse.ui.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.utils.isValidEmail

class RegisterViewModel : ViewModel() {
    var state by mutableStateOf(RegisterScreenState())
        private set
    val auth:FirebaseAuth = FirebaseAuth.getInstance()

    fun onNameChange(name:String){
        if(name.isEmpty()){
            state = state.copy(
                name = name,
                isNameError = true,
                nameErrorText = "Esta vacío"
            )
        }
        else{
            state = state.copy(
                name = name,
                isNameError = false,
                nameErrorText = ""
            )
        }
    }
    fun onDateChange(date:String){
        if(date.isEmpty()){
            state = state.copy(
                date = date,
                isDateError = true,
                dateErrorText = "Esta vacío"
            )
        }
        else{
            state = state.copy(
                date = date,
                isDateError = false,
                dateErrorText = ""
            )
        }
    }


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
        if(password.length < 6){
            state = state.copy(
                password = password,
                isPasswordError = true,
                passwordErrorText = "La contraseña no puede ser menor de 6 carácteres"
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
    fun onRegisterClick(){
        if (validateFields()){
            return
        }
        if(hasEmptyFields()){
            return
        }
        else{
            auth.createUserWithEmailAndPassword(state.email,state.password).addOnCompleteListener{ test ->
                if(test.isSuccessful){
                    Log.d("SUCCESS","SUCESS")
                }
                else{
                    Log.d("FAILED","FAILED")
                }
            }
        }
    }

    private fun hasEmptyFields():Boolean{
        var hasEmptyFields:Boolean = false

        if(state.name.isEmpty()){
            state = state.copy(
                isNameError = true,
                nameErrorText = "Este campo no puede estar vacío"
            )
            hasEmptyFields = true
        }
        if(state.date.isEmpty()){
            state = state.copy(
                isDateError = true,
                dateErrorText = "Este campo no puede estar vacío"
            )
            hasEmptyFields = true
        }
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
        return state.isNameError || state.isEmailError || state.isDateError || state.isPasswordError
    }

}