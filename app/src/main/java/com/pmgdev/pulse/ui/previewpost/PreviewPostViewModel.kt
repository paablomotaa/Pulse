package com.pmgdev.pulse.ui.previewpost

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.repository.firebaserepository.PostRepository
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import com.pmgdev.pulse.repository.model.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *
 * PreviewPost
 * ViewModel para ver el preview. Con el método uploadImage
 * Cargamos la imagen pasandole el UID por navegación por parámetros.
 *
 * Tenemos los demás métodos para los comentarios. El de observecomments es para que la
 * vista se pueda actualizar si hay algun cambio en la base de datos.
 *
 */
@HiltViewModel
class PreviewPostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {
    var state by mutableStateOf<PreviewPostState>(PreviewPostState.Loading)
        private set
    var comments by mutableStateOf<List<Comment>>(emptyList())
        private set
    var statecomment by mutableStateOf(PreviewPostCommentState())
        private set

    fun uploadImage(uid: String) {
        viewModelScope.launch {
            val post = postRepository.getPost(uid)
            Log.e("POST RECUPERADO", uid)
            if (post != null) {
                comments = postRepository.getComments(uid)
                Log.d("Comentarios recuperados", comments.count().toString())
                state = PreviewPostState.Success(post,comments)
                Log.d("Post recuperado", "post recuperado de la base de datos")
            } else {
                state = PreviewPostState.NoData
                Log.d("Post no recuperado", "ERROR")
            }
        }
    }
    fun observeComments(postId: String) {
        postRepository.observeComments(postId) { updatedComments ->
            comments = updatedComments
        }
    }

    fun onContentChange(comment: String) {
        statecomment = statecomment.copy(
            comment = comment
        )
    }

    /**
     *
     * PostComent
     * @param uid
     *
     * Le pasamos el uid del usuario para tener un control de quien ha puesto el comentario.
     * Además, el método también incrementa en uno el número de likes que se guarda en la base de datos
     *
     */
    fun postComment(uid: String) {
        val uiduser = auth.currentUser?.uid
        viewModelScope.launch {
            val user = userRepository.getUser(uiduser.toString())
            if (user != null && statecomment.comment.isNotEmpty()) {
                postRepository.addComments(
                    uid, uiduser.toString(),
                    user.username, statecomment.comment
                )
                statecomment = statecomment.copy(
                    sended = true,
                    error = false
                )
            }
            else{
                statecomment = statecomment.copy(
                    sended = false,
                    error = true,
                )
            }
        }
    }
}