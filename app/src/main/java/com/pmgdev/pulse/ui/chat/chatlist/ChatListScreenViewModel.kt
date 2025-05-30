package com.pmgdev.pulse.ui.chat.chatlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.repository.firebaserepository.ChatRepository
import com.pmgdev.pulse.repository.model.ChatPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListScreenViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    var chatPreviews = mutableStateOf<List<ChatPreview>>(emptyList())
    private set

    fun loadChats() {
        val uid = auth.currentUser?.uid
        viewModelScope.launch {
            val result = repository.getChatFromUser(uid.toString())
            chatPreviews.value = result
        }
    }
}