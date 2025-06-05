package com.pmgdev.pulse.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.network.Session
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val session: Session
) : ViewModel() {

    var state by mutableStateOf(SettingsScreenState())
        private set

    fun logout(goToLogin: () -> Unit) {
        viewModelScope.launch {
            auth.signOut()
            session.clearSession()
            goToLogin()
        }
    }

    fun deleteAccount(goToLogin: () -> Unit) {
            viewModelScope.launch {
                val result = userRepository.deleteAccount()
                if (result.isSuccess) {
                    state = state.copy(toastMessage = "Cuenta eliminada correctamente.")
                    goToLogin()
                } else {
                    state = state.copy(toastMessage = "Error al eliminar la cuenta: ${result.exceptionOrNull()?.message ?: "Desconocido"}")
                }
            }
    }

    fun changePassword() {
        val email = auth.currentUser?.email
        auth.sendPasswordResetEmail(email.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state = state.copy(toastMessage = "Te hemos enviado un correo.")
                } else {
                    state = state.copy(toastMessage = "ERROR AL ENVIAR EL CORREO")
                }
            }
    }


    fun changeEmail(newEmail: String) {
        viewModelScope.launch {
            val result = userRepository.changeEmail(newEmail)
            state = if (result.isSuccess) {
                state.copy(toastMessage = "Email cambiado correctamente.")
            } else {
                state.copy(toastMessage = "Error al cambiar el email: ${result.exceptionOrNull()?.message ?: "Desconocido"}")
            }
        }
    }

    fun contactSupport(open: (String) -> Unit) {
        open("https://paablomotaa.github.io/PulsePage/contactus/")
    }
    fun showEmailDialog() {
        state = state.copy(showEmailDialog = true)
    }
    fun hideEmailDialog(){
        state = state.copy(showEmailDialog = false)
    }
    fun showDeleteDialog(){
        state = state.copy(showDeleteDialog = true)
    }
    fun hideDeleteDialog(){
        state = state.copy(showDeleteDialog = false)

    }
}