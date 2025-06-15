package com.pmgdev.pulse.repository.model

import com.google.firebase.Timestamp

/**
 *
 * Clase Message
 *
 * Representa cada uno de los mensajes del chat.
 *
 */
data class Message(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val keyAesSender: String = "",
    val keyAesReceiver: String = ""
)