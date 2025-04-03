package com.pmgdev.pulse.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.utils.isValidEmail

class LoginViewModel : ViewModel() {
    var state by mutableStateOf(LoginScreenState())
        private set
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onEmailChange(email:String){
        if(email.contains(' ')){
            return
        }
        if(email.isEmpty() || !isValidEmail(email)){
            state = state.copy(
                email = email,
                generalError = true
            )
        }
        else{
            state = state.copy(
                email = email,
                generalError = false
            )
        }
    }

    fun onPasswordChange(password:String){
        if(password.isEmpty()){
            state = state.copy(
                password = password,
                generalError = true
            )
        }
        else{
            state = state.copy(
                password = password,
                generalError = false
            )
        }
    }

    fun onLoginClick(){
        auth.signInWithEmailAndPassword(state.email,state.password).addOnCompleteListener{ test ->
            if(test.isSuccessful){
                Log.d("LOGIN","SIUUUUUUUU")
                //Navegar
            }
            else{
                Log.d("ERROR","NO LOGIN")
            }
        }
    }
}