package com.pmgdev.pulse.repository.model

import java.util.Date

/**
 *
 * Clase Follow
 *
 * Representa la solicitud de seguimiento que se guarda en la base de datos al seguir a otro usuario.
 *
 */
data class Follow(
    val uid: String = "", 
    val username: String = "",
    val followedAt: Date = Date()
)