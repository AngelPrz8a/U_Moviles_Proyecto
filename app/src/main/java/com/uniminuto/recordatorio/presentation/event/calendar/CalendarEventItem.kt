package com.uniminuto.recordatorio.presentation.event.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uniminuto.recordatorio.domain.model.Event
import com.uniminuto.recordatorio.presentation.ui.theme.DarkBackground
import com.uniminuto.recordatorio.presentation.ui.theme.NeonGreen
import com.uniminuto.recordatorio.presentation.ui.theme.NeonPurple
import com.uniminuto.recordatorio.util.DateUtils

@Composable
fun CalendarEventItem(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            // Fondo ligeramente más claro que el fondo principal para destacar
            containerColor = DarkBackground.copy(alpha = 0.8f),
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
            // Columna de la HORA (Lado Izquierdo)
            Column(
                modifier = Modifier.width(60.dp), // Ancho fijo para la hora
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ✅ SOLO SE MUESTRA LA HORA
                Text(
                    DateUtils.formatTime(event.dateTime),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = NeonGreen
                )
            }

            Spacer(Modifier.width(16.dp))

            // Columna de Título y Detalles (Lado Derecho)
            Column(modifier = Modifier.weight(1f)) {
                // ✅ SOLO SE MUESTRA EL TÍTULO DEL EVENTO
                Text(
                    event.title, // Asegúrate de que event.title solo contenga el texto del título
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonGreen
                )
                // ✅ SOLO SE MUESTRA LA UBICACIÓN SI EXISTE
                event.location?.let {
                    Text(
                        "Ubicación: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonPurple.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}