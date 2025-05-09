package com.pmgdev.pulse.repository.model

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.auth.FirebaseAuth
import java.util.Date

/**
 *
 * Modelo User
 *
 * Le queda hacer la subcoleccion de followers y following
 * Constructor vacío necesario para firestore.
 *
 */
data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val fullname: String = "",
    val bio: String = "",
    val peso: Int = 0,
    val altura: Int = 0,
    val created_at: Date = Date(),
    val followers: Int = 0,
    val following: Int = 0
) {
    constructor() : this("", "", "", "", "", 0, 0, Date(), 0, 0)
}
