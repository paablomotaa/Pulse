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
    val profileImage:String = "",
    val username: String = "",
    val email: String = "",
    val fullname: String = "",
    val bio: String = "",
    val created_at: Date = Date(),
    val followers: Int = 0,
    val following: Int = 0,
    val title:String = ""
) {
    constructor() : this("","", "", "", "", "", Date(), 0, 0,"")
}
