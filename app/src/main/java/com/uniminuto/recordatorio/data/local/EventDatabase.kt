package com.uniminuto.recordatorio.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uniminuto.recordatorio.data.local.dao.EventDao
import com.uniminuto.recordatorio.data.local.entities.EventEntity

/**
 * Configuración de la base de datos Room. (RF15)
 */
@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}