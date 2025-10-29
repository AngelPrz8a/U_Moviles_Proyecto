package com.uniminuto.recordatorio.presentation.ui

import androidx.compose.runtime.Composable
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

/**
 * Objeto que define las rutas de navegación. (RF08)
 */
sealed class Screen(val route: String) {
    data object EventList : Screen("mis_eventos")
    data object CreateEvent : Screen("crear_evento?eventId={eventId}")
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

        composable(
            Screen.CreateEvent.route,
            listOf(
                navArgument("eventId") {
                    type = NavType.IntType
                    defaultValue = 0 // El ID 0 significa que es un evento nuevo
                }
            )
        ) {backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0

            // 🚨 CRÍTICO: Cargar los datos del evento si es edición (ID != 0)
            if (eventId != 0) {
                viewModel.loadEventForEdit(eventId)
            }
            CreateEventScreen(
                viewModel = viewModel,
                onEventSaved = {
                    // Al guardar, limpiamos el estado del formulario y volvemos a la lista
                    viewModel.updateEventUiState(com.uniminuto.recordatorio.util.EventUiState())
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Calendar.route) {
            CalendarScreen(viewModel = viewModel)
        }
    }
}