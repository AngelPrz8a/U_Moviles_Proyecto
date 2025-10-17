package com.uniminuto.recordatorio.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
}