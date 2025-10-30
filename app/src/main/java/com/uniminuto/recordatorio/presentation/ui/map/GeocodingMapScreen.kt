package com.uniminuto.recordatorio.presentation.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.uniminuto.recordatorio.presentation.ui.theme.DarkBackground
import com.uniminuto.recordatorio.presentation.ui.theme.NeonGreen
import com.uniminuto.recordatorio.presentation.ui.theme.NeonPurple
import java.io.IOException
import java.util.Locale

// LatLng por defecto: Bogotá (como ejemplo)
private val DEFAULT_LOCATION = LatLng(4.7110, -74.0721)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun GeocodingMapScreen(
    //onLocationSelected: (String) -> Unit,
    onNavigateBack: () -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // 1. Estados de Ubicación
    var currentCameraPosition by remember { mutableStateOf(DEFAULT_LOCATION) }
    var selectedMarkerPosition by remember { mutableStateOf(DEFAULT_LOCATION) }
    var selectedAddress by remember { mutableStateOf("Arrastre el mapa o pulse para seleccionar...") }

    // Estado de Permisos
    var hasLocationPermission by remember { mutableStateOf(false) }

    // 2. Launcher para solicitar permisos
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            if (hasLocationPermission) {
                // Si el permiso es concedido, cargar ubicación
                loadInitialLocation(fusedLocationClient, context) { latLng ->
                    currentCameraPosition = latLng
                    selectedMarkerPosition = latLng
                    updateAddress(context, latLng) { addr -> selectedAddress = addr }
                }
            } else {
                // Usar ubicación por defecto si no hay permiso
                updateAddress(context, DEFAULT_LOCATION) { addr -> selectedAddress = addr }
            }
        }
    )

    // 3. Efecto para cargar ubicación inicial
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar Ubicación", color = NeonGreen) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground),
                actions = {
                    IconButton(onClick = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_location_key", selectedAddress)

                        // Ahora navega hacia atrás
                        onNavigateBack()
                    }) {
                        Icon(Icons.Filled.Done, contentDescription = "Guardar", tint = NeonGreen)
                    }
                }
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Dirección Seleccionada
            Text(
                text = selectedAddress,
                color = NeonPurple,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground.copy(alpha = 0.8f))
                    .padding(16.dp)
            )

            // Mapa de Google
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentCameraPosition, 15f)
            }

            // Sincronizar la cámara con la posición actual si cambia
            LaunchedEffect(currentCameraPosition) {
                val newPosition = CameraPosition.fromLatLngZoom(currentCameraPosition, 15f)
                cameraPositionState.position = newPosition
            }

            GoogleMap(
                modifier = Modifier.weight(1f),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission,
                    mapStyleOptions = MapStyleOptions(DARK_STYLE)
                ),
                onMapClick = { latLng ->
                    selectedMarkerPosition = latLng
                    updateAddress(context, latLng) { addr -> selectedAddress = addr }
                }
            ) {
                // Marcador que representa la ubicación seleccionada
                Marker(
                    state = MarkerState(position = selectedMarkerPosition),
                    title = "Ubicación del Evento"
                )
            }
        }
    }
}

// ----------------- Funciones de Utilidad -----------------

/**
 * Intenta obtener la última ubicación conocida.
 */
@SuppressLint("MissingPermission")
private fun loadInitialLocation(
    fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
    context: Context,
    onSuccess: (LatLng) -> Unit
) {
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                onSuccess(LatLng(location.latitude, location.longitude))
            } else {
                // Si la última ubicación es nula, usar la predeterminada
                onSuccess(DEFAULT_LOCATION)
            }
        }
        .addOnFailureListener {
            onSuccess(DEFAULT_LOCATION)
        }
}

/**
 * Convierte LatLng a una dirección legible usando Geocoder.
 */
private fun updateAddress(context: Context, latLng: LatLng, onAddressFound: (String) -> Unit) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Usar método asíncrono moderno (API 33+)
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addresses ->
                val addressText = addresses.firstOrNull()?.getAddressLine(0) ?: "Ubicación desconocida"
                onAddressFound(addressText)
            }
        } else {
            // Usar método síncrono obsoleto (para compatibilidad)
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val addressText = addresses?.firstOrNull()?.getAddressLine(0) ?: "Ubicación desconocida"
            onAddressFound(addressText)
        }
    } catch (e: IOException) {
        onAddressFound("Error de geocodificación")
        Log.e("Geocoding", "Error de red/IO: ${e.message}")
    }
}

// Estilo JSON para un mapa oscuro (Dark Mode, Estilo Neón)
private const val DARK_STYLE = """
[
  {"featureType": "all","elementType": "geometry","stylers": [{"color": "#242f3e"}]},
  {"featureType": "all","elementType": "labels.text.fill","stylers": [{"color": "#746855"}]},
  {"featureType": "all","elementType": "labels.text.stroke","stylers": [{"color": "#242f3e"}]},
  {"featureType": "administrative.locality","elementType": "labels.text.fill","stylers": [{"color": "#d59563"}]},
  {"featureType": "poi","elementType": "labels.text.fill","stylers": [{"color": "#f8f8f8"}]},
  {"featureType": "poi.park","elementType": "geometry","stylers": [{"color": "#263c3f"}]},
  {"featureType": "poi.park","elementType": "labels.text.fill","stylers": [{"color": "#6b9a76"}]},
  {"featureType": "road","elementType": "geometry","stylers": [{"color": "#38414e"}]},
  {"featureType": "road","elementType": "geometry.stroke","stylers": [{"color": "#212a37"}]},
  {"featureType": "road","elementType": "labels.text.fill","stylers": [{"color": "#9ca5b3"}]},
  {"featureType": "road.highway","elementType": "geometry","stylers": [{"color": "#5e5e5e"}]},
  {"featureType": "road.highway","elementType": "geometry.stroke","stylers": [{"color": "#1f2835"}]},
  {"featureType": "road.highway","elementType": "labels.text.fill","stylers": [{"color": "#d3d3d3"}]},
  {"featureType": "transit","elementType": "geometry","stylers": [{"color": "#2f3948"}]},
  {"featureType": "transit.station","elementType": "labels.text.fill","stylers": [{"color": "#d59563"}]},
  {"featureType": "water","elementType": "geometry","stylers": [{"color": "#17263c"}]},
  {"featureType": "water","elementType": "labels.text.fill","stylers": [{"color": "#515c6d"}]}
]
"""