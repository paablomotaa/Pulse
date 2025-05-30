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

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Message received: ${remoteMessage.notification?.body}")
        remoteMessage.notification?.let {
            showNotification(it.title ?: "Pulse", it.body ?: "")
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "pulse_channel_id"
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pulse Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}