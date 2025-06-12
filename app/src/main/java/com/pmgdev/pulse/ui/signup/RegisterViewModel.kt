package com.pmgdev.pulse.ui.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import com.pmgdev.pulse.repository.model.User
import com.pmgdev.pulse.utils.ValidatePassword
import com.pmgdev.pulse.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject


/**
 *
 * RegisterViewModel
 *
 * Aun se deben añadir muchos campos(inclusive el datepicker correctamente)
 * para probar funciona correctamente.
 *
 */

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    val repository: UserRepository
) : ViewModel() {
    var state by mutableStateOf(RegisterScreenState())
        private set

    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


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
    fun onDateSelected(date: LocalDate) {
        val dateAsString = date.format(dateFormatter)
        state = state.copy(
            date = dateAsString,
            isDateError = false,
            dateErrorText = ""
        )
    }

    fun showDatePicker() {
        state = state.copy(showDatePickerDialog = true)
    }

    fun hideDatePicker() {
        state = state.copy(showDatePickerDialog = false)
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
        if(password.length < 6 || !ValidatePassword(password)){
            state = state.copy(
                password = password,
                isPasswordError = true,
                passwordErrorText = "La contraseña debe tener al menos 6 carácteres, una mayúscula, un caracter especial y un dígito."
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
    fun onUsernameChange(username:String){
        if(username.contains(' ')){
            return
        }
        if(username.isEmpty()){
            state = state.copy(
                username = username,
                isUsernameError = true,
                usernameErrorText = ""
            )
        }
        else{
            state = state.copy(
                username = username,
                isUsernameError = false,
                usernameErrorText = ""
            )
        }
    }
    fun onRegisterClick(goToLogin: () -> Unit) {
        if (validateFields()){
            state = state.copy(toastMessage = "Hay campos incorrectos❌")
            return
        }

        if(hasEmptyFields()){
            state = state.copy(toastMessage = "Hay campos vacíos❌")
            return
        }
        viewModelScope.launch {
            val usernameexists = repository.checkusernameexists(state.username)
            val emailexists = repository.checkemailexists(state.email)
            if(usernameexists){
                state = state.copy(
                    isUsernameError = true,
                    usernameErrorText = "Error, este usuario ya existe"
                )
                return@launch
            }
            if(emailexists){
                state = state.copy(
                    isEmailError = true,
                    emailErrorText = "Error, este email ya existe"
                )
                return@launch
            }
            else{
                auth.createUserWithEmailAndPassword(state.email,state.password).addOnCompleteListener{ test ->
                    if(test.isSuccessful){

                        val user = auth.currentUser
                        val newUser = User(
                            uid = user?.uid ?: "",
                            username = state.username,
                            email = state.email,
                            fullname = state.name,
                            bio = "",
                            created_at = Date(),
                            followers = 0,
                            following = 0,
                            title = ""
                        )
                        viewModelScope.launch {
                            repository.addUser(newUser)
                        }
                        state = state.copy(toastMessage = "Registro correcto✅")
                        goToLogin()
                    }
                    else{
                        Log.d("FAILED","FAILED")
                    }
                }
            }
        }

    }

    private fun hasEmptyFields():Boolean{
        var hasEmptyFields = false

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
                passwordErrorText = "Este campo no puede estar vacío",
            )

            hasEmptyFields = true
        }

        return hasEmptyFields
    }

    private fun validateFields():Boolean{
        return state.isNameError || state.isEmailError || state.isDateError || state.isPasswordError
    }
    fun clearToastMessage() {
        state = state.copy(toastMessage = null)
    }
}