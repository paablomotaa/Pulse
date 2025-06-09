package com.pmgdev.pulse.repository.model

import com.google.firebase.Timestamp


//Este modelo es para ver bien los chats desde el chatlist. Nada relevante
data class ChatPreview(
    val chatId: String,
    val otherUserName: String,
    val otherUserImageUrl: String,
    val lastMessage: String,
    val timestamp: Timestamp
)