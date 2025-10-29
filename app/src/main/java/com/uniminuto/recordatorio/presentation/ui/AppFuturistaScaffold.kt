package com.uniminuto.recordatorio.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uniminuto.recordatorio.presentation.event.EventViewModel
import com.uniminuto.recordatorio.presentation.ui.theme.DarkBackground
import com.uniminuto.recordatorio.presentation.ui.theme.NeonBlue
import com.uniminuto.recordatorio.presentation.ui.theme.NeonGreen
import com.uniminuto.recordatorio.presentation.ui.theme.NeonPurple

// Definición de ítems para la Bottom Bar (RF08)
data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val navItems = listOf(
    NavItem(Screen.EventList.route, Icons.AutoMirrored.Filled.List, "Eventos"),
    NavItem(Screen.CreateEvent.route, Icons.Default.Add, "Crear"),
    NavItem(Screen.Calendar.route, Icons.Default.DateRange, "Calendario"),
)

@Composable
fun AppFuturistaScaffold(viewModel: EventViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomBar(navController = navController) },
        containerColor = DarkBackground // Asegura el fondo oscuro
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = DarkBackground.copy(alpha = 0.9f) // Fondo oscuro semi-transparente
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        // Lógica de navegación (RF09)
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) NeonGreen else NeonPurple
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) NeonGreen else NeonPurple
                    )
                }
            )
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "$title (En Desarrollo)",
            color = NeonBlue,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Light
        )
    }
}
