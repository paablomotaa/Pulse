package com.pmgdev.pulse.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pmgdev.pulse.R
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * Clase de Service Messaging
 *
 * Para las notificaciones. He llegado hasta aquí pero tengo que ver como integrarlo con el chat
 * para que se automaticen las notificaciones con los mensajes.
 * Ahora mismo desde firebase puedo enviar notificaciones.
 *
 */

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}