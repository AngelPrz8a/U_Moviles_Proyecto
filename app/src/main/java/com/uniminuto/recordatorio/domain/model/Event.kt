package com.uniminuto.recordatorio.domain.model

import java.time.LocalDateTime

/**
 * Modelo de datos puro del dominio, independiente de la capa de datos.
 */
data class Event(
    val id: Int = 0,
    val title: String,
    val description: String?,
    val location: String?,
    val dateTime: LocalDateTime,
    val isReminderSet: Boolean,
    val reminderOffsetMinutes: Int
)