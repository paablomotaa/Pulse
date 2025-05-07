package com.pmgdev.pulse.repository.model

import java.util.Date

data class Comment(
    val uidUser: String = "",
    val username: String = "",
    val content: String = "",
    val timestamp: Date = Date()
)