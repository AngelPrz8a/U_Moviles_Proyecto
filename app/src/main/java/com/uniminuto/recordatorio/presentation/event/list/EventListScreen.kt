package com.uniminuto.recordatorio.presentation.event.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

/**
 * Pantalla principal que muestra la lista de eventos. (RF08: Mis eventos)
 */
@Composable
fun EventListScreen(
    viewModel: EventViewModel,
    onNavigateToCreate: () -> Unit // Se usará al hacer clic en un Fab (opcional)
) {
    val state by viewModel.eventListState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground) // Estética oscura futurista
            .padding(16.dp)
    ) {
        Text(
            "MIS EVENTOS",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = NeonGreen,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (state.isLoading) {
            Text("Cargando datos...", color = NeonPurple)
        } else if (state.events.isEmpty()) {
            Text("No tienes eventos registrados. ¡Crea uno!", color = NeonPurple)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.events) { event ->
                    EventItem(
                        event = event,
                        onDelete = { viewModel.deleteEvent(event) },
                        onEdit = { /* Lógica para edición */ }
                    )
                }
            }
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        colors = CardDefaults.cardColors(
            containerColor = DarkBackground.copy(alpha = 0.7f), // Fondo semitransparente
            contentColor = NeonPurple
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NeonGreen
                )
                Text(
                    DateUtils.formatDateTime(event.dateTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonPurple
                )
                event.location?.let {
                    Text("Ubicación: $it", style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = NeonGreen
                )
            }
        }
    }
}