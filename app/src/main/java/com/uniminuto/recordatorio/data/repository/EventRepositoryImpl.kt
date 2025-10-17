package com.uniminuto.recordatorio.data.repository

import com.uniminuto.recordatorio.data.local.dao.EventDao
import com.uniminuto.recordatorio.data.mapper.toDomain
import com.uniminuto.recordatorio.data.mapper.toEntity
import com.uniminuto.recordatorio.domain.model.Event
import com.uniminuto.recordatorio.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación del EventRepository que usa el DAO de Room.
 */
class EventRepositoryImpl(private val dao: EventDao) : EventRepository {

    override fun getEvents(): Flow<List<Event>> {
        return dao.getAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getEventById(id: Int): Event? {
        return dao.getEventById(id)?.toDomain()
    }

    override suspend fun saveEvent(event: Event) {
        dao.insertEvent(event.toEntity()) // Lógica para crear/actualizar
    }

    override suspend fun deleteEvent(event: Event) {
        dao.deleteEvent(event.toEntity()) // RF03
    }
}