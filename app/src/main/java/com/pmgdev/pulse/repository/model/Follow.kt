package com.pmgdev.pulse.repository.model

import java.util.Date

data class Follow(
    val uid: String = "", 
    val username: String = "",
    val followedAt: Date = Date()
)