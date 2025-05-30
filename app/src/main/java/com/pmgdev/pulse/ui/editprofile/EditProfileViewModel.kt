package com.pmgdev.pulse.ui.editprofile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *
 * ViewModel para la edición del perfil del usuario. Falta el observer para que se actualicen a tiempo real.(tarda un poco en actualizar)
 *
 */

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    val repository: UserRepository,
    val auth: FirebaseAuth
) : ViewModel() {
    var state by mutableStateOf(EditProfileState())
        private set


    fun ChargeUserData(){
        viewModelScope.launch {
            val user = repository.getUser(auth.currentUser?.uid ?: "")
            if(user != null){
                state = state.copy(
                    image = user.profileImage,
                    username = user.username,
                    fullname = user.fullname,
                    bio = user.bio,
                    peso = user.peso,
                    altura = user.altura
                )
            }
        }

    }
    fun onImageChange(image:String){
        if(image.isEmpty()){
            state = state.copy(
                image = image,
                imageError = true,
                errorImageText = "La imagen esta vacía"
            )
        }
        else{
            state = state.copy(
                image = image,
                imageError = false,
                errorImageText = ""
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
                errorUsername = true,
                errorUsernameText = ""
            )
        }
        else{
            state = state.copy(
                username = username,
                errorUsername = false,
                errorUsernameText = ""
            )
        }
    }
    fun onNameChange(name:String){
        if(name.isEmpty()){
            state = state.copy(
                fullname = name,
                fullnameError = true,
                errorFullnameText = "Esta vacío"
            )
        }
        else{
            state = state.copy(
                fullname = name,
                fullnameError = false,
                errorFullnameText = ""
            )
        }
    }
    fun onBioChange(bio:String){
        state = state.copy(
            bio = bio,
            bioError = false,
            errorBioText = ""
        )
    }

    fun onPesoChange(peso: String) {
        val value = peso.toIntOrNull() ?: 0
        state = state.copy(peso = value)
    }
    fun onAlturaChange(altura:String){
        val value = altura.toIntOrNull() ?: 0

        state = state.copy(altura = value)
    }
    fun onEditClick(goBack: () -> Boolean){
        if(hasEmptyFields()){
            Log.d("ERROR","CAMPO VACIO")
            return
        }
        if (validateFields()){
            Log.d("ERROR","CAMPO MAL VALIDADO")
            return
        }
        viewModelScope.launch {
            val user = repository.getUser(auth.currentUser?.uid ?: "")
            val usernameexists = repository.checkusernameexists(state.username)
            if(usernameexists && user?.username != state.username){
                state = state.copy(
                    errorUsername = true,
                    errorUsernameText = "Error, este usuario ya existe"
                )
                Log.d("ERROR","YA EXISTE EL USUARIO")
                return@launch
            }
            else{
                val user = repository.getUser(auth.currentUser?.uid ?: "")
                Log.d("USER", auth.currentUser?.uid ?: "NO UID")
                if(user != null){
                    val updatedUser = user.copy(
                        username = state.username,
                        profileImage = state.image,
                        bio = state.bio,
                        altura = state.altura,
                        peso = state.peso
                    )
                    Log.d("OK","OK")
                    repository.editUser(auth.currentUser?.uid ?: "",updatedUser)
                    goBack() //lo pongo aqui porque si lo pongo en el otro lado no le da tiempo a acabar
                }
                else{
                    Log.d("ERROR","ERRORRRR"
                    )
                }
            }
        }
    }

    private fun hasEmptyFields():Boolean{
        var hasEmptyFields = false

        if(state.fullname.isEmpty()){
            state = state.copy(
                fullnameError = true,
                errorFullnameText = "Este campo no puede estar vacío"
            )
            hasEmptyFields = true
        }

        return hasEmptyFields
    }
    private fun validateFields():Boolean{
        return state.errorUsername || state.fullnameError || state.bioError
    }
}