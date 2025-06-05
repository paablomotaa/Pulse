package com.pmgdev.pulse.repository.model


/**
 *
 * Clase chat
 *
 *
 * Representa los chats.
 *
 */
data class Chat(
    val id: String = "",
    val participants: List<String> = listOf(),
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = System.currentTimeMillis()
)
