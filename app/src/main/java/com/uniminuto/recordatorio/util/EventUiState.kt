package com.uniminuto.recordatorio.util

import com.uniminuto.recordatorio.domain.model.Event
import java.time.LocalDateTime

/**
 * Estado que representa los datos del formulario de Crear/Editar Evento.
 */
data class EventUiState(
    val id: Int = 0,
    val title: String = "", // RF01
    val description: String = "",
    val location: String = "", // RF01
    val dateTime: LocalDateTime = LocalDateTime.now(), // RF01
    val isReminderSet: Boolean = true, // RF04
    val reminderOffsetMinutes: Int = 10,
) {
    /**
     * Convierte el UiState a un modelo de dominio Event para guardar.
     */
    fun toEvent(): Event = Event(
        id = id,
        title = title,
        description = description.takeIf { it.isNotBlank() },
        location = location.takeIf { it.isNotBlank() },
        dateTime = dateTime,
        isReminderSet = isReminderSet,
        reminderOffsetMinutes = reminderOffsetMinutes
    )

    /**
     * Valida si el formulario es apto para guardar.
     */
    fun isValid(): Boolean {
        return title.isNotBlank() // El título es obligatorio (RF01)
    }
}