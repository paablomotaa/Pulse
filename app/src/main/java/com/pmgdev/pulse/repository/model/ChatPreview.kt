package com.pmgdev.pulse.repository.model

data class ChatPreview(
    val chatId: String,
    val otherUserName: String,
    val otherUserImageUrl: String,
    val lastMessage: String,
    val timestamp: String
)