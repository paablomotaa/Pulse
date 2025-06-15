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

    fun onEditClick(goBack: () -> Boolean){
        if(hasEmptyFields()){
            state = state.copy(toastMessage = "Hay campos vacíos❌")
            return
        }
        if (validateFields()){
            state = state.copy(toastMessage = "Hay campos mal formados❌")
            return
        }
        viewModelScope.launch {
            val user = repository.getUser(auth.currentUser?.uid ?: "")
            val usernameexists = repository.checkusernameexists(state.username)
            if(usernameexists && user?.username != state.username){
                state = state.copy(
                    errorUsername = true,
                    errorUsernameText = "Error, este usuario ya existe",
                )
                return@launch
            }
            else{
                val user = repository.getUser(auth.currentUser?.uid ?: "")
                if(user != null){
                    val updatedUser = user.copy(
                        username = state.username,
                        fullname = state.fullname,
                        profileImage = state.image,
                        bio = state.bio,
                    )
                    repository.editUser(auth.currentUser?.uid ?: "",updatedUser)
                    state = state.copy(toastMessage = "Edición correcta.✅")
                    goBack()
                }
                else{

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
    fun clearToastMessage() {
        state = state.copy(toastMessage = null)
    }
}