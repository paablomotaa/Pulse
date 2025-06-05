package com.pmgdev.pulse.repository.model

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
    val timestamp: Long = System.currentTimeMillis()
)