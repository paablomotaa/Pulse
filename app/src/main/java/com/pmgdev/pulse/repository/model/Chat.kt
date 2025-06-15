package com.pmgdev.pulse.repository.model

import com.google.firebase.Timestamp


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
    val lastMessageTimestamp: Timestamp = Timestamp.now()
)
