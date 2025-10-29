package com.uniminuto.recordatorio.presentation.event.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uniminuto.recordatorio.domain.model.Event
import com.uniminuto.recordatorio.presentation.event.EventViewModel
import com.uniminuto.recordatorio.presentation.ui.theme.DarkBackground
import com.uniminuto.recordatorio.presentation.ui.theme.NeonGreen
import com.uniminuto.recordatorio.presentation.ui.theme.NeonPurple
import com.uniminuto.recordatorio.util.DateUtils
import java.time.format.DateTimeFormatter
import java.util.Locale

// File: CalendarScreen.kt (Reemplazar la función CalendarScreen)

@Composable
fun CalendarScreen(
    viewModel: EventViewModel
) {
    val state by viewModel.eventListState.collectAsState()

    // Agrupar los eventos por fecha
    val groupedEvents = state.events
        .sortedBy { it.dateTime } // Aseguramos que estén ordenados por fecha y hora
        .groupBy {
            DateUtils.formatDateFull(it.dateTime)
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Text(
            "CALENDARIO DE EVENTOS",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = NeonGreen,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ... (Carga y estado vacío)

        if (groupedEvents.isEmpty()) {
            Text("El calendario está vacío. ¡Crea un evento!", color = NeonPurple)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) { // Más espacio entre días
                groupedEvents.forEach { (dateKey, eventsList) ->
                    item {
                        // 2. Encabezado de la Fecha: Más grande y separado
                        Text(
                            dateKey,
                            style = MaterialTheme.typography.titleLarge, // Título más grande
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonPurple, // Usar un color secundario para el encabezado
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }

                    // 3. Listado de Eventos para esa fecha
                    items(eventsList) { event ->
                        CalendarEventItem(event)
                    }
                }
            }
        }
    }
}

