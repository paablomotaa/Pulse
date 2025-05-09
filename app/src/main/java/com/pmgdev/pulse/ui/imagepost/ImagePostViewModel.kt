package com.pmgdev.pulse.ui.imagepost

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import javax.inject.Inject
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.repository.firebaserepository.PostRepository
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

/**
 *
 * ImagePost
 *
 * Para subir publicaciones. Podremos asignar imagen y descripción.
 * Esta información se guardará tanto en firestorage como en firestore
 *
 *
 */

@HiltViewModel
class ImagePostViewModel @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val postRepository: PostRepository,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {
    var state by mutableStateOf<ImagePostScreenState>(ImagePostScreenState())

    fun onImageChange(image:String){
        if(image.isEmpty()){
            state = state.copy(
                image = image,
                isErrorImage = true,
                errorTextImage = "La imagen esta vacía"
            )
        }
        else{
            state = state.copy(
                image = image,
                isErrorImage = false,
                errorTextImage = ""
            )
        }
    }
    fun onDescriptionChange(description: String){
        state = state.copy(
            description = description
        )
    }

    fun onPostClick(){
        val uri = state.image.toUri()

        viewModelScope.launch {
            val user = userRepository.getUser(auth.currentUser?.uid.toString())
            postRepository.addPost(
                uri = uri,
                uiduser = auth.currentUser?.uid ?: "null",
                username = user?.username ?: "null",
                description = state.description,
            )
        }
    }
}