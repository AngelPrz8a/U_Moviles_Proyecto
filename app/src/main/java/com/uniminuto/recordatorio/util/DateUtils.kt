package com.uniminuto.recordatorio.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Utilidades para formatear fechas y horas.
 */
object DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy 'a las' HH:mm")

    fun formatDateTime(dateTime: LocalDateTime): String {
        return try {
            dateTime.format(formatter)
        } catch (e: Exception) {
            "Fecha/Hora inválida"
        }
    }

    fun formatDateFull(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        return dateTime.format(formatter).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString()
        }
    }

    fun formatDate(dateTime: LocalDateTime): String {
        return try {
            val dateOnlyFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("es", "ES"))
            dateTime.format(dateOnlyFormatter)

        } catch (e: Exception) {
            "Fecha inválida"
        }
    }

    /**
     * Formatea solo la parte de la hora (ej: 14:30).
     */
    fun formatTime(dateTime: LocalDateTime): String {
        return try {
            val timeOnlyFormatter = DateTimeFormatter.ofPattern("HH:mm")
            dateTime.format(timeOnlyFormatter)
        } catch (e: Exception) {
            "Hora inválida"
        }
    }

}