package com.uniminuto.recordatorio.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.uniminuto.recordatorio.data.local.entities.EventEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object para la entidad EventEntity.
 * Define las operaciones CRUD (RF01, RF02, RF03).
 */
@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: EventEntity) // RF01

    @Update
    suspend fun updateEvent(event: EventEntity) // RF02

    @Delete
    suspend fun deleteEvent(event: EventEntity) // RF03

    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEventById(eventId: Int): EventEntity?
}