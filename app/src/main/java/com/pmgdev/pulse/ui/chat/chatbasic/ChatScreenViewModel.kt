package com.pmgdev.pulse.ui.chat.chatbasic

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.pmgdev.pulse.repository.firebaserepository.ChatRepository
import com.pmgdev.pulse.repository.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *
 * ViewModel para el chat.
 *
 */
@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val auth: FirebaseAuth
): ViewModel() {

    var text by mutableStateOf("")
        private set
    var messages = mutableStateOf<List<Message>>(emptyList())
    private set
    private var messageListener: ListenerRegistration? = null //Para que se pueda cambiar de chat y siga escuchando otros mensajes

    fun onTextChange(txt:String){
        text = txt
    }


    fun observeMessages(chatId: String) {
        messageListener?.remove()

        messageListener = repository.observeMessages(
            chatId = chatId,
            onChange = { msgs ->
                messages.value = msgs
            },
            onError = { e ->
                Log.d("ERROR observeMessages", e.message.toString())
            }
        )
    }

    fun loadMessages(chatId: String) {

        viewModelScope.launch {
            repository.getMessages(
                chatId = chatId,
                { msg ->
                    messages.value = msg
                },
                { e ->
                    Log.d("ERROR loadingMessages", e.message.toString())
                }
            )
        }
    }

    fun sendMessage(chatId: String) {
        val message = Message(
            id = "",
            senderId = auth.currentUser?.uid ?: "",
            text = text,
            timestamp = System.currentTimeMillis()
        )

        repository.sendMessage(
            chatId = chatId,
            message = message,
        )
    }
    fun messageIsFrom(senderId:String):Boolean{
        val currentUid = auth.currentUser?.uid
        if(currentUid != senderId){
            return false
        }
        else{
            return true
        }
    }
}