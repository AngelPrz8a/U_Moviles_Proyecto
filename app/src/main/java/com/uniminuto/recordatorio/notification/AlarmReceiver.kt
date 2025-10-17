// Archivo: com.uniminuto.recordatorio.notification/AlarmReceiver.kt

package com.uniminuto.recordatorio.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Recibe la señal del AlarmManager cuando es momento de mostrar el recordatorio.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // El BroadcastReceiver se ejecuta en el hilo principal, por lo que debe ser rápido.

        // 1. Extraer los datos que se le pasaron desde NotificationScheduler
        val title = intent.getStringExtra("EVENT_TITLE") ?: "Recordatorio"
        val eventId = intent.getIntExtra("EVENT_ID", -1)

        if (eventId != -1) {
            // 2. Usar el Helper para crear y mostrar la notificación
            NotificationHelper(context).showNotification(
                title = title,
                message = "¡Tu evento está por comenzar!",
                notificationId = eventId
            )
        }
    }
}