package com.uniminuto.recordatorio.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uniminuto.recordatorio.presentation.event.EventViewModel
import com.uniminuto.recordatorio.presentation.event.create.CreateEventScreen
import com.uniminuto.recordatorio.presentation.event.list.EventListScreen

/**
 * Objeto que define las rutas de navegación. (RF08)
 */
sealed class Screen(val route: String) {
    data object EventList : Screen("mis_eventos")
    data object CreateEvent : Screen("crear_evento")
    data object Calendar : Screen("calendario")
    data object Settings : Screen("configuracion")
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
                onNavigateToCreate = { navController.navigate(Screen.CreateEvent.route) }
            )
        }

        composable(Screen.CreateEvent.route) {
            CreateEventScreen(
                viewModel = viewModel,
                onEventSaved = { navController.popBackStack() }
            )
        }

        composable(Screen.Calendar.route) { PlaceholderScreen(title = "Calendario") }
        composable(Screen.Settings.route) { PlaceholderScreen(title = "Configuración") }
    }
}