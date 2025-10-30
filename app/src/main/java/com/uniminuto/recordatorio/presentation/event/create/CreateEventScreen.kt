package com.uniminuto.recordatorio.presentation.event.create

// Importaciones de Compose Material 3
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uniminuto.recordatorio.presentation.event.EventViewModel
import com.uniminuto.recordatorio.presentation.ui.theme.DarkBackground
import com.uniminuto.recordatorio.presentation.ui.theme.NeonBlue
import com.uniminuto.recordatorio.presentation.ui.theme.NeonGreen
import com.uniminuto.recordatorio.presentation.ui.theme.NeonPurple
import com.uniminuto.recordatorio.presentation.ui.theme.NeonRed
import com.uniminuto.recordatorio.util.DateUtils

// Importaciones de Java Time (CRÍTICAS para la conversión)
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Pantalla para crear un nuevo evento. (RF01, RF08: Crear evento)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    viewModel: EventViewModel,
    onEventSaved: () -> Unit,
    onNavigateToMap: () -> Unit
) {
    val uiState by viewModel.eventUiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            if (uiState.id == 0) "NUEVO EVENTO" else "ACTUALIZAR EVENTO",
            color = NeonGreen,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // ... (Campos Título, Descripción, Ubicación)

        StyledTextField(
            modifier = Modifier,
            value = uiState.title,
            onValueChange = { viewModel.updateEventUiState(uiState.copy(title = it)) },
            label = "Título (Requerido)",
        )
        Spacer(Modifier.height(16.dp))

        StyledTextField(
            modifier = Modifier,
            value = uiState.description,
            onValueChange = { viewModel.updateEventUiState(uiState.copy(description = it)) },
            label = "Descripción",
        )
        Spacer(Modifier.height(16.dp))

        StyledTextField(
            value = uiState.location,
            onValueChange = { /* No hace nada, es de solo lectura */ },
            label = "Ubicación",
            readOnly = true,
            modifier = Modifier.clickable { onNavigateToMap() }, // El clickable va en el Modifier
            // ✅ Añadimos el ícono aquí
            trailingIcon = {
                IconButton(onClick = onNavigateToMap) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Seleccionar Ubicación")
                }
            }
        )
        Spacer(Modifier.height(16.dp))

        // Fecha y Hora (RF01)
        Row(modifier = Modifier.fillMaxWidth()) {
            // CAMPO DE FECHA
            StyledTextField(
                value = DateUtils.formatDate(uiState.dateTime), // Asume que tienes formatDate()
                onValueChange = { /* No editable directamente */ },
                label = "Fecha",
                readOnly = true,
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker = true } // Lanza solo DatePicker
            )
            Spacer(Modifier.width(16.dp))

            // CAMPO DE HORA
            StyledTextField(
                value = DateUtils.formatTime(uiState.dateTime), // Asume que tienes formatTime()
                onValueChange = { /* No editable directamente */ },
                label = "Hora",
                readOnly = true,
                modifier = Modifier
                    .weight(1f)
                    .clickable { showTimePicker = true } // Lanza solo TimePicker
            )
        }
        Spacer(Modifier.height(16.dp))

        // ------------------ DIÁLOGO DE FECHA (Compose Material 3) ------------------
        if (showDatePicker) {
            // 🚨 CORRECCIÓN 1: Inicialización dentro del if para reflejar el uiState actual
            val initialDateMillis = uiState.dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val newDateTime = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime()

                                val finalDateTime = uiState.dateTime
                                    .withYear(newDateTime.year)
                                    .withMonth(newDateTime.monthValue)
                                    .withDayOfMonth(newDateTime.dayOfMonth)

                                viewModel.updateEventUiState(uiState.copy(dateTime = finalDateTime))

                                showDatePicker = false
                            }
                        }
                    ) { Text("OK", color = NeonGreen) }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancelar", color = NeonRed) }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // ------------------ DIÁLOGO DE HORA (Compose Material 3) ------------------
        if (showTimePicker) {
            // 🚨 CORRECCIÓN 2: Inicialización dentro del if para reflejar el uiState actualizado
            val timePickerState = rememberTimePickerState(
                initialHour = uiState.dateTime.hour,
                initialMinute = uiState.dateTime.minute
            )

            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val newDateTime = uiState.dateTime
                                .withHour(timePickerState.hour)
                                .withMinute(timePickerState.minute)

                            viewModel.updateEventUiState(uiState.copy(dateTime = newDateTime))
                            showTimePicker = false
                        }
                    ) { Text("OK", color = NeonGreen) }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text( "Cancelar", color = NeonRed )
                    }
                },
                // 🚨 CORRECCIÓN 3: Añadimos un título para satisfacer al compilador y resolver el error "No value passed for parameter 'title'".
                title = { Text("Selecciona Hora", color = NeonGreen) }
            ) {
                TimePicker(state = timePickerState)
            }
        }
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.saveEvent(onEventSaved)
            },
            enabled = uiState.isValid(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonGreen,
                contentColor = DarkBackground
            )
        ) {
            val buttonText = if (uiState.id == 0) "GUARDAR EVENTO" else "ACTUALIZAR EVENTO"
            Text(buttonText, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean = false,
    modifier: Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = NeonPurple) },
        readOnly = readOnly,
        // 🚨 CORRECCIÓN 4: Usa el Modifier que se le pasó
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonGreen,
            unfocusedBorderColor = NeonBlue,
            cursorColor = NeonGreen,
            focusedLabelColor = NeonGreen,
            unfocusedLabelColor = NeonPurple,
            focusedTextColor = NeonGreen,
            unfocusedTextColor = NeonPurple,
            unfocusedContainerColor = DarkBackground.copy(alpha = 0.5f),
            focusedContainerColor = DarkBackground.copy(alpha = 0.7f)
        )
    )
}