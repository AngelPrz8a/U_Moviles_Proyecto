package com.uniminuto.recordatorio.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uniminuto.recordatorio.presentation.event.EventViewModel
import com.uniminuto.recordatorio.presentation.event.calendar.CalendarScreen
import com.uniminuto.recordatorio.presentation.event.create.CreateEventScreen
import com.uniminuto.recordatorio.presentation.event.list.EventListScreen
import com.uniminuto.recordatorio.presentation.ui.map.GeocodingMapScreen
import androidx.compose.runtime.livedata.observeAsState

/**
 * Objeto que define las rutas de navegación. (RF08)
 */
sealed class Screen(val route: String) {
    data object EventList : Screen("mis_eventos")
    data object CreateEvent : Screen("crear_evento?eventId={eventId}")
    data object MapSelector : Screen("map_selector")
    data object Calendar : Screen("calendario")
    fun createRouteWithId(eventId: Int): String {
        return "crear_evento?eventId=$eventId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: EventViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.EventList.route,
        modifier = modifier
    ) {
        composable(Screen.EventList.route) {
            EventListScreen(
                viewModel = viewModel,
                onNavigateToEdit = { eventId ->
                    navController.navigate(Screen.CreateEvent.createRouteWithId(eventId))
                }
            )
        }

// En NavGraph.kt

        composable(
            Screen.CreateEvent.route,
            listOf(
                navArgument("eventId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0

            // ✅ --- LÓGICA DE CARGA DEL EVENTO --- ✅
            // Este LaunchedEffect SOLO se encarga de cargar el evento para editar o limpiar para crear.
            // Se ejecuta solo una vez cuando el composable aparece.
            LaunchedEffect(key1 = Unit) { // Usamos Unit para que se ejecute solo una vez
                if (eventId != 0) {
                    viewModel.loadEventForEdit(eventId)
                } else {
                    // Limpiamos el formulario para un evento nuevo
                    viewModel.updateEventUiState(com.uniminuto.recordatorio.util.EventUiState())
                }
            }

            // --- LÓGICA DEL RESULTADO DEL MAPA ---
            val resultLiveData = remember(backStackEntry) {
                backStackEntry.savedStateHandle.getLiveData<String>("selected_location_key")
            }
            val selectedLocation by resultLiveData.observeAsState()

            // ✅ Este LaunchedEffect SOLO se encarga de reaccionar al cambio de ubicación.
            LaunchedEffect(selectedLocation) {
                selectedLocation?.let { locationString ->
                    // Actualiza el ViewModel con la nueva ubicación
                    viewModel.updateEventUiState(viewModel.eventUiState.value.copy(location = locationString))

                    // Limpia el valor para que no se vuelva a usar si navegas hacia atrás y adelante
                    backStackEntry.savedStateHandle.remove<String>("selected_location_key")
                }
            }

            // --- RENDERIZADO DE LA PANTALLA ---
            CreateEventScreen(
                viewModel = viewModel,
                onEventSaved = {
                    navController.popBackStack()
                },
                onNavigateToMap = {
                    navController.navigate(Screen.MapSelector.route)
                }
            )
        }


        composable(Screen.MapSelector.route) {
            GeocodingMapScreen(
                navController = navController,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Calendar.route) {
            CalendarScreen(viewModel = viewModel)
        }
    }
}