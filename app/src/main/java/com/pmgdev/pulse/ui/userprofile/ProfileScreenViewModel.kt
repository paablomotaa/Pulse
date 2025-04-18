package com.pmgdev.pulse.ui.userprofile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
 * ViewModel del Profile Screen
 *
 * ViewModel que por ahora solo recoge los datos del usuario logueado.
 *
 */
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: UserRepository
) : ViewModel() {
    var state by mutableStateOf<ProfileScreenState>(ProfileScreenState.Loading)

    init {
        if (auth.currentUser != null) {
            getProfileData()
        } else {
            state = ProfileScreenState.NoData
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getProfileData(){
        viewModelScope.launch {
            val myuser = repository.getUser(auth.currentUser?.uid ?: "")
            Log.d("Usuario logeado", myuser?.fullname ?: "")
            state = ProfileScreenState.Success(myuser)
        }
    }

}