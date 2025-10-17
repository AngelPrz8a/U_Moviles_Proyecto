package com.uniminuto.recordatorio.domain.repository

import com.uniminuto.recordatorio.domain.model.Event
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del Repositorio de Eventos.
 */
interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    suspend fun getEventById(id: Int): Event?
    suspend fun saveEvent(event: Event) // RF01, RF02
    suspend fun deleteEvent(event: Event) // RF03
}