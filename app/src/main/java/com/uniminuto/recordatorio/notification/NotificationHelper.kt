// Archivo: com.uniminuto.recordatorio.notification/NotificationHelper.kt

package com.uniminuto.recordatorio.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.uniminuto.recordatorio.R // Asegúrate de que este import funciona

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "recordatorio_channel"
    private val CHANNEL_NAME = "Recordatorios de Eventos"
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        // Inicializa el canal de notificación (obligatorio en Android 8.0 / API 26+)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para todas las notificaciones de recordatorios."
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, message: String, notificationId: Int) {
        // 1. Crear la notificación
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // 👈 Usar un ícono adecuado (ej. ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Se cierra al tocarla
            .build()

        // 2. Mostrar la notificación (usando el ID del evento como ID de notificación)
        notificationManager.notify(notificationId, notification)
    }
}