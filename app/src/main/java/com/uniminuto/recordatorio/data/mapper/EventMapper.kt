package com.uniminuto.recordatorio.data.mapper

import com.uniminuto.recordatorio.data.local.entities.EventEntity
import com.uniminuto.recordatorio.domain.model.Event
/**
 * Función de extensión para mapear EventEntity a Event (Domain Model).
 */
fun EventEntity.toDomain(): Event = Event(
    id = this.id,
    title = this.title,
    description = this.description,
    location = this.location,
    dateTime = this.dateTime,
    isReminderSet = this.isReminderSet,
    reminderOffsetMinutes = this.reminderOffsetMinutes
)

/**
 * Función de extensión para mapear Event (Domain Model) a EventEntity.
 */
fun Event.toEntity(): EventEntity = EventEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    location = this.location,
    dateTime = this.dateTime,
    isReminderSet = this.isReminderSet,
    reminderOffsetMinutes = this.reminderOffsetMinutes
)