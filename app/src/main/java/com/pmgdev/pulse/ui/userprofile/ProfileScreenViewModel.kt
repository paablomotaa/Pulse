    package com.pmgdev.pulse.ui.userprofile

    import android.util.Log
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.setValue
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore
    import com.pmgdev.pulse.repository.firebaserepository.ChatRepository
    import com.pmgdev.pulse.repository.firebaserepository.PostRepository
    import com.pmgdev.pulse.repository.firebaserepository.UserRepository
    import com.pmgdev.pulse.repository.model.Notification
    import com.pmgdev.pulse.repository.model.NotificationType
    import com.pmgdev.pulse.repository.model.Post
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.tasks.await
    import javax.inject.Inject

    /**
     *
     * ViewModel del Profile Screen
     *
     * Recoge los datos del usuario y los muestra en pantalla.
     *
     */
    @HiltViewModel
    class ProfileScreenViewModel @Inject constructor(
        private val auth: FirebaseAuth,
        private val repository: UserRepository,
        private val db: FirebaseFirestore,
        private val repositorych: ChatRepository,
        private val repositorypost: PostRepository
    ) : ViewModel() {
        var state by mutableStateOf<ProfileScreenState>(ProfileScreenState.Loading)
        var isFollowing by mutableStateOf(false)
        var isCurrentUser by mutableStateOf(true)
        var userPosts by mutableStateOf<List<Post>>(emptyList())


        /**
         *
         * getProfileData
         *
         * Recoge la información de la base de datos ejecutando este método en un launched effect
         * para que los recoja antes de que cargue la pantalla.
         *
         */
        fun getProfileData(userId: String) {
            viewModelScope.launch {
                val currentUid = auth.currentUser?.uid ?: return@launch
                if (userId == "{userId}" || userId == currentUid) {
                    val user = repository.getUser(currentUid)
                    state = ProfileScreenState.Success(user)
                    isCurrentUser = true
                    checkIfFollowing(user?.uid ?: "")
                } else {
                    val user = repository.getUser(userId)
                    state = ProfileScreenState.Success(user)
                    isCurrentUser = false
                    checkIfFollowing(user?.uid ?: "")
                }
            }
        }

        /**
         *
         * checkIfFollowing
         *
         * Metodo para chequear si el usuario sigue o no a la persona del parámetro.
         *
         */
        private fun checkIfFollowing(targetUid: String) {
            val currentUid = auth.currentUser?.uid ?: return
            viewModelScope.launch {
                val doc = db.collection("users").document(currentUid)
                    .collection("following").document(targetUid)
                    .get()
                    .await()
                isFollowing = doc.exists()
            }
        }

        /**
         *
         * toggleFollow
         *
         * Metodo de alternación entre seguido y no seguido.
         *
         *
         */

        fun toggleFollow(targetUid: String) {
            val currentUid = auth.currentUser?.uid ?: return
            viewModelScope.launch {
                if (isFollowing) {
                    repository.unfollowUser(
                        currentUid, targetUid,
                    )
                    isFollowing = false
                } else {
                    Log.d("Paso por aqui","Paso por aqui voy a seguir a " + targetUid)
                    repository.followUser(
                        currentUid = currentUid,
                        targetUid = targetUid,
                        currentUsername = "",
                        targetUsername = "",
                    )
                    notifyUser(targetUid)
                    isFollowing = true
                }
                val updatedUser = repository.getUser(targetUid)
                state = ProfileScreenState.Success(updatedUser)
            }
        }

        /**
         *
         *notifyUser
         *
         * Metodo para notificar a un usuario que ha seguido a otro.
         *
         * @param targetUid
         *
         */
        private fun notifyUser(targetUid:String){
            val currentUid = auth.currentUser?.uid ?: return
            viewModelScope.launch {

                val user = repository.getUser(currentUid)
                if(user != null){
                    repository.getUser(targetUid)
                    val notification = Notification(
                        uid = "",
                        uidSender = currentUid,
                        uidReceiver = targetUid,
                        usernameSender = user.username,
                        notificationType = NotificationType.FOLLOW
                    )
                    val result = repository.pushNotificationFromUser(targetUid,notification)
                    if(result.isSuccess){
                        Log.d("Notificacion","Notificacion enviada")
                    }
                    else{
                        Log.d("Notificacion","Notificacion no enviada")
                    }
                }
            }
        }
        /**
         *
         * iniciateChat
         *
         * Metodo para crear un chat.
         *
         */
        fun iniciateChat(
            userId: String,
            goChat:(String) -> Unit
        ){
            viewModelScope.launch {
                repositorych.createChatIfNotExists(
                    userId1 = auth.currentUser?.uid ?: "",
                    userId2 = userId,
                    onComplete = { chatId ->
                        Log.d("Navegando al chat",chatId)
                        goChat(chatId)
                    }
                )
            }
        }

        /**
         *
         * getUserPosts
         *
         * Metodo para obtener los posts de un usuario.
         *
         * @param userId
         *
         */
        fun getUserPosts(userId: String) {
            Log.d("POSTS","OBTENIENDO POSTS DE " + userId)
            viewModelScope.launch {
                    if(userId == "{userId}"){
                        val list = repositorypost.getPostsFromUser(auth.currentUser?.uid ?: "")
                        if(list.isNotEmpty()){
                            userPosts = list
                        }

                    }
                    else{
                        Log.d("OBTENIENDO POSTS",userId)
                        val list = repositorypost.getPostsFromUser(userId)
                        if(list.isNotEmpty()){
                            Log.d("Post obtenidos","post obtenidos")
                            userPosts = list
                        }
                    }
            }
        }
    }