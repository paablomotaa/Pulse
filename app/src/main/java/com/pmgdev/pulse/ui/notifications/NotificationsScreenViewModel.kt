package com.pmgdev.pulse.ui.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import com.pmgdev.pulse.repository.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    var state by mutableStateOf<NotificationsScreenState>(NotificationsScreenState.isLoading)
    private set

    /**
     *
     * getNotifications
     *
     * Obtiene las notificaciones del usuario.
     *
     * @param notifications
     *
     */
    fun getNotifications(){
        val userID = auth.currentUser?.uid
        if (userID != null) {
            viewModelScope.launch{
                val notifications = userRepository.getNotificationsFromUser(userID)
                if(notifications.isNotEmpty()){
                    state = NotificationsScreenState.Success(notifications)
                }
                else{
                    state = NotificationsScreenState.NoData
                }
            }

        }
        else{
            state = NotificationsScreenState.NoData
        }
    }
}