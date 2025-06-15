package com.pmgdev.pulse.repository.model

import com.google.firebase.Timestamp
import java.util.Date

/**
 *
 * Notification
 *
 * Representa las notificaciones que se van generando.
 *
 */
data class Notification(
    val uid:String,
    val uidSender:String,
    val uidReceiver:String,
    val usernameSender:String,
    val notificationType: NotificationType,
    val timestamp: Date = Date()
){
    constructor() : this("","","","", NotificationType.FOLLOW,Date())

    @Override
    override fun toString(): String {
        when(notificationType){
            NotificationType.FOLLOW -> return "El usuario $usernameSender te ha seguido."
            NotificationType.LIKE -> return "El usuario $usernameSender ha dado like a tu post."
            NotificationType.COMMENT -> return "El usuario $usernameSender comentó tu post."
        }
    }
}

enum class NotificationType{
    FOLLOW,
    LIKE,
    COMMENT
}
