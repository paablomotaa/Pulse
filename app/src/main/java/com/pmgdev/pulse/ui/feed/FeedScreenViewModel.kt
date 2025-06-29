package com.pmgdev.pulse.ui.feed

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pmgdev.pulse.repository.firebaserepository.PostRepository
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import com.pmgdev.pulse.repository.model.Notification
import com.pmgdev.pulse.repository.model.NotificationType
import com.pmgdev.pulse.repository.model.Post
import com.pmgdev.pulse.repository.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject


/**
 *
 * FeedViewModel
 *
 * Recoge todo el contenido de la base de datos ordenado por fecha de subida
 * Le puse muchos controles por el tema de que no me iba bien
 *
 */
@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val postRepository: PostRepository,
    val auth: FirebaseAuth,
    val userRepository: UserRepository
) : ViewModel() {
    var state by mutableStateOf<FeedScreenState>(FeedScreenState.Loading)

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set


    var searchMode by mutableStateOf(false)
    var searchQuery by mutableStateOf("")

    fun getNotices() {
        viewModelScope.launch {
            val posts = postRepository.getPosts()

            if (posts.isEmpty()) {
                state = FeedScreenState.NoData
                Log.e("ERROR", "NO CONSIGUE LOS DATOS post is empty")
            } else {
                state = FeedScreenState.Success(posts)
                Log.e("SUCCESS", "CONSIGUE LOS DATOS")
            }
        }
    }

    fun observePosts() {
        postRepository.observePosts { updatedPost ->
            posts = updatedPost
        }
    }

    var searchResults by mutableStateOf<List<User>>(emptyList())
        private set

    fun searchUsers(query: String) {
        viewModelScope.launch {
            firestore.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    val users = result.documents.mapNotNull { it.toObject(User::class.java) }
                    searchResults = users.filter {
                        it.username.contains(query, ignoreCase = true)
                    }
                }
        }
    }

    fun toggleLike(postId: String) {
        val userId = auth.currentUser?.uid
        viewModelScope.launch {
            val hasLiked = postRepository.isPostLikedByUser(postId, userId.toString())
            val user = userRepository.getUser(userId.toString())

            if(user != null){
                if (hasLiked) {
                    postRepository.unlikePost(postId, userId.toString())
                } else {
                    postRepository.likePost(postId, userId.toString())
                    notifyUser(user.uid)
                }
            }

        }
    }

    suspend fun isPostLikedByUserSuspend(postId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        return postRepository.isPostLikedByUser(postId, userId)
    }

    private fun notifyUser(targetUid: String) {
        val currentUid = auth.currentUser?.uid ?: return
        viewModelScope.launch {

            val user = userRepository.getUser(currentUid)
            if (user != null) {
                userRepository.getUser(targetUid)
                val notification = Notification(
                    uid = "",
                    uidSender = currentUid,
                    uidReceiver = targetUid,
                    usernameSender = user.username,
                    notificationType = NotificationType.LIKE
                )
                val result = userRepository.pushNotificationFromUser(targetUid, notification)
                if (result.isSuccess) {
                    Log.d("Notificacion", "Notificacion enviada")
                } else {
                    Log.d("Notificacion", "Notificacion no enviada")
                }
            }
        }
    }
}