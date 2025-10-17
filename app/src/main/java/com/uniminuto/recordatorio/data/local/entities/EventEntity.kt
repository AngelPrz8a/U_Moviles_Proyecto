package com.uniminuto.recordatorio.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String, // RF01
    val description: String?, // RF01
    val location: String?, // RF01
    val dateTime: LocalDateTime, // RF01
    val isReminderSet: Boolean = false, // RF04
    val reminderOffsetMinutes: Int = 10
)