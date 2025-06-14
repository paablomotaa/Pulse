package com.pmgdev.pulse.ui.previewpost

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.repository.firebaserepository.PostRepository
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import com.pmgdev.pulse.repository.model.Comment
import com.pmgdev.pulse.repository.model.Fine
import com.pmgdev.pulse.repository.model.Notification
import com.pmgdev.pulse.repository.model.NotificationType
import com.pmgdev.pulse.repository.model.TypeFine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import javax.inject.Inject


/**
 *
 * PreviewPost
 * ViewModel para ver el preview. Con el método uploadImage
 * Cargamos la imagen pasandole el UID por navegación por parámetros.
 *
 * Tenemos los demás métodos para los comentarios.
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
    var isCurrentUserPost by mutableStateOf(false)
    private set
    var showDialogFine by mutableStateOf(false)
    private set
    var showDialogDelete by mutableStateOf(false)
    private set


    /**
     *
     * uploadImage
     *
     * Carga el post elegido por el usuario en un launchedEffect
     *
     */
    fun uploadImage(uid: String) {
        viewModelScope.launch {
            val post = postRepository.getPost(uid)
            Log.e("POST RECUPERADO", uid)
            if (post != null) {
                if(post.uiduser == auth.currentUser?.uid){
                    isCurrentUserPost = true
                }
                else{
                    isCurrentUserPost = false
                }
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

    /**
     *
     * observeComments
     *
     * Para recibir los comentarios a tiempo real.
     *
     */
    fun observeComments(postId: String) {
            postRepository.observeComments(postId) { updatedComments ->
                comments = updatedComments
                Log.d("ViewModel", "Comentarios recibidos en tiempo real: ${updatedComments.size}")
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
            val post = postRepository.getPost(uid)
            if (user != null && statecomment.comment.isNotEmpty() && post != null) {
                postRepository.addComments(
                    uid, uiduser.toString(),
                    user.username, statecomment.comment
                )
                statecomment = statecomment.copy(
                    sended = true,
                    error = false
                )
                notifyUser(post.uiduser)
            }
            else{
                statecomment = statecomment.copy(
                    sended = false,
                    error = true,
                )
            }
        }
    }

    /**
     *
     * notifyUser
     *
     * Notifica al usuario que ha recibido un comentario.
     *
     * @param targetUid
     *
     */
    private fun notifyUser(targetUid:String){
        val currentUid = auth.currentUser?.uid ?: return
        viewModelScope.launch {

            val user = userRepository.getUser(currentUid)
            if(user != null){
                userRepository.getUser(targetUid)
                val notification = Notification(
                    uid = "",
                    uidSender = currentUid,
                    uidReceiver = targetUid,
                    usernameSender = user.username,
                    notificationType = NotificationType.COMMENT
                )
                val result = userRepository.pushNotificationFromUser(targetUid,notification)
                if(result.isSuccess){
                    Log.d("Notificacion","Notificacion enviada")
                }
                else{
                    Log.d("Notificacion","Notificacion no enviada")
                }
            }
        }
    }

    fun showDialogDelete(){
        showDialogDelete = true
    }
    fun hideDialogDelete(){
        showDialogDelete = false
    }

    fun showDialogFine(){
        showDialogFine = true
    }

    fun hideDialogFine(){
        showDialogFine = false
    }

    fun deletePost(postId:String){
        viewModelScope.launch {
            postRepository.deletePost(postId)
        }
    }

    /**
     *
     * createFine
     *
     * Crea una penalización en la base de datos.
     *
     * @param chatId
     *
     */
    fun createFine(chatId: String) {
        viewModelScope.launch {
            postRepository.createFine(
                Fine(
                    userId = auth.currentUser?.uid ?: "",
                    reportedId = chatId,
                    TypeFine = TypeFine.POST
                )
            )
        }
    }
}