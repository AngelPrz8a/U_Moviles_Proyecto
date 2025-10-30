package com.uniminuto.recordatorio.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uniminuto.recordatorio.domain.model.Event
import com.uniminuto.recordatorio.domain.repository.EventRepository
import com.uniminuto.recordatorio.util.EventUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * Estado que representa la lista de eventos.
 */
data class EventListState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = true
)

/**
 * ViewModel que gestiona la lógica de las pantallas de eventos (RF01-RF03).
 */
class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {

    // 1. Mis Eventos (Listado)
    // ... (el código de eventListState se mantiene igual)
    val eventListState: StateFlow<EventListState> =
        repository.getEvents()
            .map { events ->
                EventListState(
                    events = events,
                    isLoading = false
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = EventListState(isLoading = true)
            )

    // 2. Crear/Editar Evento (Formulario)
    private val _eventUiState = MutableStateFlow(EventUiState())
    // 🚨 CORRECCIÓN: Usamos .asStateFlow() para garantizar inmutabilidad
    val eventUiState: StateFlow<EventUiState> = _eventUiState.asStateFlow()

    fun updateEventUiState(newUiState: EventUiState) {
        // ✅ Esta función es la clave y está bien: asegura la emisión del nuevo estado
        _eventUiState.update { newUiState }
    }

    // ... (saveEvent, deleteEvent, Factory, y loadEventForEdit se mantienen iguales)

    /**
     * Guarda un evento a partir del estado actual del formulario.
     */
    fun saveEvent(onComplete: () -> Unit) { // <<--- CORRECTION: Remove parentheses from Unit
        val currentUiState = eventUiState.value

        // 1. Mapear el estado a una entidad Event
        val event = Event(
            id = currentUiState.id,
            title = currentUiState.title,
            description = currentUiState.description.ifBlank { null },
            location = currentUiState.location.ifBlank { null },
            dateTime = currentUiState.dateTime,
            isReminderSet = currentUiState.isReminderSet,
            reminderOffsetMinutes = currentUiState.reminderOffsetMinutes
        )

        // 2. Ejecutar la operación en la base de datos
        viewModelScope.launch {
            repository.saveEvent(event)
            onComplete()
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            // Aquí iría la lógica para CANCELAR la alarma (RF04)
        }
    }

    fun loadEventForEdit(eventId: Int) {
        if (eventId == 0) return

        viewModelScope.launch {
            repository.getEventById(eventId)?.let { event ->
                _eventUiState.update {
                    it.copy(
                        id = event.id,
                        title = event.title,
                        description = event.description ?: "",
                        location = event.location ?: "",
                        dateTime = event.dateTime,
                        isReminderSet = event.isReminderSet,
                        reminderOffsetMinutes = event.reminderOffsetMinutes
                    )
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: EventRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
                return EventViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}