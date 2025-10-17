package com.uniminuto.recordatorio.util

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import com.uniminuto.recordatorio.domain.model.Event
import com.uniminuto.recordatorio.notification.AlarmReceiver // Necesitarás crear este archivo
import java.time.ZoneId

class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun schedule(event: Event) {
        // 1. Crear el Intent para el BroadcastReceiver
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EVENT_TITLE", event.title)
            putExtra("EVENT_ID", event.id)
            // Aquí puedes añadir más datos
        }

        // 2. Crear el PendingIntent (usa el ID del evento como request code)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.id, // ID ÚNICO
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Calcular el tiempo de disparo (fecha del evento - offset)
        val triggerTimeMillis = event.dateTime.minusMinutes(event.reminderOffsetMinutes.toLong())
            .atZone(ZoneId.systemDefault()) // Importante para la zona horaria
            .toInstant().toEpochMilli()

        // 4. Programar la alarma (RF04)
        alarmManager.setExactAndAllowWhileIdle( // Usa setExactAndAllowWhileIdle para precisión
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )
    }

    fun cancel(eventId: Int) {
        // Lógica para cancelar una alarma existente
        // ... (Similar a schedule, pero usando alarmManager.cancel(pendingIntent))
    }
}